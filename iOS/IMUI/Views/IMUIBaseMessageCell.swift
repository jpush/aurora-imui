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
  func presentCell(with message: IMUIMessageModel, delegate: IMUIMessageMessageCollectionViewDelegate?)
  func didDisAppearCell()
}

extension IMUIMessageCellProtocal {
  func didDisAppearCell() {}
}

class IMUIBaseMessageCell: UICollectionViewCell, IMUIMessageCellProtocal {
  var bubbleView: IMUIMessageBubbleView
  var avatarImage: UIImageView
  var timeLable: UILabel
  var nameLable: UILabel
  
  weak var delegate: IMUIMessageMessageCollectionViewDelegate?
  weak var message: IMUIMessageModel?
  
  override init(frame: CGRect) {

    bubbleView = IMUIMessageBubbleView(frame: CGRect.zero)
    avatarImage = UIImageView()
    timeLable = UILabel()
    nameLable = UILabel()
    super.init(frame: frame)
    
    self.contentView.addSubview(self.bubbleView)
    self.contentView.addSubview(self.avatarImage)
    self.contentView.addSubview(self.timeLable)
    self.contentView.addSubview(self.nameLable)
    
    let gesture = UITapGestureRecognizer(target: self, action: #selector(self.tapBubbleView))
    gesture.numberOfTapsRequired = 1
    self.bubbleView.isUserInteractionEnabled = true
    self.bubbleView.addGestureRecognizer(gesture)
    
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
  
  func layoutCell(with layout: IMUIMessageCellLayoutProtocal) {
    self.timeLable.frame = layout.timeLabelFrame
    self.avatarImage.frame = layout.avatarFrame
    self.bubbleView.frame = layout.bubbleFrame
    
  }
  
  func setupData(with message: IMUIMessageModel) {
    self.avatarImage.image = message.fromUser.Avatar()
    self.avatarImage.backgroundColor = UIColor.white
    self.bubbleView.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
    self.timeLable.text = message.date.parseDate
    self.message = message
    
    self.bubbleView.setupBubbleImage(resizeBubbleImage: message.resizableBubbleImage)
  }
  
  func presentCell(with message: IMUIMessageModel, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    self.layoutCell(with: message.layout)
    self.setupData(with: message)
    self.delegate = delegate
  }
  
  func tapBubbleView() {
    self.delegate?.didTapMessageCell(self.message!)
  }
}
