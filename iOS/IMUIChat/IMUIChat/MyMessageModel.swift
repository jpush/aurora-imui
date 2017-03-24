//
//  MyMessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/5.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit



//extension MyMessageModel {
//  func textMessage() -> String {
//    return self.myTextMessage
//  }
//  
//  func mediaData() -> Data {
//    return Data()
//  }
//}

class MyMessageModel: IMUIMessageModel {
  open var myTextMessage: String = ""
  var mediaPath: String = ""
  
  override var videoPath: String! {
    if self.type != .video { return nil }
    
    return mediaPath
  }
  init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, text: String, mediaPath: String) {
    self.myTextMessage = text
    self.mediaPath = mediaPath
    
    super.init(msgId: msgId, fromUser: fromUser, isOutGoing: isOutGoing, date: date, status: status, type: type)
  }
  
  convenience init(text: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .text, text: text, mediaPath: "")
  }

  convenience init(voicePath: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .voice, text: "", mediaPath: voicePath)
  }
  
  convenience init(imagePath: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .image, text: "", mediaPath: imagePath)
  }
  
  convenience init(videoPath: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .video, text: "", mediaPath: videoPath)
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
  
}
