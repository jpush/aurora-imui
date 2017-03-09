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
  var textMessageLable: IMUITextView
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
    textMessageLable = IMUITextView()
    
    super.init(frame: frame)
    
    self.addSubview(bubbleImageView)
    self.addSubview(textMessageLable)
    self.backgroundColor = UIColor.red
    textMessageLable.numberOfLines = 0
    textMessageLable.contentInset = IMUIMessageCellLayout.contentInset
    textMessageLable.textColor = UIColor.white
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
    
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    self.textMessageLable.frame = self.bounds
    self.bubbleImageView.frame = self.bounds
    
    var bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    bubbleImageView.image = bubbleImg
    self.layer.mask = bubbleImageView.layer
    
  }
  
  func setupText(with text: String) {
    textMessageLable.text = text
  }

  func setText(with text: String, with contentInset: UIEdgeInsets) {
    textMessageLable.text = text
    
  }
}
