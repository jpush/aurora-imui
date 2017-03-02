//
//  IMUIBaseMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIBaseMessageCell: UICollectionViewCell {
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

  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  
  
  
  
}
