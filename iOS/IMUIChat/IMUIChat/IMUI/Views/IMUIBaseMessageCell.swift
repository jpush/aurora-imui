//
//  IMUIBaseMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


enum IMUIMessageCellType {
  case incoming
  case outgoing
}

protocol IMUIMessageCellProtocal {
  func presentCell(with message: IMUIMessageModel)
}


class IMUIBaseMessageCell: UICollectionViewCell, IMUIMessageCellProtocal {
  var bubbleView: IMUIMessageBubbleView
  var avatarImage: UIImageView
  var timeLable: UILabel
  var nameLable: UILabel
  
  weak var message: IMUIMessageModel?
  
  override init(frame: CGRect) {

    self.bubbleView = IMUIMessageBubbleView()
    self.avatarImage = UIImageView()
    self.timeLable = UILabel()
    self.nameLable = UILabel()
    super.init(frame: frame)
    
    self.contentView.addSubview(self.bubbleView)
    self.contentView.addSubview(self.avatarImage)
    self.contentView.addSubview(self.timeLable)
    self.contentView.addSubview(self.nameLable)
    
    
    self.nameLable.frame = IMUIMessageCellLayout.nameLabelFrame
    
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func layoutCell(with type: IMUIMessageCellType) {
    
  }
  
  func layoutCell(with layout: IMUIMessageCellLayout) {
    self.timeLable.frame = layout.timeLabelFrame
    self.avatarImage.frame = layout.avatarFrame
    self.bubbleView.frame = layout.bubbleFrame
  }
  
  func setupData(with message: IMUIMessageModel) {
    self.avatarImage.image = message.fromUser.Avatar()
    self.avatarImage.backgroundColor = UIColor.red
    self.bubbleView.backgroundColor = UIColor.blue
  }
  
  open func presentCell(with message: IMUIMessageModel) {
    self.layoutCell(with: message.layout)
    self.setupData(with: message)
  }
  
}
