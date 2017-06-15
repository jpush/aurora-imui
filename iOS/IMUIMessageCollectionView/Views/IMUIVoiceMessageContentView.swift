//
//  IMUIVoiceMessageContentView.swift
//  sample
//
//  Created by oshumini on 2017/6/12.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIVoiceMessageContentView: UIView, IMUIMessageContentViewProtocal {

  var voiceImg = UIImageView()
  fileprivate var isMediaActivity = false
  var message: IMUIMessageModelProtocol?
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.addSubview(voiceImg)
    
    let gesture = UITapGestureRecognizer(target: self, action: #selector(self.onTapContentView))
    self.isUserInteractionEnabled = true
    self.addGestureRecognizer(gesture)
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func layoutContentView(message: IMUIMessageModelProtocol) {
    self.message = message
    self.layoutToVoice(isOutGoing: message.isOutGoing)
    
  }
  
  func onTapContentView() {
    if !isMediaActivity {
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
  
  func layoutToVoice(isOutGoing: Bool) {
    if isOutGoing {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: frame.width - 20, y: frame.height/2)
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: 20, y: frame.height/2)
    }
  }
}
