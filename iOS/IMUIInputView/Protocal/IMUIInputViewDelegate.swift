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
@objc public protocol IMUIInputViewDelegate: NSObjectProtocol {
  
  /**
   *  Tells the delegate that user tap send button and text input string is not empty
   */
  @objc optional func sendTextMessage(_ messageText: String)
  
  /**
   *  Tells the delegate that IMUIInputView will switch to recording voice mode
   */
  @objc optional func switchToMicrophoneMode(recordVoiceBtn: UIButton)
  
  /**
   *  Tells the delegate that start record voice
   */
  @objc optional func startRecordVoice()
  
  /**
   *  Tells the delegate when finish record voice
   */
  @objc optional func finishRecordVoice(_ voicePath: String, durationTime: Double)
  
  /**
   *  Tells the delegate that user cancel record
   */
  @objc optional func cancelRecordVoice()
  
  /**
   *  Tells the delegate that IMUIInputView will switch to gallery
   */
  @objc optional func switchToGalleryMode(photoBtn: UIButton)
  
  /**
   *  Tells the delegate that user did selected Photo in gallery
   */
  @objc optional func didSeletedGallery(AssetArr: [PHAsset])
  
  /**
   *  Tells the delegate that IMUIInputView will switch to camera mode
   */
  @objc optional func switchToCameraMode(cameraBtn: UIButton)
  
  /**
   *  Tells the delegate that user did shoot picture in camera mode
   */
  @objc optional func didShootPicture(picture: Data)
  
  /**
   *  Tells the delegate that IMUIInputView will switch to emoji mode
   */
  @objc optional func switchToEmojiMode(cameraBtn: UIButton)
  
  /**
   *  Tells the delegate that user did seleted emoji
   */
  @objc optional func didSeletedEmoji(emoji: IMUIEmojiModel)
  
  /**
   *  Tells the delegate when starting record video
   */
  @objc optional func startRecordVideo()
  
  /**
   *  Tells the delegate when user did shoot video in camera mode
   */
  @objc optional func finishRecordVideo(videoPath: String, durationTime: Double)
  
  @objc optional func keyBoardWillShow(height: CGFloat, durationTime: Double)
}


//public extension IMUIInputViewDelegate {
//  func sendTextMessage(_ messageText: String) {}
//
//  func switchToMicrophoneMode(recordVoiceBtn: UIButton) {}
//  
//  func startRecordVoice() {}
//  
//  func finishRecordVoice(_ voicePath: String, durationTime: Double) {}
//  
//  func cancelRecordVoice() {}
//  
//  func switchToGalleryMode(photoBtn: UIButton) {}
//  
//  func didSeletedGallery(AssetArr: [PHAsset]) {}
//  
//  func switchToCameraMode(cameraBtn: UIButton) {}
//  
//  func didShootPicture(picture: Data) {}
//  
//  func startRecordVideo() {}
//  
//  func finishRecordVideo(videoPath: String, durationTime: Double) {}
//}
