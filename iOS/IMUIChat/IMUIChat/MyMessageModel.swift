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
  
  override var videoPath: String! {
    if self.type != .video { return nil }
    
    return mediaPath
  }
  
  init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, text: String, mediaPath: String, layout: IMUIMessageCellLayout?) {
    self.myTextMessage = text
    self.mediaPath = mediaPath
    
    super.init(msgId: msgId, fromUser: fromUser, isOutGoing: isOutGoing, date: date, status: status, type: type, cellLayout: layout)
  }
  
  convenience init(text: String, isOutGoing: Bool) {
    let layout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       bubbleContentSize: MyMessageModel.calculateTextContentSize(text: text),
                                       bubbleContentInset: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10),
                                       isNeedShowTime: false)
    
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .text, text: text, mediaPath: "", layout:  layout)
  }

  convenience init(voicePath: String, isOutGoing: Bool) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .voice, text: "", mediaPath: voicePath, layout:  nil)
  }
  
  convenience init(imagePath: String, isOutGoing: Bool) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .image, text: "", mediaPath: imagePath, layout:  nil)
  }
  
  convenience init(videoPath: String, isOutGoing: Bool) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: isOutGoing, date: Date(), status: .success, type: .video, text: "", mediaPath: videoPath, layout:  nil)
  }
  
  override func textMessage() -> String {
    return self.myTextMessage
  }
  
  override func mediaData() -> Data {
    var mediaData: Data?
    do {
      mediaData = try! Data(contentsOf: URL(fileURLWithPath: self.mediaPath))
    } catch {
      print("load voice data frome path fail")
    }
    return mediaData!
  }
  
  static func calculateTextContentSize(text: String) -> CGSize {
    let textSize  = text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: UIFont.systemFont(ofSize: 18))
    
    return textSize
  }

}


