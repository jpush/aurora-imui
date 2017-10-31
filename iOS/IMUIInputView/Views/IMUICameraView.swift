//
//  IMUICameraView.swift
//  sample
//
//  Created by oshumini on 2017/9/15.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos
import AVFoundation

private enum SessionSetupResult {
		case success
		case notAuthorized
		case configurationFailed
}

private enum LivePhotoMode {
		case on
		case off
}

@available(iOS 8.0, *)
class IMUICameraView: UIView {
  
  public typealias PathCallback = (String, Double) -> ()
  public typealias DataCallback = (Data) -> ()
  public typealias ButtonOnClickCallback = (UIButton) -> ()
  
  public var recordVideoCallback: PathCallback?
  public var shootPictureCallback: DataCallback?
  public var onClickFullSizeCallback: ButtonOnClickCallback?
  
  @IBOutlet var view: UIView!
  
  var isFullScreenMode = false
  
  @IBOutlet weak var switchCameraModeBtn: UIButton!
  @IBOutlet weak var cameraShotBtn: UIButton!
  @IBOutlet weak var videoRecordBtn: UIButton!
  
  @IBOutlet weak var switchCameraDeviceBtn: UIButton!
  @IBOutlet weak var resizeCameraPreviewBtn: UIButton!
  @IBOutlet weak var cameraPreviewView: IMUICameraPreviewView!
  
  var inConfiging: Bool = false
  var currentCameraDeviceType: AVCaptureDevice.Position = .back
  
  var currentCameraDevice: AVCaptureDeviceInput?
  
