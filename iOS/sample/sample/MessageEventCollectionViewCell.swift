//
//  MessageEventCollectionViewCell.swift
//  sample
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class MessageEventCollectionViewCell: UICollectionViewCell {
  var eventLabel = UILabel()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    self.contentView.addSubview(eventLabel)
    eventLabel.frame = CGRect(x: 0, y: 0, width: 300, height: 20)
    eventLabel.textColor = UIColor(netHex: 0x7587A8)
    eventLabel.textAlignment = .center
  }
  
  func presentCell(eventText: String) {
    eventLabel.text = eventText
    eventLabel.center = CGPoint(x: self.contentView.imui_width/2, y: self.contentView.imui_height/2)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
