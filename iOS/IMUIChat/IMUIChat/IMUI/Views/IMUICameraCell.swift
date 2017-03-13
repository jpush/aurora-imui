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

class IMUICameraCell: UICollectionViewCell {

  @IBOutlet weak var cameraPreviewView: IMUICameraPreviewView!
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  
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

