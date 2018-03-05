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
  @objc public static var eventFont: UIFont = UIFont.systemFont(ofSize: 12)
  @objc public static var eventTextColor: UIColor = UIColor.white
  @objc public static var eventBackgroundColor: UIColor = UIColor(netHex: 0xCECECE)
  @objc public static var eventCornerRadius: CGFloat = 3.0//
  @objc public static var eventTextLineHeight: CGFloat = 2.0// TODO:
  
  
  static var maxWidth: CGFloat = 200.0
  @objc public static var contentInset: UIEdgeInsets = UIEdgeInsets(top: 6, left: 8, bottom: 6, right: 8)//
  
  var eventLabel = IMUITextView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    self.contentView.addSubview(eventLabel)
    eventLabel.backgroundColor = MessageEventCollectionViewCell.eventBackgroundColor
    eventLabel.frame = CGRect(x: 0, y: 0, width: 300, height: 20)
    eventLabel.font = MessageEventCollectionViewCell.eventFont
    eventLabel.numberOfLines = 0
    eventLabel.layer.cornerRadius = MessageEventCollectionViewCell.eventCornerRadius
    eventLabel.layer.masksToBounds = true
    eventLabel.contentInset = MessageEventCollectionViewCell.contentInset
    eventLabel.textColor = MessageEventCollectionViewCell.eventTextColor
    
    eventLabel.textAlignment = .center
  }
  
  @objc open func presentCell(event: MessageEventModel) {
    eventLabel.text = event.eventText                                                                                                                                                                                                                                                                                               
    let eventX = (IMUIMessageCellLayout.cellWidth - event.eventSize.width)/2
    let eventY = MessageEventCollectionViewCell.paddingGap
      
    eventLabel.frame = CGRect(x: Int(eventX),
                              y: Int(eventY),
                              width: Int(event.eventSize.width + 1),
                              height: Int(event.eventSize.height + 1))
//    eventLabel.center = self.contentView.center
//    eventLabel.frame.origin.y = MessageEventCollectionViewCell.paddingGap
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
