//
//  MessageEventModel.swift
//  sample
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

@objc open class MessageEventModel: NSObject, IMUIMessageProtocol {

  public var msgId: String = ""
  public var eventText: String = ""
  
  var evenSize: CGSize = CGSize.zero
  
  public init(msgId: String, eventText: String) {
    super.init()
    self.msgId = msgId
    self.eventText = eventText
  }
  
  public convenience init(messageDic: NSDictionary) {
    let msgId = messageDic["msgId"]
    let eventText = messageDic["text"]
    
    self.init(msgId: msgId as! String, eventText: eventText as! String)
    self.evenSize = MessageEventModel.calculateTextContentSize(text: eventText as! String)
  }
  
  static func calculateTextContentSize(text: String) -> CGSize {
      var contentSize = text.sizeWithConstrainedWidth(with: MessageEventCollectionViewCell.maxWidth, font: MessageEventCollectionViewCell.eventFont)
      return CGSize(width: MessageEventCollectionViewCell.contentInset.left +
                           MessageEventCollectionViewCell.contentInset.right +
                           contentSize.width
                 , height: MessageEventCollectionViewCell.contentInset.top +
                           MessageEventCollectionViewCell.contentInset.bottom +
                           contentSize.height)
  }
  
  public func cellHeight() -> CGFloat {
    return self.evenSize.height + MessageEventCollectionViewCell.paddingGap * 2.0;
  }
}
