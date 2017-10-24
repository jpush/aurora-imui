//
//  IMUIPhotoCaptureDelegate.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/17.
//  Copyright © 2017年 HXHG. All rights reserved.
//


import AVFoundation
import Photos

@available(iOS 10.0, *)
class IMUIPhotoCaptureDelegate: NSObject, AVCapturePhotoCaptureDelegate {
  private(set) var requestedPhotoSettings: AVCapturePhotoSettings
  
  private let willCapturePhotoAnimation: () -> ()
  
  private let capturingLivePhoto: (Bool) -> ()
  
  private let completed: (IMUIPhotoCaptureDelegate) -> ()
  
  var photoData: Data? = nil
  
  private var livePhotoCompanionMovieURL: URL? = nil
  
  init(with requestedPhotoSettings: AVCapturePhotoSettings, willCapturePhotoAnimation: @escaping () -> (), capturingLivePhoto: @escaping (Bool) -> (), completed: @escaping (IMUIPhotoCaptureDelegate) -> ()) {
    self.requestedPhotoSettings = requestedPhotoSettings
    self.willCapturePhotoAnimation = willCapturePhotoAnimation
    self.capturingLivePhoto = capturingLivePhoto
    self.completed = completed
  }
  
  private func didFinish() {
    if let livePhotoCompanionMoviePath = livePhotoCompanionMovieURL?.path {
      if FileManager.default.fileExists(atPath: livePhotoCompanionMoviePath) {
        do {
          try FileManager.default.removeItem(atPath: livePhotoCompanionMoviePath)
        }
        catch {
          print("Could not remove file at url: \(livePhotoCompanionMoviePath)")
        }
      }
    }
    
    completed(self)
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, willBeginCaptureFor resolvedSettings: AVCaptureResolvedPhotoSettings) {
    if resolvedSettings.livePhotoMovieDimensions.width > 0 && resolvedSettings.livePhotoMovieDimensions.height > 0 {
      capturingLivePhoto(true)
    }
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, willCapturePhotoFor resolvedSettings: AVCaptureResolvedPhotoSettings) {
    willCapturePhotoAnimation()
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, didFinishProcessingPhoto photoSampleBuffer: CMSampleBuffer?, previewPhoto previewPhotoSampleBuffer: CMSampleBuffer?, resolvedSettings: AVCaptureResolvedPhotoSettings, bracketSettings: AVCaptureBracketedStillImageSettings?, error: Error?) {
    if let photoSampleBuffer = photoSampleBuffer {
      photoData = AVCapturePhotoOutput.jpegPhotoDataRepresentation(forJPEGSampleBuffer: photoSampleBuffer, previewPhotoSampleBuffer: previewPhotoSampleBuffer)
    }
    else {
      print("Error capturing photo: \(String(describing: error))")
      return
    }
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, didFinishRecordingLivePhotoMovieForEventualFileAt outputFileURL: URL, resolvedSettings: AVCaptureResolvedPhotoSettings) {
    capturingLivePhoto(false)
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, didFinishProcessingLivePhotoToMovieFileAt outputFileURL: URL, duration: CMTime, photoDisplayTime: CMTime, resolvedSettings: AVCaptureResolvedPhotoSettings, error: Error?) {
    if let _ = error {
      print("Error processing live photo companion movie: \(String(describing: error))")
      return
    }
    
    livePhotoCompanionMovieURL = outputFileURL
  }
  
  func photoOutput(_ captureOutput: AVCapturePhotoOutput, didFinishCaptureFor resolvedSettings: AVCaptureResolvedPhotoSettings, error: Error?) {
    if let error = error {
      print("Error capturing photo: \(error)")
      didFinish()
      return
    }
    
    guard let photoData = photoData else {
      print("No photo data resource")
      didFinish()
      return
    }
    
    PHPhotoLibrary.requestAuthorization { [unowned self] status in
      if status == .authorized {
        PHPhotoLibrary.shared().performChanges({ [unowned self] in
          let creationRequest = PHAssetCreationRequest.forAsset()
          creationRequest.addResource(with: .photo, data: photoData, options: nil)
          
          if let livePhotoCompanionMovieURL = self.livePhotoCompanionMovieURL {
              let livePhotoCompanionMovieFileResourceOptions = PHAssetResourceCreationOptions()
              livePhotoCompanionMovieFileResourceOptions.shouldMoveFile = true
              creationRequest.addResource(with: .pairedVideo, fileURL: livePhotoCompanionMovieURL, options: livePhotoCompanionMovieFileResourceOptions)
          }
          
          }, completionHandler: { [unowned self] success, error in
            if let error = error {
              print("Error occurered while saving photo to photo library: \(error)")
            }
            
            self.didFinish()
          }
        )
      }
      else {
        self.didFinish()
      }
    }
  }
}

