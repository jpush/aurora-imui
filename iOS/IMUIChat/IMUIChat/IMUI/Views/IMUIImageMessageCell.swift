//
//  IMUIImageMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIImageMessageCell: IMUIBaseMessageCell {

  var imageView = UIImageView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    bubbleView.addSubview(imageView)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
  }

  override func presentCell(with message: IMUIMessageModel) {
    super.presentCell(with: message)
    
    let layout = message.layout as! IMUIMessageCellLayout
    self.imageView.frame = layout.bubbleContentFrame
    self.layoutImage(image: UIImage(data: message.mediaData())!)
  }
  
  func layoutImage(image: UIImage) {
    self.imageView.image = image
  }
}

