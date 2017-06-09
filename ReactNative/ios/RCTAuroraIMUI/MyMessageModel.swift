//
//  MyMessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/5.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit



open class RCTMessageModel: IMUIMessageModel {
  static let kMsgKeyStatus = "status"
  static let kMsgStatusSuccess = "send_succeed"
  static let kMsgStatusSending = "send_going"
  static let kMsgStatusFail = "send_failed"
  static let kMsgStatusDownloadFail = "download_failed"
  static let kMsgStatusDownloading = "downloading"
  

  
  static let kMsgKeyMsgType = "msgType"
  static let kMsgTypeText = "text"
  static let kMsgTypeVoice = "voice"
  static let kMsgTypeVideo = "video"
  static let kMsgTypeImage = "image"

  static let kMsgKeyMsgId = "msgId"
  static let kMsgKeyFromUser = "fromUser"
  static let kMsgKeyText = "text"
  static let kMsgKeyisOutgoing = "isOutgoing"
  static let kMsgKeyMediaFilePath = "mediaPath"
  static let kMsgKeyDuration = "duration"
  static let kUserKeyUerId = "userId"
  static let kUserKeyDisplayName = "diaplayName"
  static let kUserAvatarPath = "avatarPath"
  
  static let ktimeString = "timeString"
  
  
  open var myTextMessage: String = ""
  
  var mediaPath: String = ""

  
  override open func mediaFilePath() -> String {
    return mediaPath
  }

  static open var outgoingBubbleImage: UIImage = {
    var bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    return bubbleImg!
  }()
  
  static open var incommingBubbleImage: UIImage = {
    var bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
    return bubbleImg!
  }()
  
  override open var resizableBubbleImage: UIImage {
    // return defoult message bubble
    if isOutGoing {
      return RCTMessageModel.outgoingBubbleImage
    } else {
      return RCTMessageModel.incommingBubbleImage
    }
  }
  
