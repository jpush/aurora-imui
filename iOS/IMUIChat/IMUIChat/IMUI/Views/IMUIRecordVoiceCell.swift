//
//  IMUIRecordVoiceCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


class IMUIRecordVoiceCell: UICollectionViewCell {
  static var buttonNormalWith: CGFloat = 46.0
  
  @IBOutlet weak var recordVoiceBtn: UIButton!
  @IBOutlet weak var timeLable: UILabel!
  @IBOutlet weak var playVoiceBtn: UIButton!
  @IBOutlet weak var cancelVoiceBtn: UIButton!
  
  @IBOutlet weak var playVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var playVoiceBtnWidth: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnWidth: NSLayoutConstraint!
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  
  lazy var recordHelper = IMUIRecordVoiceHelper()
  
  override func awakeFromNib() {
    super.awakeFromNib()
    
    self.playVoiceBtn.layer.cornerRadius = self.playVoiceBtn.imui_width / 2
    self.playVoiceBtn.layer.borderColor = UIColor.gray.cgColor
    self.playVoiceBtn.layer.masksToBounds = true
    self.playVoiceBtn.layer.borderWidth = 0.5
    self.playVoiceBtn.isHidden = true
    
    self.cancelVoiceBtn.layer.cornerRadius = self.cancelVoiceBtn.imui_width / 2
    self.cancelVoiceBtn.layer.borderColor = UIColor.gray.cgColor
    self.cancelVoiceBtn.layer.borderWidth = 0.5
    self.cancelVoiceBtn.layer.masksToBounds = true
    self.cancelVoiceBtn.isHidden = true
    
    let gestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(handlePan))
    self.recordVoiceBtn.addGestureRecognizer(gestureRecognizer)
    
  }

  
  @IBAction func startRecordVoice(_ sender: Any) {
    self.playVoiceBtn.isHidden = false
    self.cancelVoiceBtn.isHidden = false
    
    recordHelper.startRecordingWithPath(self.getRecorderPath(),
                                        startRecordCompleted: { 
                                          print("start!")
                                        },
                                        finishCallback: { 
                                          print("finish!")
                                        },
                                        cancelCallback: {
                                          print("cancel!")
                                        })
  }
  
  @IBAction func finishRecordVoice(_ sender: Any) {
    self.playVoiceBtn.isHidden = true
    self.cancelVoiceBtn.isHidden = true
    self.resetButtonSize()
    
    recordHelper.finishRecordingCompletion()
    
    do {
      let voiceData = try! Data(contentsOf: URL(fileURLWithPath: self.recordHelper.recordPath!))
      self.inputViewDelegate?.finishRecordingVoice?(voiceData, durationTime: Double(self.recordHelper.recordDuration!)!)
    } catch {
      print("\(error)")
    }

  }
  
  func resetButtonSize() {
    self.playVoiceBtnWidth.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.playVoiceBtnHeight.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.playVoiceBtn.layer.cornerRadius = IMUIRecordVoiceCell.buttonNormalWith/2
    
    self.cancelVoiceBtnHeight.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.cancelVoiceBtnWidth.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.cancelVoiceBtn.layer.cornerRadius = IMUIRecordVoiceCell.buttonNormalWith/2
  }
  
  @IBAction func cancelRecordVoice(_ sender: Any) {
    self.playVoiceBtn.isHidden = true
    self.cancelVoiceBtn.isHidden = true
    self.resetButtonSize()
  }
  

  func handlePan(recognizer:UIPanGestureRecognizer) {
    let translation = recognizer.translation(in: self.contentView)
    print("x \(translation.x)   y \(translation.y)")
    if abs(translation.x) > 10.0 {
      if translation.x > 0 {
        let cancelDistance = abs(self.cancelVoiceBtn.imui_right - self.recordVoiceBtn.imui_right)
        let progress = abs(translation.x) > abs(cancelDistance) ? abs(translation.x) : abs(cancelDistance)
        let sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(progress / cancelDistance) * 15.0
        cancelVoiceBtnWidth.constant = sizeWidth
        cancelVoiceBtnHeight.constant = sizeWidth
        self.cancelVoiceBtn.layer.cornerRadius = sizeWidth/2
      } else {
        let playDistance = self.recordVoiceBtn.imui_left - self.playVoiceBtn.imui_left
        let progress = abs(translation.x) > abs(playDistance) ? abs(translation.x) : abs(playDistance)
        let sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(progress / playDistance) * 15.0
        playVoiceBtnHeight.constant = sizeWidth
        playVoiceBtnWidth.constant = sizeWidth
        self.playVoiceBtn.layer.cornerRadius = sizeWidth/2
      }
    } else {
      self.resetButtonSize()
    }
    
  }
  
  func getRecorderPath() -> String {
    var recorderPath:String? = nil
    let now:Date = Date()
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "yy-MMMM-dd"
    recorderPath = "\(NSHomeDirectory())/Documents/"
    
    dateFormatter.dateFormat = "yyyy-MM-dd-hh-mm-ss"
    recorderPath?.append("\(dateFormatter.string(from: now))-MySound.ilbc")
    return recorderPath!
  }
  
}

