//
//  IMUICameraCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos

private enum SessionSetupResult {
		case success
		case notAuthorized
		case configurationFailed
}

private enum LivePhotoMode {
		case on
		case off
}

class IMUICameraCell: UICollectionViewCell, IMUIFeatureCellProtocal {

  @IBOutlet weak var cameraPreviewView: IMUICameraPreviewView!
  weak var delegate: IMUIInputViewDelegate?
  
  var inputViewDelegate: IMUIInputViewDelegate? {
    set {
      self.delegate = newValue
    }
    
    get {
      return self.delegate
    }
  }
  
  private var inProgressPhotoCaptureDelegates = [Int64 : IMUIPhotoCaptureDelegate]()
  private var inProgressLivePhotoCapturesCount = 0
  
  private var isPhotoMode: Bool = true {
    didSet {
    
    }
  }

  private let stillImageOutput = AVCaptureStillImageOutput()
  private let session = AVCaptureSession()
  private var setupResult: SessionSetupResult = .success
  private let photoOutput = AVCapturePhotoOutput()
  var videoDeviceInput: AVCaptureDeviceInput!
  private var livePhotoMode: LivePhotoMode = .off
  var backgroundRecordingID: UIBackgroundTaskIdentifier? = nil
  private var movieFileOutput: AVCaptureMovieFileOutput? = nil
  private var isSessionRunning = false
  private var sessionRunningObserveContext = 0
  private let sessionQueue = DispatchQueue(label: "session queue", attributes: [], target: nil) // Communicate with the session and other session objects on this
  
  override func awakeFromNib() {
    super.awakeFromNib()
    cameraPreviewView.session = session
    
    switch AVCaptureDevice.authorizationStatus(forMediaType: AVMediaTypeVideo) {
    case .authorized:
      // The user has previously granted access to the camera.
      break
      
    case .notDetermined:
      sessionQueue.suspend()
      AVCaptureDevice.requestAccess(forMediaType: AVMediaTypeVideo, completionHandler: { [unowned self] granted in
        if !granted {
          self.setupResult = .notAuthorized
        }
        self.sessionQueue.resume()
      })
      
    default:
      // The user has previously denied access.
      setupResult = .notAuthorized
    }
    
    sessionQueue.async { [unowned self] in
      self.configureSession()
    }
  }
  
  
  override func layoutSubviews() {
    super.layoutSubviews()
    sessionQueue.async {
      switch self.setupResult {
      case .success:
        self.session.startRunning()
        self.isSessionRunning = self.session.isRunning
        
      case .notAuthorized:
        DispatchQueue.main.async { [unowned self] in
          print("AVCam doesn't have permission to use the camera, please change privacy settings")
        }
        
      case .configurationFailed:
        DispatchQueue.main.async { [unowned self] in
          print("Unable to capture media")
        }
      }
    }
  }
  
