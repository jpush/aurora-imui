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

class IMUIBaseMessageCell: UICollectionViewCell, IMUIMessageCellProtocal {
  var bubbleView: IMUIMessageBubbleView
  lazy var avatarImage = UIImageView()
  lazy var timeLabel = UILabel()
  lazy var nameLabel = UILabel()
  
  weak var delegate: IMUIMessageMessageCollectionViewDelegate?
  var message: IMUIMessageModelProtocol?
  
  override init(frame: CGRect) {

    bubbleView = IMUIMessageBubbleView(frame: CGRect.zero)
    super.init(frame: frame)
    
    self.contentView.addSubview(self.bubbleView)
    self.contentView.addSubview(self.avatarImage)
    self.contentView.addSubview(self.timeLabel)
    self.contentView.addSubview(self.nameLabel)
    
    let bubbleGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapBubbleView))
    bubbleGesture.numberOfTapsRequired = 1
    self.bubbleView.isUserInteractionEnabled = true
    self.bubbleView.addGestureRecognizer(bubbleGesture)
    
    let avatarGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapHeaderImage))
    avatarGesture.numberOfTapsRequired = 1
    avatarImage.isUserInteractionEnabled = true
    avatarImage.addGestureRecognizer(avatarGesture)
    
    self.nameLabel.frame = IMUIMessageCellLayout.nameLabelFrame
    self.setupSubViews()
  }
  
  fileprivate func setupSubViews() {
    timeLabel.textAlignment = .center
    timeLabel.textColor = UIColor.init(netHex: 0x90A6C4)
    timeLabel.font = UIFont.systemFont(ofSize: 10)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func layoutCell(with layout: IMUIMessageCellLayoutProtocal) {
    self.timeLabel.frame = layout.timeLabelFrame
    self.avatarImage.frame = layout.avatarFrame
    self.bubbleView.frame = layout.bubbleFrame
    
  }
  
  func setupData(with message: IMUIMessageModelProtocol) {
    self.avatarImage.image = message.fromUser.Avatar()
    self.bubbleView.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
    self.timeLabel.text = message.timeString
    self.message = message
    
    self.bubbleView.setupBubbleImage(resizeBubbleImage: message.resizableBubbleImage)
  }
  
  func presentCell(with message: IMUIMessageModelProtocol, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    self.layoutCell(with: message.layout)
    self.setupData(with: message)
    self.delegate = delegate
  }
  
  func tapBubbleView() {
    self.delegate?.messageCollectionView(didTapMessageBubbleInCell: self, model: self.message!)
  }
  
  func tapHeaderImage() {
    self.delegate?.messageCollectionView(didTapHeaderImageInCell: self, model: self.message!)
  }
}
