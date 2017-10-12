//
//  IMUIImageMessageContentView.swift
//  sample
//
//  Created by oshumini on 2017/6/11.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIImageMessageContentView: UIView, IMUIMessageContentViewProtocol {

  var imageView = UIImageView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.addSubview(imageView)
    imageView.contentMode = .scaleAspectFill
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func layoutContentView(message: IMUIMessageModelProtocol) {
    
    imageView.frame = CGRect(origin: CGPoint.zero, size: message.layout.bubbleContentSize)
    imageView.image = UIImage(contentsOfFile: message.mediaFilePath())
  }
}