  // -MARK: Click Event
  @IBAction func clickCameraSwitch(_ sender: Any) {
    if #available(iOS 10.0, *) {
      self.capturePhotoAfter_iOS10()
    } else {
      self.capturePhotoBefore_iOS8()
    }
  }
  
  @IBAction func clickToSwitchCamera(_ sender: Any) {
    
  }
  
  @IBAction func clickToChangeCameraMode(_ sender: Any) {
    
  }
  
  @IBAction func clickToAdjustCameraViewSize(_ sender: Any) {
    
  }
  
  func capturePhotoAfter_iOS10() {
    /*
     Retrieve the video preview layer's video orientation on the main queue before
     entering the session queue. We do this to ensure UI elements are accessed on
     the main thread and session configuration is done on the session queue.
     */
    let videoPreviewLayerOrientation = cameraPreviewView.videoPreviewLayer.connection.videoOrientation
    
    sessionQueue.async {
      // Update the photo output's connection to match the video orientation of the video preview layer.
      if let photoOutputConnection = self.photoOutput.connection(withMediaType: AVMediaTypeVideo) {
        photoOutputConnection.videoOrientation = videoPreviewLayerOrientation
      }
      
      // Capture a JPEG photo with flash set to auto and high resolution photo enabled.
      let photoSettings = AVCapturePhotoSettings()
      photoSettings.flashMode = .auto
      
      photoSettings.isHighResolutionPhotoEnabled = false
      
      if photoSettings.availablePreviewPhotoPixelFormatTypes.count > 0 {
        photoSettings.previewPhotoFormat = [kCVPixelBufferPixelFormatTypeKey as String : photoSettings.availablePreviewPhotoPixelFormatTypes.first!]
      }
      if self.livePhotoMode == .on && self.photoOutput.isLivePhotoCaptureSupported { // Live Photo capture is not supported in movie mode.
        let livePhotoMovieFileName = NSUUID().uuidString
        let livePhotoMovieFilePath = (NSTemporaryDirectory() as NSString).appendingPathComponent((livePhotoMovieFileName as NSString).appendingPathExtension("mov")!)
        photoSettings.livePhotoMovieFileURL = URL(fileURLWithPath: livePhotoMovieFilePath)
      }
      
      // Use a separate object for the photo capture delegate to isolate each capture life cycle.
      let photoCaptureDelegate = IMUIPhotoCaptureDelegate(with: photoSettings, willCapturePhotoAnimation: {
        DispatchQueue.main.async { [unowned self] in
          self.cameraPreviewView.videoPreviewLayer.opacity = 0
          UIView.animate(withDuration: 0.25) { [unowned self] in
            self.cameraPreviewView.videoPreviewLayer.opacity = 1
          }
        }
      }, capturingLivePhoto: { capturing in
        /*
         Because Live Photo captures can overlap, we need to keep track of the
         number of in progress Live Photo captures to ensure that the
         Live Photo label stays visible during these captures.
         */
        self.sessionQueue.async { [unowned self] in
          if capturing {
            self.inProgressLivePhotoCapturesCount += 1
          }
          else {
            self.inProgressLivePhotoCapturesCount -= 1
          }
          
          let inProgressLivePhotoCapturesCount = self.inProgressLivePhotoCapturesCount
          DispatchQueue.main.async { [unowned self] in
            if inProgressLivePhotoCapturesCount > 0 {
//              self.capturingLivePhotoLabel.isHidden = false
            }
            else if inProgressLivePhotoCapturesCount == 0 {
//              self.capturingLivePhotoLabel.isHidden = true
            }
            else {
              print("Error: In progress live photo capture count is less than 0");
            }
          }
        }
      }, completed: { [unowned self] photoCaptureDelegate in
        self.inputViewDelegate?.finishShootPicture?(picture: UIImage(data: photoCaptureDelegate.photoData!)!)
        self.sessionQueue.async { [unowned self] in
          self.inProgressPhotoCaptureDelegates[photoCaptureDelegate.requestedPhotoSettings.uniqueID] = nil
        }
        }
      )
      
      /*
       The Photo Output keeps a weak reference to the photo capture delegate so
       we store it in an array to maintain a strong reference to this object
       until the capture is completed.
       */
      self.inProgressPhotoCaptureDelegates[photoCaptureDelegate.requestedPhotoSettings.uniqueID] = photoCaptureDelegate
      self.photoOutput.capturePhoto(with: photoSettings, delegate: photoCaptureDelegate)
    }
  }
  
  private func capturePhotoBefore_iOS8() {
  
    var videoConnection: AVCaptureConnection? = nil
    for connection in stillImageOutput.connections {
      let theConnection = connection as! AVCaptureConnection
      for port in theConnection.inputPorts {
        if ((port as? AVCaptureInputPort)?.isEqual(AVMediaTypeVideo))! {
          videoConnection = theConnection
          break
        }
      }
      
      if (videoConnection != nil) {
        break
      }
    }
    print("about to request a capture from: \(stillImageOutput)")
    stillImageOutput.captureStillImageAsynchronously(from: videoConnection) { (imageSampleBuffer, error) in
      var exifAttachments = CMGetAttachment(imageSampleBuffer!, kCGImagePropertyExifDictionary, nil)
      
      if (exifAttachments != nil) {
        print("exifAttachments exit")
      } else {
        print("exifAttachments not exit")
      }
      
      let imageData = AVCaptureStillImageOutput.jpegStillImageNSDataRepresentation(imageSampleBuffer)
      let image = UIImage(data: imageData!)
      UIImageWriteToSavedPhotosAlbum(image!, nil, nil, nil)

    }
  }
  
  private func configureSession() {
    if setupResult != .success {
      return
    }
    
    session.beginConfiguration()
    session.sessionPreset = AVCaptureSessionPresetPhoto
    
    do {
      var defaultVideoDevice: AVCaptureDevice?
      
      if let dualCameraDevice = AVCaptureDevice.defaultDevice(withDeviceType: .builtInDuoCamera, mediaType: AVMediaTypeVideo, position: .back) {
        defaultVideoDevice = dualCameraDevice
      }
      else if let backCameraDevice = AVCaptureDevice.defaultDevice(withDeviceType: .builtInWideAngleCamera, mediaType: AVMediaTypeVideo, position: .back) {
        defaultVideoDevice = backCameraDevice
      }
      else if let frontCameraDevice = AVCaptureDevice.defaultDevice(withDeviceType: .builtInWideAngleCamera, mediaType: AVMediaTypeVideo, position: .front) {
        
        defaultVideoDevice = frontCameraDevice
      }
      
      let videoDeviceInput = try AVCaptureDeviceInput(device: defaultVideoDevice)
      
      if session.canAddInput(videoDeviceInput) {
        session.addInput(videoDeviceInput)
        self.videoDeviceInput = videoDeviceInput
        
        DispatchQueue.main.async {
          
          let statusBarOrientation = UIApplication.shared.statusBarOrientation
          var initialVideoOrientation: AVCaptureVideoOrientation = .portrait
          if statusBarOrientation != .unknown {
            if let videoOrientation = statusBarOrientation.videoOrientation {
              initialVideoOrientation = videoOrientation
            }
          }
          
          self.cameraPreviewView.videoPreviewLayer.connection.videoOrientation = initialVideoOrientation
        }
      }
      else {
        print("Could not add video device input to the session")
        setupResult = .configurationFailed
        session.commitConfiguration()
        return
      }
    }
    catch {
      print("Could not create video device input: \(error)")
      setupResult = .configurationFailed
      session.commitConfiguration()
      return
    }

    do {
      let audioDevice = AVCaptureDevice.defaultDevice(withMediaType: AVMediaTypeAudio)
      let audioDeviceInput = try AVCaptureDeviceInput(device: audioDevice)
      
      if session.canAddInput(audioDeviceInput) {
        session.addInput(audioDeviceInput)
      }
      else {
        print("Could not add audio device input to the session")
      }
    }
    catch {
      print("Could not create audio device input: \(error)")
    }

    if session.canAddOutput(photoOutput)
    {
      session.addOutput(photoOutput)
      
      photoOutput.isHighResolutionCaptureEnabled = true
      photoOutput.isLivePhotoCaptureEnabled = photoOutput.isLivePhotoCaptureSupported
      livePhotoMode = photoOutput.isLivePhotoCaptureSupported ? .on : .off
    }
    else {
      print("Could not add photo output to the session")
      setupResult = .configurationFailed
      session.commitConfiguration()
      return
    }
    
    session.commitConfiguration()
  }

}

extension UIInterfaceOrientation {
  var videoOrientation: AVCaptureVideoOrientation? {
    switch self {
    case .portrait: return .portrait
    case .portraitUpsideDown: return .portraitUpsideDown
    case .landscapeLeft: return .landscapeLeft
    case .landscapeRight: return .landscapeRight
    default: return nil
    }
  }
}