  public init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: RCTUser, isOutGoing: Bool, time: String, status: IMUIMessageStatus, type: IMUIMessageType, text: String, mediaPath: String, layout: IMUIMessageCellLayoutProtocal?) {
    
    self.myTextMessage = text
    self.mediaPath = mediaPath
    
    super.init(msgId: msgId, messageStatus: messageStatus, fromUser: fromUser, isOutGoing: isOutGoing, time: time, status: status, type: type, cellLayout: layout)
  }
  
  public convenience init(messageDic: NSDictionary) {
    
    let msgId = messageDic.object(forKey: RCTMessageModel.kMsgKeyMsgId) as! String
    let msgTypeString = messageDic.object(forKey: RCTMessageModel.kMsgKeyMsgType) as? String
    let statusString = messageDic.object(forKey: RCTMessageModel.kMsgKeyStatus) as? String
    let isOutgoing = messageDic.object(forKey: RCTMessageModel.kMsgKeyisOutgoing) as? Bool
    
    let timeString = messageDic.object(forKey: RCTMessageModel.ktimeString) as? String
    var needShowTime = false
    if let timeString = timeString {
      if timeString != "" {
        needShowTime = true
      }
    }

    var mediaPath = messageDic.object(forKey: RCTMessageModel.kMsgKeyMediaFilePath) as? String
    if let _ = mediaPath {
      
    } else {
      mediaPath = ""
    }
    
    var text = messageDic.object(forKey: RCTMessageModel.kMsgKeyText) as? String
    if let _ = text {
      
    } else {
      text = ""
    }
    
    var msgType: IMUIMessageType?
    // TODO: duration
    let userDic = messageDic.object(forKey: RCTMessageModel.kMsgKeyFromUser) as? NSDictionary
    let user = RCTUser(userDic: userDic!)
    
    var textLayout: MyMessageCellLayout?
    
    if let typeString = msgTypeString {
      if typeString == RCTMessageModel.kMsgTypeText {
        msgType = .text
        textLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing!,
                                       isNeedShowTime: needShowTime,
                                       bubbleContentSize: RCTMessageModel.calculateTextContentSize(text: text!, isOutGoing: isOutgoing!), bubbleContentInsets: UIEdgeInsets.zero)
      }
      
      if typeString == RCTMessageModel.kMsgTypeImage {
        msgType = .image
      }
      
      if typeString == RCTMessageModel.kMsgTypeVoice {
        
        msgType = .voice
      }
      
      if typeString == RCTMessageModel.kMsgTypeVideo {
        msgType = .video
      }
    }
    
    var msgStatus = IMUIMessageStatus.success
    if let statusString = statusString {
      
      if statusString == RCTMessageModel.kMsgStatusSuccess {
        msgStatus = .success
      }
      
      if statusString == RCTMessageModel.kMsgStatusFail {
        msgStatus = .failed
      }
      
      if statusString == RCTMessageModel.kMsgStatusSending {
        msgStatus = .sending
      }
      
      if statusString == RCTMessageModel.kMsgStatusDownloadFail {
        msgStatus = .mediaDownloadFail
      }
      
      if statusString == RCTMessageModel.kMsgStatusDownloading {
        msgStatus = .mediaDownloading
      }
      
    }
    
    self.init(msgId: msgId, messageStatus: msgStatus, fromUser: user, isOutGoing: isOutgoing!, time: timeString!, status: msgStatus, type: msgType!, text: text!, mediaPath: mediaPath!, layout:  textLayout)

  }
  
  convenience init(msgId: String, text: String, isOutGoing: Bool, user: RCTUser) {

    let myLayout = MyMessageCellLayout(isOutGoingMessage: isOutGoing,
                                       isNeedShowTime: false,
                                       bubbleContentSize: RCTMessageModel.calculateTextContentSize(text: text, isOutGoing: isOutGoing), bubbleContentInsets: UIEdgeInsets.zero)
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    self.init(msgId: msgId, messageStatus: .failed, fromUser: user, isOutGoing: isOutGoing, time: "", status: .success, type: .text, text: text, mediaPath: "", layout:  myLayout)
  }

  convenience init(msgId: String, voicePath: String, isOutGoing: Bool, user: RCTUser) {
    self.init(msgId: msgId, messageStatus: .sending, fromUser: user, isOutGoing: isOutGoing, time: "", status: .success, type: .voice, text: "", mediaPath: voicePath, layout:  nil)
  }
  
  convenience init(msgId: String, imagePath: String, isOutGoing: Bool, user: RCTUser) {
    self.init(msgId: msgId, messageStatus: .sending, fromUser: user, isOutGoing: isOutGoing, time: "", status: .success, type: .image, text: "", mediaPath: imagePath, layout:  nil)
  }
  
  convenience init(msgId: String, videoPath: String, isOutGoing: Bool, user: RCTUser) {
    self.init(msgId: msgId, messageStatus: .sending, fromUser: user, isOutGoing: isOutGoing, time: "", status: .success, type: .video, text: "", mediaPath: videoPath, layout:  nil)
  }
  
  override open func text() -> String {
    return self.myTextMessage
  }
  
  static func calculateTextContentSize(text: String, isOutGoing: Bool) -> CGSize {
    if isOutGoing {
      return text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: IMUITextMessageCell.outGoingTextFont)
    } else {
      return text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: IMUITextMessageCell.inComingTextFont)
    }
  }
  
  public var messageDictionary: NSDictionary {
    get {
      
      var messageDic = NSMutableDictionary()
      messageDic.setValue(self.msgId, forKey: RCTMessageModel.kMsgKeyMsgId)
      messageDic.setValue(self.isOutGoing, forKey: RCTMessageModel.kMsgKeyisOutgoing)

      switch self.type {
      case .text:
        messageDic.setValue(RCTMessageModel.kMsgTypeText, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.text(), forKey: RCTMessageModel.kMsgKeyText)
        break
      case .image:
        messageDic.setValue(RCTMessageModel.kMsgTypeImage, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        break
      case .voice:
        messageDic.setValue(RCTMessageModel.kMsgTypeVoice, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        messageDic.setValue(self.duration, forKey: RCTMessageModel.kMsgKeyDuration)
        break
      case .video:
        messageDic.setValue(RCTMessageModel.kMsgTypeVideo, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        messageDic.setValue(self.duration, forKey: RCTMessageModel.kMsgKeyDuration)
        break
      case .custom:
        break
        
      default:
        break
      }
      
      var msgStatus = ""
      switch self.status {
      case .success:
        msgStatus = RCTMessageModel.kMsgStatusSuccess
        break
      case .sending:
        msgStatus = RCTMessageModel.kMsgStatusSending
        break
      case .failed:
        msgStatus = RCTMessageModel.kMsgStatusFail
        break
      case .mediaDownloading:
        msgStatus = RCTMessageModel.kMsgStatusDownloading
        break
      case .mediaDownloadFail:
        msgStatus = RCTMessageModel.kMsgStatusDownloadFail
        break
      }
      
      messageDic.setValue(msgStatus, forKey: "status")
      let userDic = NSMutableDictionary()
      userDic.setValue(self.fromUser.userId(), forKey: "userId")
      userDic.setValue(self.fromUser.displayName(), forKey: "diaplayName")
      let user = self.fromUser as! RCTUser
      userDic.setValue(user.rAvatarFilePath, forKey: "avatarPath")
      
      messageDic.setValue(userDic, forKey: "fromUser")
      messageDic.setValue(self.msgId, forKey: "msgId")
      return messageDic
    }
  }
}


//MARK - IMUIMessageCellLayoutProtocal
open class MyMessageCellLayout: IMUIMessageCellLayout {
  open static var outgoingPadding = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 15)
  open static var incommingPadding = UIEdgeInsets(top: 10, left: 15, bottom: 10, right: 10)
  
  override init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize, bubbleContentInsets: UIEdgeInsets) {
    
    super.init(isOutGoingMessage: isOutGoingMessage, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleContentSize, bubbleContentInsets: bubbleContentInsets)
  }
  
  open override var bubbleContentInset: UIEdgeInsets {
    
    if isOutGoingMessage {
      return MyMessageCellLayout.outgoingPadding
    } else {
      return MyMessageCellLayout.outgoingPadding
    }
  }
  
}


