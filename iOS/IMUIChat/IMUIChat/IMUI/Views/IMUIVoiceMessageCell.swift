//
//  IMUIVoiceMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIVoiceMessageCell: IMUIBaseMessageCell {

  var voiceImg = UIImageView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    bubbleView.addSubview(voiceImg)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func tapBubbleView() {
    super.tapBubbleView()
    
        if bubbleView.isActivity {
          switch message!.type {
          case .voice:
            IMUIAudioPlayerHelper.sharedInstance.stopAudio()
            break
          default:
            break
          }
        } else {
          switch message!.type {
          case .voice:
            IMUIAudioPlayerHelper.sharedInstance.playAudioWithData((message?.mediaData())!,
                                                                   progressCallback: { (currendTime, duration) in
    
                                                                    },
                                                                   finishCallBack: {
                                                                    self.bubbleView.isActivity = false
                                                                  })
            break
          default:
            break
          }
        }
    
        bubbleView.isActivity = !bubbleView.isActivity
  }
  
}


extension IMUIVoiceMessageCell: IMUIMessageCellProtocal {
  open func presentCell(with message: IMUIMessageModel) {
    self.layoutCell(with: message.layout!)
    self.setupData(with: message)
    
    self.layoutToVoice(isOutGoing: message.isOutGoing)
  }
  
  func layoutToVoice(isOutGoing: Bool) {
    
    if isOutGoing {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: bubbleView.frame.width - 20, y: bubbleView.frame.height/2)
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: 20, y: bubbleView.frame.height/2)
    }
  }
}
