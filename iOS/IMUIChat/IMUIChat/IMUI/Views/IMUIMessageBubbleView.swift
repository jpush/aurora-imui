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
  case location
}

class IMUIMessageBubbleView: UIView {
  
  var bubbleType: IMUIMessageBubbleType?
  var textMessageLable: UILabel
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
    self.bubbleImageView = UIImageView()
    self.textMessageLable = UILabel()
    
    super.init(frame: frame)
    self.backgroundColor = UIColor.red
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
    
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
//    self.bubbleImageView!.image = self.maskBackgroupImage
//    self.bubbleImageView!.frame = CGRect(x: 0, y: 0, width: self.frame.width, height: self.frame.height)
//    self.layer.mask = self.maskBackgroupView!.layer
    self.bubbleImageView.frame = self.bounds
    self.layer
  }

}
