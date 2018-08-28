//
//  IMUIImageMessageContentView.swift
//  sample
//
//  Created by oshumini on 2017/6/11.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIImageMessageContentView: UIView, IMUIMessageContentViewProtocol {
  var urlString: String?
  weak var task: URLSessionTask?
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
    imageView.image = nil // reset image
    
    imageView.frame = CGRect(origin: CGPoint.zero, size: message.layout.bubbleContentSize)
    imageView.image = UIImage(contentsOfFile: message.mediaFilePath())
    if imageView.image == nil && (message.webImageUrl?() == nil || message.webImageUrl?() == "") {
      self.imageView.image = UIImage.imuiImage(with: "image-broken")
    }
    
    task?.suspend()
    self.urlString = message.webImageUrl?() ?? ""
    task = IMUIWebImageTaskManager.shared.downloadImage(self.urlString!) { (data, precent, urlString, error) in
      if (error != nil) {
        return
      }
      
      if precent == 1.0 && data != nil {
        let image = UIImage(data: data!)
        if self.urlString == urlString {
          self.imageView.image = image
        }
      }
    }
  }
}
