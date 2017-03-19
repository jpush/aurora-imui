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
//  var isOutGoing: Bool
  var bubbleType: IMUIMessageBubbleType?
  
  var bubbleImageView: UIImageView
  
  var imageView: UIImageView
  var textMessageLable: IMUITextView
  var voiceImg: UIImageView
  var isActivity: Bool = false {
    didSet {
      switch bubbleType! {
      case .voice:
        
        break
      default:
        return
      }
    }
  }
  
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
    voiceImg = UIImageView()
    imageView = UIImageView()
    super.init(frame: frame)
    
    self.addSubview(bubbleImageView)
    self.addSubview(textMessageLable)
    self.addSubview(voiceImg)
    self.addSubview(imageView)
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
    self.imageView.frame = self.bounds
    self.setupBubbleImage(isOutgoing: true)
  }
  
  func layoutToText(with text: String, isOutGoing: Bool) {
    self.bubbleType = .text
    
    textMessageLable.text = text
    voiceImg.isHidden = true
    imageView.isHidden = true
    self.setupBubbleImage(isOutgoing: isOutGoing)
  }

  func layoutToVoice(isOutGoing: Bool) {
    self.bubbleType = .voice
    
    voiceImg.isHidden = false
    imageView.isHidden = true
    if isOutGoing {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_3")
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_3")
    }
    
    self.setupBubbleImage(isOutgoing: isOutGoing)
    self.layoutVoiceInBubble(with: self.frame, isOutGoing: isOutGoing)
  }
  
  func layoutImage(image: UIImage, isOutGoing: Bool) {
    self.bubbleType = .image
    
    voiceImg.isHidden = true
    imageView.isHidden = false
    self.setupBubbleImage(isOutgoing: isOutGoing)
    self.imageView.image = image
    print("fad")
  }
  
  func layoutVoiceInBubble(with frame: CGRect, isOutGoing: Bool) {
    if isOutGoing {
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: frame.width - 20, y: frame.height/2)
    } else {
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: frame.width - 20, y: frame.height/2)
    }
    
  }
  
  func setupBubbleImage(isOutgoing: Bool) {
    var bubbleImg: UIImage?
    if isOutgoing {
      bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
    } else {
      bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
    }
    
    bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    bubbleImageView.image = bubbleImg
    self.layer.mask = bubbleImageView.layer
  }
  
  func setText(with text: String, with contentInset: UIEdgeInsets) {
    textMessageLable.text = text
    
  }
  
}
