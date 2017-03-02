//
//  MessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit



///// 发送息创建时的初始状态
//kJMSGMessageStatusSendDraft = 0,
///// 消息正在发送过程中. UI 一般显示进度条
//kJMSGMessageStatusSending = 1,
///// 媒体类消息文件上传失败
//kJMSGMessageStatusSendUploadFailed = 2,
///// 媒体类消息文件上传成功
//kJMSGMessageStatusSendUploadSucceed = 3,
///// 消息发送失败
//kJMSGMessageStatusSendFailed = 4,
///// 消息发送成功
//kJMSGMessageStatusSendSucceed = 5,
///// 接收中的消息(还在处理)
//kJMSGMessageStatusReceiving = 6,
///// 接收消息时自动下载媒体失败
//kJMSGMessageStatusReceiveDownloadFailed = 7,
///// 接收消息成功
//kJMSGMessageStatusReceiveSucceed = 8,

public enum IMUIMessageType {
  case text
  case image
  case voice
  case video
  case location
  case custom
}

public enum IMUIMessageStatus {
  case failed
  case sending
  case success
}

public enum IMUIMessageReceiveStatus {
  case failed
  case sending
  case success
}


public protocol IMUIMessageModelProtocol {

  
//  func msgId() -> String
//  func senderId() -> String
//  func isIncoming() -> Bool
//  func date() -> Date
//  func status() -> IMUIMessageStatus
//  
//  func messageType() -> IMUIMessageType
  var mediaData: NSData { get }
  var textMessage: String { get }

}

public protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}


open class IMUIMessageModel: IMUIMessageModelProtocol {
  
  public var textMessage: String {
    get {
      return ""
    }
  }

  public var mediaData: NSData {
    get {
     return NSData()
    }
  }
  
  open var msgId: String = ""
  open var fromUser: IMUIUser
  open var isIncoming: Bool = false
  open var date: Date
  open var isNeedShowTime: Bool = false
  open var status: IMUIMessageStatus
  open var type: IMUIMessageType
  
  
  
  public init(msgId: String, fromUser: IMUIUser, isIncoming: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isIncoming = isIncoming
    self.date = date
    self.status = status
    self.type = type
  }
  
}
