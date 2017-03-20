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
  var voicePath: String = ""
  var imagePath: String? = nil
  
  init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, text: String, voicePath: String, imagePath: String) {
    self.myTextMessage = text
    self.voicePath = voicePath
    self.imagePath = imagePath
    super.init(msgId: msgId, fromUser: fromUser, isOutGoing: isOutGoing, date: date, status: status, type: type)
  }
  
  convenience init(text: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .text, text: text, voicePath: "", imagePath: "")
  }

  convenience init(voicePath: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .voice, text: "", voicePath: voicePath, imagePath: "")
  }
  
  convenience init(imagePath: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .image, text: "", voicePath: "", imagePath: imagePath)
  }
  
  override func textMessage() -> String {
    return self.myTextMessage
  }
  
  override func mediaData() -> Data {
    var mediaData: Data?
    
    switch type {
    case .image:
      do {
        mediaData = try! Data(contentsOf: URL(fileURLWithPath: self.imagePath!))
      } catch {
        print("cannot load image file")
      }
      
      break
    case .voice:
      do {
        let voiceData = try! Data(contentsOf: URL(fileURLWithPath: self.voicePath))
        mediaData = voiceData
      } catch {
        print("load voice data frome path fail")
      }
      break
    case .video:
      break
      
    default:
      break
    }
    
    return mediaData!
  }
  
}
