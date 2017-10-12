//
//  IMUIMessageStatusView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIMessageDefaultStatusView: UIButton, IMUIMessageStatusViewProtocol {

  public func layoutMediaDownloading() {
    
  }
  
  public func layoutMediaDownloadFail() {
  
  }
  
  public var statusViewID: String { return "" }
  
  var activityIndicator = UIActivityIndicatorView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    activityIndicator.hidesWhenStopped = true
    self.addSubview(activityIndicator)
    
    self.setImage(UIImage.imuiImage(with: "message_status_fail"), for: .selected)
  }
  
  override public var frame: CGRect {
    didSet {
      activityIndicator.color = UIColor.black
      activityIndicator.frame = frame
      activityIndicator.center = CGPoint(x: frame.size.width/2, y: frame.size.height/2)
    }
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  
  
  // MARK: - IMUIMessageStatusViewProtocol
  public func layoutFailedStatus() {
    self.isSelected = true
    activityIndicator.stopAnimating()
  }
  
  public func layoutSendingStatus() {
    self.isSelected = false
    activityIndicator.startAnimating()
  }
  
  public func layoutSuccessStatus() {
    self.isSelected = false
    activityIndicator.stopAnimating()
  }
}
