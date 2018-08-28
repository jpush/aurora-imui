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
  static let kMsgTypeImage = "image"
  static let kMsgTypeVideo = "video"
  static let kMsgTypeCustom = "custom"

  static let kMsgKeyMsgId = "msgId"
  static let kMsgKeyFromUser = "fromUser"
  static let kMsgKeyText = "text"
  static let kMsgKeyisOutgoing = "isOutgoing"
  static let kMsgKeyMediaFilePath = "mediaPath"
  static let kMsgKeyImageUrl = "imageUrl"
  static let kMsgKeyDuration = "duration"
  static let kMsgKeyContentSize = "contentSize"
  static let kMsgKeyContent = "content"
  static let kMsgKeyExtras = "extras"
  
  static let kUserKeyUerId = "userId"
  static let kUserKeyDisplayName = "diaplayName"
  static let kUserAvatarPath = "avatarPath"
  
  static let ktimeString = "timeString"
  
  
  open var myTextMessage: String = ""
  
  var mediaPath: String = ""
  var imageUrl: String = ""
  var extras: NSDictionary?
  
  override open func mediaFilePath() -> String {
    return mediaPath
  }

  override open func webImageUrl() -> String {
    return imageUrl
  }
  
  @objc static open var outgoingBubbleImage: UIImage = {
    var bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    return bubbleImg!
  }()
  
  @objc static open var incommingBubbleImage: UIImage = {
    var bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
    return bubbleImg!
  }()
  
  @objc override open var resizableBubbleImage: UIImage {
    if isOutGoing {
      return RCTMessageModel.outgoingBubbleImage
    } else {
      return RCTMessageModel.incommingBubbleImage
    }
  }
  
  @objc public init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: RCTUser, isOutGoing: Bool, time: String, type: String, text: String, mediaPath: String, imageUrl: String, layout: IMUIMessageCellLayoutProtocol, duration: CGFloat, extras: NSDictionary?) {
    
    self.myTextMessage = text
    self.mediaPath = mediaPath
    self.extras = extras
    self.imageUrl = imageUrl
    super.init(msgId: msgId, messageStatus: messageStatus, fromUser: fromUser, isOutGoing: isOutGoing, time: time, type: type, cellLayout: layout, duration: duration)
  }
  
  @objc public convenience init(messageDic: NSDictionary) {
    
    let msgId = messageDic.object(forKey: RCTMessageModel.kMsgKeyMsgId) as! String
    let msgTypeString = messageDic.object(forKey: RCTMessageModel.kMsgKeyMsgType) as? String
    let statusString = messageDic.object(forKey: RCTMessageModel.kMsgKeyStatus) as? String
    let isOutgoing = messageDic.object(forKey: RCTMessageModel.kMsgKeyisOutgoing) as? Bool
    
    var timeString = messageDic.object(forKey: RCTMessageModel.ktimeString) as? String
    let duration = messageDic.object(forKey: RCTMessageModel.kMsgKeyDuration) as? NSNumber
    let durationTime = CGFloat(duration?.floatValue ?? 0.0)
    
    var needShowTime = false
    var timeContentSize: CGSize = CGSize.zero
    if let timeString = timeString {
      if timeString != "" {
        needShowTime = true
        timeContentSize = RCTMessageModel.calculateNameContentSize(text: timeString)
      }
    } else {
      timeString = ""
    }

    let extras = messageDic.object(forKey: RCTMessageModel.kMsgKeyExtras) as? NSDictionary
    if let _ = extras {
      
    }
    
    var mediaPath = messageDic.object(forKey: RCTMessageModel.kMsgKeyMediaFilePath) as? String
    var imgUrl = ""
    if let _ = mediaPath {
      if FileManager.default.fileExists(atPath: mediaPath!) {
      } else {
        imgUrl = mediaPath!
        mediaPath = ""
      }
    } else {
      mediaPath = ""
    }
    
    var text = messageDic.object(forKey: RCTMessageModel.kMsgKeyText) as? String
    if let _ = text {
      
    } else {
      text = ""
    }
    
    var msgType: String?
    // TODO: duration
    let userDic = messageDic.object(forKey: RCTMessageModel.kMsgKeyFromUser) as? NSDictionary
    let user = RCTUser(userDic: userDic!)
    
    var messageLayout: MyMessageCellLayout?
    if let typeString = msgTypeString {
      msgType = typeString
      if typeString == RCTMessageModel.kMsgTypeText {
        
        messageLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing ?? true,
                                               isNeedShowTime: needShowTime,
                                            bubbleContentSize: RCTMessageModel.calculateTextContentSize(text: text!,
                                                                                                  isOutGoing: isOutgoing!),
                                          bubbleContentInsets: UIEdgeInsets.zero,
                                         timeLabelContentSize: timeContentSize,
                                                         type: RCTMessageModel.kMsgTypeText)
      }
      
      if typeString == RCTMessageModel.kMsgTypeImage {
        var imgSize = CGSize(width: 120, height: 160)
        if let img = UIImage(contentsOfFile: mediaPath!) {
          imgSize = RCTMessageModel.converImageSize(with: CGSize(width: img.size.width, height: img.size.height))
        } else {
          imgSize = CGSize(width: 120, height: 160)
        }
        
        messageLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing ?? true,
                                               isNeedShowTime: needShowTime,
                                            bubbleContentSize: imgSize,
                                          bubbleContentInsets: UIEdgeInsets.zero,
                                         timeLabelContentSize: timeContentSize,
                                                         type: RCTMessageModel.kMsgTypeImage)
      }
      
      if typeString == RCTMessageModel.kMsgTypeVoice {
        messageLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing ?? true,
                                               isNeedShowTime: needShowTime,
                                            bubbleContentSize: CGSize(width: 80, height: 37),
                                          bubbleContentInsets: UIEdgeInsets.zero,
                                         timeLabelContentSize: timeContentSize,
                                                         type: RCTMessageModel.kMsgTypeVoice)
      }
      
      if typeString == RCTMessageModel.kMsgTypeVideo {

        messageLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing ?? true,
                                               isNeedShowTime: needShowTime,
                                            bubbleContentSize: CGSize(width: 120, height: 160),
                                          bubbleContentInsets: UIEdgeInsets.zero,
                                         timeLabelContentSize: timeContentSize,
                                                         type: RCTMessageModel.kMsgTypeVideo)
      }
      
      if typeString == RCTMessageModel.kMsgTypeCustom {
        // TODO custom
        text = messageDic.object(forKey: RCTMessageModel.kMsgKeyContent) as? String
        var bubbleContentSize = CGSize.zero
        var contentSize = messageDic.object(forKey: RCTMessageModel.kMsgKeyContentSize) as? NSDictionary
        if let _ = contentSize {
          let contentWidth = contentSize!["width"] as! NSNumber
          let contentHeight = contentSize!["height"] as! NSNumber
          bubbleContentSize = CGSize(width: contentWidth.doubleValue, height: contentHeight.doubleValue)
        } else {
          bubbleContentSize = CGSize.zero
        }
        messageLayout = MyMessageCellLayout(isOutGoingMessage: isOutgoing ?? true,
                                               isNeedShowTime: needShowTime,
                                            bubbleContentSize: bubbleContentSize,
                                          bubbleContentInsets: UIEdgeInsets.zero,
                                         timeLabelContentSize: timeContentSize,
                                                         type: RCTMessageModel.kMsgTypeCustom)
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
    
    self.init(msgId: msgId, messageStatus: msgStatus, fromUser: user, isOutGoing: isOutgoing ?? true, time: timeString!, type: msgType!, text: text!, mediaPath: mediaPath!,imageUrl: imgUrl, layout:  messageLayout!,duration: durationTime, extras: extras)

  }
  
  override open func text() -> String {
    return self.myTextMessage
  }
  
  @objc static func calculateTextContentSize(text: String, isOutGoing: Bool) -> CGSize {
    if isOutGoing {
      return text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth,
                                    font: IMUITextMessageContentView.outGoingTextFont)
    } else {
      return text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth,
                                           font: IMUITextMessageContentView.inComingTextFont)
    }
  }
  
  static func calculateNameContentSize(text: String) -> CGSize {
      return text.sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth,
                                           font: IMUIMessageCellLayout.timeStringFont)
  }
  
  static func converImageSize(with size: CGSize) -> CGSize {
    let maxSide = 160.0
    
    var scale = size.width / size.height
    
    if size.width > size.height {
      scale = scale > 2 ? 2 : scale
      return CGSize(width: CGFloat(maxSide), height: CGFloat(maxSide) / CGFloat(scale))
    } else {
      scale = scale < 0.5 ? 0.5 : scale
      return CGSize(width: CGFloat(maxSide) * CGFloat(scale), height: CGFloat(maxSide))
    }
  }
  
  @objc public var messageDictionary: NSDictionary {
    get {
      
      let messageDic = NSMutableDictionary()
      messageDic.setValue(self.msgId, forKey: RCTMessageModel.kMsgKeyMsgId)
      messageDic.setValue(self.isOutGoing, forKey: RCTMessageModel.kMsgKeyisOutgoing)

      switch self.type {
      case "text":
        messageDic.setValue(RCTMessageModel.kMsgTypeText, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.text(), forKey: RCTMessageModel.kMsgKeyText)
        break
      case "image":
        messageDic.setValue(RCTMessageModel.kMsgTypeImage, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        break
      case "voice":
        messageDic.setValue(RCTMessageModel.kMsgTypeVoice, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        messageDic.setValue(self.duration, forKey: RCTMessageModel.kMsgKeyDuration)
        break
      case "video":
        messageDic.setValue(RCTMessageModel.kMsgTypeVideo, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.mediaPath, forKey: RCTMessageModel.kMsgKeyMediaFilePath)
        messageDic.setValue(self.duration, forKey: RCTMessageModel.kMsgKeyDuration)
        break
      case "custom":
        messageDic.setValue(RCTMessageModel.kMsgTypeCustom, forKey: RCTMessageModel.kMsgKeyMsgType)
        messageDic.setValue(self.text(), forKey: RCTMessageModel.kMsgKeyContent)
        let contentSize = ["height": self.layout.bubbleContentSize.height,
                           "width": self.layout.bubbleContentSize.width]
        messageDic.setValue(contentSize, forKey: RCTMessageModel.kMsgKeyContentSize)
        break
        
      default:
        break
      }
      
      var msgStatus = ""
      switch self.messageStatus {
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
      
      if let _ = self.extras {
        messageDic.setValue(self.extras, forKey: RCTMessageModel.kMsgKeyExtras)
      }
      
      messageDic.setValue(msgStatus, forKey: "status")
      let userDic = NSMutableDictionary()
      userDic.setValue(self.fromUser.userId(), forKey: "userId")
      userDic.setValue(self.fromUser.displayName(), forKey: "diaplayName")
      let user = self.fromUser as! RCTUser
      userDic.setValue(user.rAvatarFilePath, forKey: "avatarPath")
      
      messageDic.setValue(userDic, forKey: "fromUser")
      messageDic.setValue(self.msgId, forKey: "msgId")
      messageDic.setValue(self.timeString, forKey: "timeString")
      return messageDic
    }
  }
}


//MARK - IMUIMessageCellLayoutProtocal
public class MyMessageCellLayout: IMUIMessageCellLayout {
  @objc open static var outgoingPadding = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 15)
  @objc open static var incommingPadding = UIEdgeInsets(top: 10, left: 15, bottom: 10, right: 10)
  
  var type: String
  
  init(isOutGoingMessage: Bool,
          isNeedShowTime: Bool,
       bubbleContentSize: CGSize,
     bubbleContentInsets: UIEdgeInsets,
    timeLabelContentSize: CGSize,
                    type: String) {
    self.type = type
    super.init(isOutGoingMessage: isOutGoingMessage,
                  isNeedShowTime: isNeedShowTime,
               bubbleContentSize: bubbleContentSize,
             bubbleContentInsets: UIEdgeInsets.zero,
            timeLabelContentSize: timeLabelContentSize)
  }
  
  open override var bubbleContentInset: UIEdgeInsets {
    if type != RCTMessageModel.kMsgTypeText {
      return UIEdgeInsets.zero
    }
    
    if isOutGoingMessage {
      return MyMessageCellLayout.outgoingPadding
    } else {
      return MyMessageCellLayout.incommingPadding
    }
  }
  
  open override var bubbleContentView: IMUIMessageContentViewProtocol {
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
    
    if type == "custom" {
      return IMUICustomMessageContentView()
    }
    
    return IMUIDefaultContentView()
  }
  
  open override var bubbleContentType: String {
    return type
  }
  
}

