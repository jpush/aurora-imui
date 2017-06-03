//
//  IMUITextMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

open class IMUITextMessageCell: IMUIBaseMessageCell {

  open static var outGoingTextColor = UIColor(netHex: 0x7587A8)
  open static var inComingTextColor = UIColor.white
  
  open static var outGoingTextFont = UIFont.systemFont(ofSize: 18)
  open static var inComingTextFont = UIFont.systemFont(ofSize: 18)
  
  var textMessageLable = IMUITextView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.bubbleView.addSubview(textMessageLable)
    textMessageLable.numberOfLines = 0
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override open func layoutSubviews() {
    super.layoutSubviews()
  }
  
  override func presentCell(with message: IMUIMessageModelProtocol, viewCache: IMUIReuseViewCache, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    super.presentCell(with: message, viewCache: viewCache, delegate: delegate)

    let layout = message.layout

    self.textMessageLable.frame = UIEdgeInsetsInsetRect(CGRect(origin: CGPoint.zero, size: layout.bubbleFrame.size), layout.bubbleContentInset)
    self.layoutToText(with: message.text(), isOutGoing: message.isOutGoing)
  }
  
  func layoutToText(with text: String, isOutGoing: Bool) {
    textMessageLable.text = text
    if isOutGoing {
      textMessageLable.textColor = IMUITextMessageCell.outGoingTextColor
      textMessageLable.font = IMUITextMessageCell.outGoingTextFont
    } else {
      textMessageLable.textColor = IMUITextMessageCell.inComingTextColor
      textMessageLable.font = IMUITextMessageCell.inComingTextFont
    }
  }
  
}
