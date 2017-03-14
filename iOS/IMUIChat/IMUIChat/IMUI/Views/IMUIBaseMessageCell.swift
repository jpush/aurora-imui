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

    bubbleView = IMUIMessageBubbleView()
    avatarImage = UIImageView()
    timeLable = UILabel()
    nameLable = UILabel()
    super.init(frame: frame)
    
    self.contentView.addSubview(self.bubbleView)
    self.contentView.addSubview(self.avatarImage)
    self.contentView.addSubview(self.timeLable)
    self.contentView.addSubview(self.nameLable)
        
    self.nameLable.frame = IMUIMessageCellLayout.nameLabelFrame
    self.setupSubViews()
  }
  
  fileprivate func setupSubViews() {
    timeLable.textAlignment = .center
    timeLable.textColor = UIColor.init(netHex: 0x90A6C4)
    timeLable.font = UIFont.systemFont(ofSize: 10)
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
    self.avatarImage.backgroundColor = UIColor.white
    self.bubbleView.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
    self.timeLable.text = message.date.parseDate
    
    switch message.type {
    case .text:
      bubbleView.layoutToText(with: message.textMessage(), isOutGoing: message.isOutGoing)
      break
    case .voice:
      bubbleView.layoutToVoice(isOutGoing: message.isOutGoing)
      break
    case .image:
      bubbleView.layoutImage(image: UIImage(data: message.mediaData())!, isOutGoing: message.isOutGoing)
      break
    case .location:
      break
    case .custom:
      break
    default:
      break
    }
  }
  
  open func presentCell(with message: IMUIMessageModel) {
    self.layoutCell(with: message.layout)
    self.setupData(with: message)
  }
  
}
