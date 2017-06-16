//
//  MyMessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/5.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class MyMessageModel: IMUIMessageModel {
  
  open var myTextMessage: String = ""
  
  var mediaPath: String = ""
  
  override func mediaFilePath() -> String {
    return mediaPath
  }

  
  override var resizableBubbleImage: UIImage {
    // return defoult message bubble
    return super.resizableBubbleImage
  }
  
  init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: MyUser, isOutGoing: Bool, date: Date, type: String, text: String, mediaPath: String, layout: IMUIMessageCellLayoutProtocol) {
    
    self.myTextMessage = text
    self.mediaPath = mediaPath
    
    super.init(msgId: msgId, messageStatus: messageStatus, fromUser: fromUser, isOutGoing: isOutGoing, time: "", type: type, cellLayout: layout)
  }
  
  convenience init(text: String, isOutGoing: Bool) {

    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: MyMessageModel.calculateTextContentSize(text: text), bubbleContentInsets: UIEdgeInsets.zero, type: "text")
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .failed, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), type: "text", text: text, mediaPath: "", layout:  myLayout)
  }

  convenience init(voicePath: String, isOutGoing: Bool) {
    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: CGSize(width: 80, height: 37), bubbleContentInsets: UIEdgeInsets.zero, type: "voice")
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), type: "voice", text: "", mediaPath: voicePath, layout:  myLayout)
  }
  
  convenience init(imagePath: String, isOutGoing: Bool) {
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: CGSize(width: 120, height: 160), bubbleContentInsets: UIEdgeInsets.zero, type: "image")
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), type: "image", text: "", mediaPath: imagePath, layout:  myLayout)
  }
  
  convenience init(videoPath: String, isOutGoing: Bool) {
    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: CGSize(width: 120, height: 160), bubbleContentInsets: UIEdgeInsets.zero, type: "video")
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), type: "video", text: "", mediaPath: videoPath, layout:  myLayout)
  }
  
//  convenience init(eventText: String) {
//    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
//    self.init(msgId: msgId, messageStatus: .success, fromUser: MyUser(), isOutGoing: true, date: Date(), type: "event", text: "", mediaPath: "", layout: MyMessageCellLayout())
//  }
  
  override func text() -> String {
    return self.myTextMessage
  }
  
  static func calculateTextContentSize(text: String) -> CGSize {
    let textSize  = text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: UIFont.systemFont(ofSize: 18))
    
    return textSize
  }
}


//MARK - IMUIMessageCellLayoutProtocal
class MyMessageCellLayout: IMUIMessageCellLayout {

  var type: String
  
  init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize, bubbleContentInsets: UIEdgeInsets, type: String) {
    self.type = type
    super.init(isOutGoingMessage: isOutGoingMessage, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleContentSize, bubbleContentInsets: UIEdgeInsets.zero)
  }
  
  override var bubbleContentInset: UIEdgeInsets {
    if type != "text" { return UIEdgeInsets.zero }
    if isOutGoingMessage {
      return UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 15)
    } else {
      return UIEdgeInsets(top: 10, left: 15, bottom: 10, right: 10)
    }
  }
  
  override var bubbleContentView: IMUIMessageContentViewProtocol {
    if type == "text" {
      return IMUITextMessageContentView()
    }

    if type == "image" {
      return IMUIImageMessageContentView()
    }

    if type == "voice" {
      return IMUIVoiceMessageContentView()
    }

    if type == "video" {
      return IMUIVideoMessageContentView()
    }
    
    
    return IMUIDefaultContentView()
  }
  
  override var bubbleContentType: String {
    return type
  }
  
}