  var isActivity = true


  
  open override func awakeFromNib() {
    super.awakeFromNib()
  
    self.videoRecordBtn.isHidden = true
    self.layer.masksToBounds = true
    cameraPreviewView.session = session
    
    switch AVCaptureDevice.authorizationStatus(for: AVMediaType.video) {
    case .authorized:
      // The user has previously granted access to the camera.
      break
      
    case .notDetermined:
      sessionQueue.suspend()
      AVCaptureDevice.requestAccess(for: AVMediaType.video, completionHandler: { [unowned self] granted in
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
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUICameraView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)

    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUICameraView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  private func configureSession() {
    if setupResult != .success {
      return
    }
    
    session.beginConfiguration()
    self.inConfiging = true
    session.sessionPreset = AVCaptureSession.Preset.photo
    
    do {
      var defaultVideoDevice: AVCaptureDevice?
      
      if #available(iOS 10.0, *) {
        
        if let dualCameraDevice = AVCaptureDevice.default(.builtInDuoCamera, for: AVMediaType.video, position: .back) {
          defaultVideoDevice = dualCameraDevice
          currentCameraDeviceType = .back
        }
        else if let backCameraDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType.video, position: .back) {
          defaultVideoDevice = backCameraDevice
          currentCameraDeviceType = .back
        }
        else if let frontCameraDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType.video, position: .front) {
          
          defaultVideoDevice = frontCameraDevice
          currentCameraDeviceType = .front
        }
      } else {
        
        defaultVideoDevice = AVCaptureDevice.default(for: AVMediaType.video)
        
        currentCameraDeviceType = .back
      }
      
      let videoDeviceInput = try AVCaptureDeviceInput(device: defaultVideoDevice!)
      currentCameraDevice = videoDeviceInput
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
          
          self.cameraPreviewView.videoPreviewLayer.connection?.videoOrientation = initialVideoOrientation
        }
      } else {
        print("Could not add video device input to the session")
        setupResult = .configurationFailed
        session.commitConfiguration()
        self.inConfiging = false
        return
      }
    } catch {
      print("Could not create video device input: \(error)")
      setupResult = .configurationFailed
      session.commitConfiguration()
      self.inConfiging = false
      return
    }
    
    do {
      let audioDevice = AVCaptureDevice.default(for: AVMediaType.audio)
      let audioDeviceInput = try AVCaptureDeviceInput(device: audioDevice!)
      
      if session.canAddInput(audioDeviceInput) {
        session.addInput(audioDeviceInput)
      } else {
        print("Could not add audio device input to the session")
      }
    } catch {
      print("Could not create audio device input: \(error)")
    }
    
    // configure output
    
    if #available(iOS 10.0, *) {
      self.photoOutput = AVCapturePhotoOutput()
      
      if session.canAddOutput(self.photoOutput!) {
        session.addOutput(self.photoOutput!)
        self.photoOutput?.isHighResolutionCaptureEnabled = true
        self.photoOutput?.isLivePhotoCaptureEnabled = (self.photoOutput?.isLivePhotoCaptureSupported)!
        livePhotoMode = (photoOutput?.isLivePhotoCaptureSupported)! ? .on : .off
      } else {
        print("Could not add photo output to the session")
        setupResult = .configurationFailed
        session.commitConfiguration()
        self.inConfiging = false
        return
      }
    } else {
      if session.canAddOutput(stillImageOutput) {
        session.addOutput(stillImageOutput)
      }
    }
    
    videoFileOutput = AVCaptureMovieFileOutput()
    if session.canAddOutput(videoFileOutput!) {
      session.addOutput(videoFileOutput!)
      let maxDuration = CMTime(seconds: 20, preferredTimescale: 1)
      videoFileOutput?.maxRecordedDuration = maxDuration
      videoFileOutput?.minFreeDiskSpaceLimit = 1000
    }
    
    session.commitConfiguration()
    self.inConfiging = false
  }
  
  func activateMedia() {
    isActivity = true
    sessionQueue.async {
      switch self.setupResult {
      case .success:
        self.session.startRunning()
        self.isSessionRunning = self.session.isRunning
        
      case .notAuthorized:
        print("AVCam doesn't have permission to use the camera, please change privacy settings")
        
      case .configurationFailed:
        print("Unable to capture media")
      }
    }
    
    self.videoRecordBtn.isHidden = true
    self.videoRecordBtn.isSelected = false
    self.cameraShotBtn.isHidden = false
    self.cameraShotBtn.isSelected = false
    self.switchCameraModeBtn.isSelected = false
    isPhotoMode = true
    //    self.chooseCamera(with: .back)
  }
  
  func inactivateMedia() {
    isActivity = false
    
    if let videofile = videoFileOutput {
      if videofile.isRecording {
        videoFileOutput?.stopRecording()
      }
    }
    if !self.inConfiging {
      self.session.stopRunning()
    }
  }
  
  func chooseCamera(with cameraDevicePosition: AVCaptureDevice.Position) {
    // remove current camera device
    session.beginConfiguration()
    self.inConfiging = true
    session.removeInput(currentCameraDevice!)
    
    // add new camera device
    
    var defaultVideoDevice: AVCaptureDevice?
    
    if #available(iOS 10.0, *) {
      if cameraDevicePosition == .front {
        if let frontCameraDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType.video, position: .front) {
          defaultVideoDevice = frontCameraDevice
          currentCameraDeviceType = .front
        }
      }
      
      if cameraDevicePosition == .back {
        
        if let dualCameraDevice = AVCaptureDevice.default(.builtInDuoCamera, for: AVMediaType.video, position: .back) {
          if defaultVideoDevice == nil {
            defaultVideoDevice = dualCameraDevice
            currentCameraDeviceType = .back
          }
          
        } else if let backCameraDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType.video, position: .back) {
          if defaultVideoDevice == nil {
            defaultVideoDevice = backCameraDevice
            currentCameraDeviceType = .back
          }
        }
      }
      
    } else {
      
      for device in AVCaptureDevice.devices() {
        if (device as AnyObject).hasMediaType( AVMediaType.video ) {
          if (device as AnyObject).position == cameraDevicePosition {
            defaultVideoDevice = device as? AVCaptureDevice
            currentCameraDeviceType = cameraDevicePosition
          }
        }
      }
      
      if defaultVideoDevice == nil {
        defaultVideoDevice = AVCaptureDevice.default(for: AVMediaType.video)
        currentCameraDeviceType = .back
      }
    }
    
    do {
      let videoDeviceInput = try AVCaptureDeviceInput(device: defaultVideoDevice!)
      
      currentCameraDevice = videoDeviceInput
      
      if session.canAddInput(videoDeviceInput) {
        session.addInput(videoDeviceInput)
        session.commitConfiguration()
        self.inConfiging = false
        self.videoDeviceInput = videoDeviceInput
        
        DispatchQueue.main.async {
          
          let statusBarOrientation = UIApplication.shared.statusBarOrientation
          var initialVideoOrientation: AVCaptureVideoOrientation = .portrait
          if statusBarOrientation != .unknown {
            if let videoOrientation = statusBarOrientation.videoOrientation {
              initialVideoOrientation = videoOrientation
            }
          }
          
          self.cameraPreviewView.videoPreviewLayer.connection?.videoOrientation = initialVideoOrientation
          
        }
      } else {
        print("Could not add video device input to the session")
        setupResult = .configurationFailed
        session.commitConfiguration()
        self.inConfiging = false
        return
      }
    } catch {
      print("add camera device fail")
      setupResult = .configurationFailed
      session.commitConfiguration()
      self.inConfiging = false
    }
    
  }
  
  private var _inProgressPhotoCaptureDelegates: Any?
  @available(iOS 10.0, *)
  var inProgressPhotoCaptureDelegates: [Int64 : IMUIPhotoCaptureDelegate] {
    get {
      if _inProgressPhotoCaptureDelegates == nil {
        _inProgressPhotoCaptureDelegates = [Int64 : IMUIPhotoCaptureDelegate]()
      }
      return _inProgressPhotoCaptureDelegates as! [Int64 : IMUIPhotoCaptureDelegate]
    }
    
    set {
      _inProgressPhotoCaptureDelegates = newValue
    }
  }
  
  private var inProgressLivePhotoCapturesCount = 0
  
  private var isPhotoMode: Bool = true {
    didSet {
      self.switchCameraModeBtn.isSelected = !isPhotoMode
      self.cameraShotBtn.isSelected = !isPhotoMode
    }
  }
  
  private let stillImageOutput = AVCaptureStillImageOutput()
  private let session = AVCaptureSession()
  private var setupResult: SessionSetupResult = .success
  
  var videoFileOutput: AVCaptureMovieFileOutput?
  
  // OutPut
  private var _photoOutput: Any?
  @available(iOS 10.0, *)
  var photoOutput: AVCapturePhotoOutput? {
    get {
      return _photoOutput as? AVCapturePhotoOutput
    }
    
    set {
      _photoOutput = newValue
    }
  }
  
  var videoDeviceInput: AVCaptureDeviceInput!
  private var livePhotoMode: LivePhotoMode = .off
  var backgroundRecordingID: UIBackgroundTaskIdentifier? = nil
  
  private var isSessionRunning = false
  private var sessionRunningObserveContext = 0
  private let sessionQueue = DispatchQueue(label: "session queue", attributes: [], target: nil) // Communicate with the session and other session objects on this
  
  
  // -MARK: Click Event
  @IBAction func clickCameraSwitch(_ sender: Any) {
    if isPhotoMode {
      if #available(iOS 10.0, *) {
        self.capturePhotoAfter_iOS10()
      } else {
        self.capturePhotoBefore_iOS8()
      }
    } else {
      
      if !(videoFileOutput!.isRecording) {
        
        let outputPath = self.getPath()
        let fileManager = FileManager()
        if fileManager.fileExists(atPath: outputPath) {
          do {
            try fileManager.removeItem(at: URL(fileURLWithPath: outputPath))
          } catch {
            print("removefile fail")
          }
          
        }
        session.beginConfiguration()
        self.inConfiging = true
        session.sessionPreset = AVCaptureSession.Preset.cif352x288
        session.commitConfiguration()
        self.inConfiging = false
        videoFileOutput?.startRecording(to: URL(fileURLWithPath: outputPath), recordingDelegate: self)
      } else {
        videoFileOutput?.stopRecording()
      }
    }
    
  }
  
  @IBAction func clickVideoRecordSwitch(_ sender: Any) {
    videoRecordBtn.isSelected = !videoRecordBtn.isSelected
    
    if !(videoFileOutput!.isRecording) {
      
      let outputPath = self.getPath()
      let fileManager = FileManager()
      if fileManager.fileExists(atPath: outputPath) {
        do {
          try fileManager.removeItem(at: URL(fileURLWithPath: outputPath))
        } catch {
          print("removefile fail")
        }
        
      }
      session.beginConfiguration()
      self.inConfiging = true
      session.sessionPreset = AVCaptureSession.Preset.cif352x288
      session.commitConfiguration()
      self.inConfiging = false
      videoFileOutput?.startRecording(to: URL(fileURLWithPath: outputPath), recordingDelegate: self)
    } else {
      videoFileOutput?.stopRecording()
    }
    
  }
  
  @IBAction func clickToSwitchCamera(_ sender: Any) {
    switch currentCameraDeviceType {
    case .back:
      self.chooseCamera(with: .front)
      break
    default:
      self.chooseCamera(with: .back)
      break
    }
    
  }
  
  @IBAction func clickToChangeCameraMode(_ sender: Any) {
    
    isPhotoMode = !isPhotoMode
    if isPhotoMode {
      session.sessionPreset = AVCaptureSession.Preset.photo
      videoRecordBtn.isHidden = true
      cameraShotBtn.isHidden = false
    } else {
      session.sessionPreset = AVCaptureSession.Preset.cif352x288
      videoRecordBtn.isHidden = false
      cameraShotBtn.isHidden = true
    }
  }
  
  @IBAction func clickToAdjustCameraViewSize(_ sender: UIButton) {
//    if sender.isSelected {
//      self.cameraVC?.view = UIView()
//      self.cameraVC?.dismiss(animated: true, completion: { })
//    } else {
//      
//      let rootVC = UIApplication.shared.delegate?.window??.rootViewController
//      self.cameraVC = UIViewController()
//      self.cameraVC?.view = self
//      
//      
//      rootVC?.present(self.cameraVC!, animated: true, completion: {
//        
//      })
//      
//    }
//
    
    self.onClickFullSizeCallback?(sender)
    sender.isSelected = !sender.isSelected
  }
  
  @available(iOS 10.0, *)
  func capturePhotoAfter_iOS10() {
    let videoPreviewLayerOrientation = cameraPreviewView.videoPreviewLayer.connection?.videoOrientation
    
    sessionQueue.async {
      if let photoOutputConnection = self.photoOutput?.connection(with: AVMediaType.video) {
        photoOutputConnection.videoOrientation = videoPreviewLayerOrientation!
      }
      
      let photoSettings = AVCapturePhotoSettings()
      photoSettings.flashMode = .auto
      
      photoSettings.isHighResolutionPhotoEnabled = false
      
      if photoSettings.availablePreviewPhotoPixelFormatTypes.count > 0 {
        photoSettings.previewPhotoFormat = [kCVPixelBufferPixelFormatTypeKey as String : photoSettings.availablePreviewPhotoPixelFormatTypes.first!]
      }
      
      if self.livePhotoMode == .on && (self.photoOutput?.isLivePhotoCaptureSupported)! { // Live Photo capture is not supported in movie mode.
        let livePhotoMovieFileName = NSUUID().uuidString
        let livePhotoMovieFilePath = (NSTemporaryDirectory() as NSString).appendingPathComponent((livePhotoMovieFileName as NSString).appendingPathExtension("mov")!)
        photoSettings.livePhotoMovieFileURL = URL(fileURLWithPath: livePhotoMovieFilePath)
      }
      
      let photoCaptureDelegate = IMUIPhotoCaptureDelegate(with: photoSettings, willCapturePhotoAnimation: {
        DispatchQueue.main.async { [unowned self] in
          self.cameraPreviewView.videoPreviewLayer.opacity = 0
          UIView.animate(withDuration: 0.25) { [unowned self] in
            self.cameraPreviewView.videoPreviewLayer.opacity = 1
          }
        }
      }, capturingLivePhoto: { capturing in
        self.sessionQueue.async { [unowned self] in
          if capturing {
            self.inProgressLivePhotoCapturesCount += 1
          }
          else {
            self.inProgressLivePhotoCapturesCount -= 1
          }
          
          let inProgressLivePhotoCapturesCount = self.inProgressLivePhotoCapturesCount
          DispatchQueue.main.async {
            if inProgressLivePhotoCapturesCount > 0 {
              
            }
            else if inProgressLivePhotoCapturesCount == 0 {
              
            }
              
            else {
              print("Error: In progress live photo capture count is less than 0");
            }
          }
        }
      }, completed: { [unowned self] photoCaptureDelegate in
        self.shootPictureCallback?(photoCaptureDelegate.photoData!)
        self.sessionQueue.async { [unowned self] in
          self.inProgressPhotoCaptureDelegates[photoCaptureDelegate.requestedPhotoSettings.uniqueID] = nil
        }
        }
      )
      
      self.inProgressPhotoCaptureDelegates[photoCaptureDelegate.requestedPhotoSettings.uniqueID] = photoCaptureDelegate
      self.photoOutput?.capturePhoto(with: photoSettings, delegate: photoCaptureDelegate)
    }
  }
  
  private func capturePhotoBefore_iOS8() {
    
    stillImageOutput.outputSettings = [AVVideoCodecKey: AVVideoCodecJPEG]
    
    var videoConnection: AVCaptureConnection? = nil
    for connection in stillImageOutput.connections as! [AVCaptureConnection] {
      for port in connection.inputPorts as! [AVCaptureInput.Port]{
        if port.mediaType == AVMediaType.video {
          videoConnection = connection
          break
        }
      }
      
      if videoConnection != nil { break }
    }
    
    print("about to request a capture from: \(stillImageOutput)")
    
    stillImageOutput.captureStillImageAsynchronously(from: videoConnection!) { (imageSampleBuffer, error) in
      if imageSampleBuffer == nil {
        return
      }
      let exifAttachments = CMGetAttachment(imageSampleBuffer!, kCGImagePropertyExifDictionary, nil)
      
      if (exifAttachments != nil) {
        print("exifAttachments exit")
      } else {
        print("exifAttachments not exit")
      }
      
      let imageData = AVCaptureStillImageOutput.jpegStillImageNSDataRepresentation(imageSampleBuffer!)

      self.shootPictureCallback?(imageData!)
      let image = UIImage(data: imageData!)
      
    }
  }
  
  func getPath() -> String {
    var recorderPath:String? = nil
    let now:Date = Date()
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "yy-MMMM-dd"
    recorderPath = NSTemporaryDirectory()
    dateFormatter.dateFormat = "yyyy-MM-dd-hh-mm-ss"
    recorderPath?.append("\(dateFormatter.string(from: now))-video.mp4")
    return recorderPath!
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


extension IMUICameraView: AVCaptureFileOutputRecordingDelegate {
  func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
    if error == nil {
      if isActivity {
        self.recordVideoCallback?(outputFileURL.path, output.recordedDuration.seconds)
      } else {
        let fileManager = FileManager()
        if fileManager.fileExists(atPath: outputFileURL.path) {
          do {
            try fileManager.removeItem(at: URL(fileURLWithPath: outputFileURL.path))
          } catch {
            print("removefile fail")
          }
        }
      }
    } else {
      print("record video fail")
    }
  }

  func capture(_ captureOutput: AVCaptureFileOutput!, didFinishRecordingToOutputFileAt outputFileURL: URL!, fromConnections connections: [Any]!, error: Error!) {
    if error == nil {
      if isActivity {
        self.recordVideoCallback?(outputFileURL.path, captureOutput.recordedDuration.seconds)
      } else {
        let fileManager = FileManager()
        if fileManager.fileExists(atPath: outputFileURL.path) {
          do {
            try fileManager.removeItem(at: URL(fileURLWithPath: outputFileURL.path))
          } catch {
            print("removefile fail")
          }
        }
      }
    } else {
      print("record video fail")
    }
    
  }
  
  func capture(_ captureOutput: AVCaptureFileOutput!, didStartRecordingToOutputFileAt fileURL: URL!, fromConnections connections: [Any]!) {
    
  }


}
