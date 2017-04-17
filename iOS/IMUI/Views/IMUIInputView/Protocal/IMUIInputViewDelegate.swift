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

protocol IMUIInputViewDelegate: NSObjectProtocol {
  
  /**
   *  Send Text
   */
  func sendTextMessage(_ messageText: String)
  
  /**
   *  IMUIInputView will switch to recording voice mode
   */
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
  
  /**
   *  Start record voice
   */
  func startRecordingVoice()
  
  /**
   *  Finish record voice
   */
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
  
  /**
   *  Cancel record
   */
  func cancelRecordingVoice()
  
  /**
   *  IMUIInputView will switch to gallery
   */
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
  
  /**
   *  Did selected Photo in gallery
   */
  func didSeletedGallery(AssetArr: [PHAsset])
  
  /**
   *  IMUIInputView will switch to camera mode
   */
  func switchIntoCameraMode(cameraBtn: UIButton)
  
  /**
   *  Did shoot picture in camera mode
   */
  func didShootPicture(picture: Data)
  
  /**
   *  Did shoot video in camera mode
   */
  func didShootVideo(videoPath: String, durationTime: Double)
}


extension IMUIInputViewDelegate {
  func sendTextMessage(_ messageText: String) {}
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {}
  func startRecordingVoice() {}
  func finishRecordingVoice(_ voicePath: String, durationTime: Double) {}
  func cancelRecordingVoice() {}
  func switchIntoSelectPhotoMode(photoBtn: UIButton) {}
  func didSeletedGallery(AssetArr: [PHAsset]) {}
  func switchIntoCameraMode(cameraBtn: UIButton) {}
  func didShootPicture(picture: Data) {}
  func didShootVideo(videoPath: String, durationTime: Double) {}
}
