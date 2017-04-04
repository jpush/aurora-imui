//
//  IMUITextMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUITextMessageCell: IMUIBaseMessageCell {


  
  var textMessageLable = IMUITextView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.bubbleView.addSubview(textMessageLable)
    textMessageLable.numberOfLines = 0
    textMessageLable.contentInset = IMUIMessageCellLayout.contentInset
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    self.textMessageLable.frame = bubbleView.bounds
  }
  
}

extension IMUITextMessageCell: IMUIMessageCellProtocal {
  open func presentCell(with message: IMUIMessageModel) {
    self.layoutCell(with: message.layout!)
    self.setupData(with: message)
    
    self.layoutToText(with: message.textMessage(), isOutGoing: message.isOutGoing)
  }
  
  func layoutToText(with text: String, isOutGoing: Bool) {
    textMessageLable.text = text
  }
}
