//
//  IMUIMessageBubbleView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

enum IMUIMessageBubbleType {
  case text
  case image
  case video
  case voice
  case location
}

class IMUIMessageBubbleView: UIView {

  var bubbleImageView: UIImageView
  
  var image: UIImage? {
    set {
      self.bubbleImageView.image = newValue
    }
    
    get {
      return self.bubbleImageView.image
    }
  }
  
  override init(frame: CGRect) {
    bubbleImageView = UIImageView()
    super.init(frame: frame)
    
    self.addSubview(bubbleImageView)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }

  override func layoutSubviews() {
    super.layoutSubviews()
    self.bubbleImageView.frame = self.bounds
  }
  
  func setupBubbleImage(isOutgoing: Bool) {
    var bubbleImg: UIImage?
    if isOutgoing {
      bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
      self.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    } else {
      bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
      self.backgroundColor = UIColor.init(netHex: 0x2977FA)
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
    }
    
    bubbleImageView.image = bubbleImg
    self.layer.mask = bubbleImageView.layer
  }
  
  
}
