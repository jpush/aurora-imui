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
  
  init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: MyUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, text: String, mediaPath: String, layout: IMUIMessageCellLayoutProtocal?) {
    
    self.myTextMessage = text
    self.mediaPath = mediaPath
    
    super.init(msgId: msgId, messageStatus: messageStatus, fromUser: fromUser, isOutGoing: isOutGoing, time: "", status: status, type: type, cellLayout: layout)
  }
  
  convenience init(text: String, isOutGoing: Bool) {

    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: MyMessageModel.calculateTextContentSize(text: text))
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .failed, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .text, text: text, mediaPath: "", layout:  myLayout)
  }

  convenience init(voicePath: String, isOutGoing: Bool) {
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .voice, text: "", mediaPath: voicePath, layout:  nil)
  }
  
  convenience init(imagePath: String, isOutGoing: Bool) {
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .image, text: "", mediaPath: imagePath, layout:  nil)
  }
  
  convenience init(videoPath: String, isOutGoing: Bool) {
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .sending, fromUser: MyUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .video, text: "", mediaPath: videoPath, layout:  nil)
  }
  
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
  
  override init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize) {
    
    super.init(isOutGoingMessage: isOutGoingMessage, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleContentSize)
  }
  
  override var bubbleContentInset: UIEdgeInsets {
    
    if isOutGoingMessage {
      return UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 15)
    } else {
      return UIEdgeInsets(top: 10, left: 15, bottom: 10, right: 10)
    }
  }
  
}


