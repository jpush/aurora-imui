//
//  IMUIVoiceMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIVoiceMessageCell: IMUIBaseMessageCell {

  fileprivate var voiceImg = UIImageView()
  fileprivate var isMediaActivity = false {
    didSet {
      
    }
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    bubbleView.addSubview(voiceImg)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func tapBubbleView() {
    super.tapBubbleView()
    
        if isMediaActivity {
          do {
            let voiceData = try Data(contentsOf: URL(fileURLWithPath: (message?.mediaFilePath())!))
            IMUIAudioPlayerHelper.sharedInstance.playAudioWithData(voiceData,
                                                                   progressCallback: { (currendTime, duration) in
                                                                    
            },
                                                                   finishCallBack: {
                                                                    self.isMediaActivity = false
            })
          } catch {
            print("load voice file fail")
          }
          
        } else {
          IMUIAudioPlayerHelper.sharedInstance.stopAudio()

        }
    
    
        isMediaActivity = !isMediaActivity
  }
  
  override func presentCell(with message: IMUIMessageModelProtocol, viewCache: IMUIReuseViewCache, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    super.presentCell(with: message, viewCache: viewCache, delegate: delegate)
    self.isMediaActivity = true // TODO: add playRecording
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
