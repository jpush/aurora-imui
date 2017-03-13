//
//  IMUIRecordVoiceCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIRecordVoiceCell: UICollectionViewCell {

  @IBOutlet weak var recordVoiceBtn: UIButton!
  @IBOutlet weak var timeLable: UILabel!
  @IBOutlet weak var playVoiceBtn: UIButton!
  @IBOutlet weak var cancelVoiceBtn: UIButton!
  
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
    recordHelper.finishRecordingCompletion()
    
    do {
      let voiceData = try! Data(contentsOf: URL(fileURLWithPath: self.recordHelper.recordPath!))
      self.inputViewDelegate?.finishRecordingVoice?(voiceData, durationTime: Double(self.recordHelper.recordDuration!)!)
    } catch {
      print("\(error)")
    }

  }
  
  @IBAction func cancelRecordVoice(_ sender: Any) {
    
  }
  

  func handlePan(recognizer:UIPanGestureRecognizer) {
    let translation = recognizer.translation(in: self.contentView)
    print("x \(translation.x)   y \(translation.y)")
    if abs(translation.x) > 100.0 {
      
    }
    let cancelDistance = self.cancelVoiceBtn.imui_left - self.recordVoiceBtn.imui_right
    let playDistance = self.recordVoiceBtn.imui_left - self.playVoiceBtn.imui_right
    
    
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

