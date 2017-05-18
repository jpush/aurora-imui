//
//  IMUIInputViewDelegate.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit
import Photos

/**
 *  The `IMUIInputViewDelegate` protocol defines the even callback delegate
 */
public protocol IMUIInputViewDelegate: NSObjectProtocol {
  
  /**
   *  Tells the delegate that user tap send button and text input string is not empty
   */
  func sendTextMessage(_ messageText: String)
  
  /**
   *  Tells the delegate that IMUIInputView will switch to recording voice mode
   */
  func switchToMicrophoneMode(recordVoiceBtn: UIButton)
  
  /**
   *  Tells the delegate that start record voice
   */
  func startRecordVoice()
  
  /**
   *  Tells the delegate when finish record voice
   */
  func finishRecordVoice(_ voicePath: String, durationTime: Double)
  
  /**
   *  Tells the delegate that user cancel record
   */
  func cancelRecordVoice()
  
  /**
   *  Tells the delegate that IMUIInputView will switch to gallery
   */
  func switchToGalleryMode(photoBtn: UIButton)
  
  /**
   *  Tells the delegate that user did selected Photo in gallery
   */
  func didSeletedGallery(AssetArr: [PHAsset])
  
  /**
   *  Tells the delegate that IMUIInputView will switch to camera mode
   */
  func switchToCameraMode(cameraBtn: UIButton)
  
  /**
   *  Tells the delegate that user did shoot picture in camera mode
   */
  func didShootPicture(picture: Data)
  
  /**
   *  Tells the delegate when starting record video
   */
  func startRecordVideo()
  
  /**
   *  Tells the delegate when user did shoot video in camera mode
   */
  func finishRecordVideo(videoPath: String, durationTime: Double)
}


public extension IMUIInputViewDelegate {
  func sendTextMessage(_ messageText: String) {}

  func switchToMicrophoneMode(recordVoiceBtn: UIButton) {}
  
  func startRecordVoice() {}
  
  func finishRecordVoice(_ voicePath: String, durationTime: Double) {}
  
  func cancelRecordVoice() {}
  
  func switchToGalleryMode(photoBtn: UIButton) {}
  
  func didSeletedGallery(AssetArr: [PHAsset]) {}
  
  func switchToCameraMode(cameraBtn: UIButton) {}
  
  func didShootPicture(picture: Data) {}
  
  func startRecordVideo() {}
  
  func finishRecordVideo(videoPath: String, durationTime: Double) {}
}
