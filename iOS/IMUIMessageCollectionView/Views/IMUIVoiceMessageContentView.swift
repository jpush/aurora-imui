//
//  IMUIVoiceMessageContentView.swift
//  sample
//
//  Created by oshumini on 2017/6/12.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation

public class IMUIVoiceMessageContentView: UIView, IMUIMessageContentViewProtocol {
  open static var outGoingVoiceDurationColor = UIColor(netHex: 0x7587A8)
  open static var inComingVoiceDurationColor = UIColor(netHex: 0xFFFFFF)
  
  var voiceImg = UIImageView()
  fileprivate var isMediaActivity = false
  var message: IMUIMessageModelProtocol?
  var voiceDuration = UILabel()
  fileprivate var voiceImageIndex = 0
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.addSubview(voiceImg)
    self.addSubview(voiceDuration)
    voiceDuration.textColor = UIColor.white
    voiceDuration.font = UIFont.systemFont(ofSize: 10.0)
    voiceDuration.frame = CGRect(origin: CGPoint.zero, size: CGSize(width: 40, height: 20))
    let gesture = UITapGestureRecognizer(target: self, action: #selector(self.onTapContentView))
    self.isUserInteractionEnabled = true
    self.addGestureRecognizer(gesture)
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func layoutContentView(message: IMUIMessageModelProtocol) {
    self.message = message
    
    let seconds = Int(message.duration)
    if seconds/3600 > 0 {
      voiceDuration.text = "\(seconds/3600):\(String(format: "%02d", (seconds/3600)%60)):\(seconds%60)"
    } else {
      voiceDuration.text = "\(seconds / 60):\(String(format: "%02d", seconds % 60))"
    }

    self.layoutToVoice(isOutGoing: message.isOutGoing)
    
    IMUIAudioPlayerHelper.sharedInstance.renewProgressCallback(message.msgId) { (id,currendTime, duration) in
      if self.message?.msgId == id {
        self.setImage(with: Int(currendTime*4)%3 + 1)
      }
    }
  }
  
  @objc func onTapContentView() {
    if self.isMediaActivity {
      self.isMediaActivity = false
      IMUIAudioPlayerHelper.sharedInstance.stopAudio()
      self.resetVoiceImage()
    } else {
      
      do {
        let voiceData = try Data(contentsOf: URL(fileURLWithPath: (message?.mediaFilePath())!))
        IMUIAudioPlayerHelper
          .sharedInstance
          .playAudioWithData((self.message?.msgId)!,voiceData,
                             progressCallback: { (id,currendTime, duration) in
                                if self.message?.msgId == id {
                                  self.setImage(with: Int(currendTime*4)%3 + 1)
                                }
                              },
                             finishCallBack: { id in
                                if self.message?.msgId == id {
                                  self.isMediaActivity = false
                                  self.resetVoiceImage()
                                }
                              },
                             stopCallBack: {id in
                                if self.message?.msgId == id {
                                  self.isMediaActivity = false
                                  self.resetVoiceImage()
                                }
                              })
      } catch {
        print("load voice file fail")
      }
      
      self.isMediaActivity = true
    }
  }
  
  func resetVoiceImage() {
    if (message?.isOutGoing)! {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_3")
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_3")
    }
  }
  
  func setImage(with index:Int) {
    if (message?.isOutGoing)! {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_\(index)")
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_\(index)")
    }
  }
  
  func layoutToVoice(isOutGoing: Bool) {
    if isOutGoing {
      self.voiceImg.image = UIImage.imuiImage(with: "outgoing_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: frame.width - 20, y: frame.height/2)
      self.voiceDuration.center = CGPoint(x: 30, y: frame.height/2)
      voiceDuration.textAlignment = .left
      voiceDuration.textColor = IMUIVoiceMessageContentView.outGoingVoiceDurationColor
    } else {
      self.voiceImg.image = UIImage.imuiImage(with: "incoming_voice_3")
      self.voiceImg.frame = CGRect(x: 0, y: 0, width: 12, height: 16)
      self.voiceImg.center = CGPoint(x: 20, y: frame.height/2)
      self.voiceDuration.center = CGPoint(x: frame.width - 30, y: frame.height/2)
      voiceDuration.textAlignment = .right
      voiceDuration.textColor = IMUIVoiceMessageContentView.inComingVoiceDurationColor
    }
  }
}
