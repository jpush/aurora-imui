//
//  MessageEventCollectionViewCell.swift
//  sample
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

@objc open class MessageEventCollectionViewCell: UICollectionViewCell {
  static var paddingGap:CGFloat = 5.0
  static var eventFont = UIFont.systemFont(ofSize: 12)
  static var eventTextColor = UIColor.white
  static var eventBackgroundColor = UIColor(netHex: 0xCECECE)
  static var maxWidth: CGFloat = 200.0
  static var contentInset: UIEdgeInsets = UIEdgeInsets(top: 6, left: 8, bottom: 6, right: 8)
  
  var eventLabel = IMUITextView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    self.contentView.addSubview(eventLabel)
    eventLabel.backgroundColor = MessageEventCollectionViewCell.eventBackgroundColor
    eventLabel.frame = CGRect(x: 0, y: 0, width: 300, height: 20)
    eventLabel.font = MessageEventCollectionViewCell.eventFont
    eventLabel.numberOfLines = 0
    eventLabel.layer.cornerRadius = 3.0
    eventLabel.layer.masksToBounds = true
    eventLabel.contentInset = MessageEventCollectionViewCell.contentInset
    eventLabel.textColor = MessageEventCollectionViewCell.eventTextColor
    
    eventLabel.textAlignment = .center
  }
  
  open func presentCell(event: MessageEventModel) {
    eventLabel.text = event.eventText
    eventLabel.frame = CGRect(origin: CGPoint.zero, size: event.evenSize)
    eventLabel.center = self.contentView.center
    eventLabel.frame.origin.y = MessageEventCollectionViewCell.paddingGap
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
