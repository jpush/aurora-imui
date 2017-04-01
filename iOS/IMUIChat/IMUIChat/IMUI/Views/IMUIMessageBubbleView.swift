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
  var videoView: UIImageView
  
  var videoReader = IMUIVideoFileLoader()
  
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
    videoView = UIImageView()
    super.init(frame: frame)
    
    self.addSubview(bubbleImageView)
    self.addSubview(textMessageLable)
    self.addSubview(voiceImg)
    self.addSubview(imageView)
    self.addSubview(videoView)
    
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
    self.videoView.frame = self.bounds
  }
  
  func layoutToText(with text: String, isOutGoing: Bool) {
    self.bubbleType = .text
    
    voiceImg.isHidden = true
    imageView.isHidden = true
    videoView.isHidden = true
    textMessageLable.isHidden = false
    
    textMessageLable.text = text
    self.setupBubbleImage(isOutgoing: isOutGoing)
    self.videoReader.isNeedToStopVideo = true
  }

  func layoutToVoice(isOutGoing: Bool) {
    self.bubbleType = .voice
    
    voiceImg.isHidden = false
    imageView.isHidden = true
    videoView.isHidden = true
    textMessageLable.isHidden = true
    
    self.videoReader.isNeedToStopVideo = true
    
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
    videoView.isHidden = true
    textMessageLable.isHidden = true
    imageView.isHidden = false
    
    self.videoReader.isNeedToStopVideo = true
    
    self.setupBubbleImage(isOutgoing: isOutGoing)
    self.imageView.image = image
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
  
  func layoutVideo(with videoPath: String, isOutGoing: Bool) {
    voiceImg.isHidden = true
    imageView.isHidden = true
    textMessageLable.isHidden = true
    videoView.isHidden = false
    
    self.setupBubbleImage(isOutgoing: isOutGoing)
    self.videoView.layer.contents = nil
    self.videoReader.loadVideoFile(with: URL(fileURLWithPath: videoPath)) { (videoFrameImage) in
      DispatchQueue.main.async {
        self.videoView.layer.contents = videoFrameImage
      }
    }
  }
  
  func setupBubbleImage(isOutgoing: Bool) {
    var bubbleImg: UIImage?
    if isOutgoing {
      bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
      self.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    } else {
      bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
      self.backgroundColor = UIColor.init(netHex: 0x2977FA)
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
    }
    
    bubbleImageView.image = bubbleImg
    self.layer.mask = bubbleImageView.layer
  }
  
  func setText(with text: String, with contentInset: UIEdgeInsets) {
    textMessageLable.text = text
    
  }
  
}
