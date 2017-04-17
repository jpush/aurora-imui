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
   *  start record voice
   */
  func startRecordingVoice()
  
  /**
   *  finish record voice
   */
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
  
  /**
   *  cancel record
   */
  func cancelRecordingVoice()
  
  /**
   *  IMUIInputView will switch to gallery
   */
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
  
  /**
   *  finish select Photo
   */
  func didSelectedPhoto(_ photoArr: [Data])
  
  /**
   *  finish select Photo
   */
  func didSelectedVideo(_ VideoArr: [URL])
  func didSeletedGallery(AssetArr: [PHAsset])
  
  /**
   *  IMUIInputView will switch to camera moe
   */
  func switchIntoCameraMode(cameraBtn: UIButton)
  func didShootPicture(picture: Data)
  func didShootVideo(videoPath: String, durationTime: Double)
}


extension IMUIInputViewDelegate {
  func sendTextMessage(_ messageText: String) {}
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {}
  
  // RecordVoice
  func startRecordingVoice() {}
  func finishRecordingVoice(_ voicePath: String, durationTime: Double) {}
  func cancelRecordingVoice() {}
  
  // Photo
  func switchIntoSelectPhotoMode(photoBtn: UIButton) {}
  func didSelectedPhoto(_ photoArr: [UIImage]) {}
  func didSelectedVideo(_ VideoArr: [URL]) {}
  func didSeletedGallery(AssetArr: [PHAsset]) {}
  
  // Camera
  func switchIntoCameraMode(cameraBtn: UIButton) {}
  func didShootPicture(picture: Data) {}
  func didShootVideo(videoPath: String, durationTime: Double) {}
}
