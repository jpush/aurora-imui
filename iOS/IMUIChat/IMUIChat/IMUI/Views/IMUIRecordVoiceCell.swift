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
  @IBOutlet weak var swtichToPlayModeBtn: UIButton!
  @IBOutlet weak var cancelVoiceBtn: UIButton!
  
  @IBOutlet weak var playVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var playVoiceBtnWidth: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnWidth: NSLayoutConstraint!
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  
  lazy var recordHelper = IMUIRecordVoiceHelper()
  
  override func awakeFromNib() {
    super.awakeFromNib()
    
    self.swtichToPlayModeBtn.layer.borderColor = UIColor.gray.cgColor
    self.swtichToPlayModeBtn.layer.masksToBounds = true
    self.swtichToPlayModeBtn.layer.borderWidth = 0.5
    self.swtichToPlayModeBtn.isHidden = true
    
    self.cancelVoiceBtn.layer.borderColor = UIColor.gray.cgColor
    self.cancelVoiceBtn.layer.borderWidth = 0.5
    self.cancelVoiceBtn.layer.masksToBounds = true
    self.cancelVoiceBtn.isHidden = true
    
    self.resetSubViewsStyle()
    
    let gestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(handlePan))
    self.recordVoiceBtn.addGestureRecognizer(gestureRecognizer)
    
  }

  @IBAction func finishiRecordVoiceCallback(_ sender: Any) {
    self.finishRecordVoice()
  }
  
  @IBAction func startRecordVoice(_ sender: Any) {
    self.swtichToPlayModeBtn.isHidden = false
    self.cancelVoiceBtn.isHidden = false

    recordHelper.startRecordingWithPath(self.getRecorderPath(),
                                        startRecordCompleted: {
      print("start record")
    }, finishCallback: { 
      print("finish record")
    }, cancelCallback: { 
      print("cancel record")
    }) { (timer) in
      
      let seconds = Date.secondsBetween(date1: self.recordHelper.timerFireDate!, date2: Date())
      
      
      self.timeLable.text = "\(String(format: "%02d", seconds/60)):\(String(format: "%02d", seconds%60))"
      
    }
  }
  
  func finishRecordVoice() {
    self.swtichToPlayModeBtn.isHidden = true
    self.cancelVoiceBtn.isHidden = true
    self.resetSubViewsStyle()
    
    recordHelper.finishRecordingCompletion()
    
    do {
      let voiceData = try! Data(contentsOf: URL(fileURLWithPath: self.recordHelper.recordPath!))
      self.inputViewDelegate?.finishRecordingVoice?(voiceData, durationTime: Double(self.recordHelper.recordDuration!)!)
    } catch {
      print("\(error)")
    }

  }
  
  func resetSubViewsStyle() {
    self.playVoiceBtnWidth.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.playVoiceBtnHeight.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.swtichToPlayModeBtn.layer.cornerRadius = IMUIRecordVoiceCell.buttonNormalWith/2
    self.swtichToPlayModeBtn.backgroundColor = UIColor.clear
    self.swtichToPlayModeBtn.isSelected = false
    self.swtichToPlayModeBtn.isHidden = true
    
    self.cancelVoiceBtnHeight.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.cancelVoiceBtnWidth.constant = IMUIRecordVoiceCell.buttonNormalWith
    self.cancelVoiceBtn.layer.cornerRadius = IMUIRecordVoiceCell.buttonNormalWith/2
    self.cancelVoiceBtn.backgroundColor = UIColor.clear
    self.cancelVoiceBtn.isSelected = false
    self.cancelVoiceBtn.isHidden = true
    
    self.timeLable.text = "按住说话"
  }

  func handlePan(recognizer:UIPanGestureRecognizer) {
    let pointInSuperView = recognizer.location(in: self.contentView)
    
    if !self.recordVoiceBtn.frame.contains(pointInSuperView) { // touch move out from recordVoiceBtn
      
      let playDistance =  abs(self.swtichToPlayModeBtn.imui_centerX - self.recordVoiceBtn.imui_left)
      let playProgress = (self.recordVoiceBtn.imui_left - pointInSuperView.x) > 0 ? min((self.recordVoiceBtn.imui_left - pointInSuperView.x), playDistance) : 0
      
      var sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(playProgress / playDistance) * 20.0
      playVoiceBtnHeight.constant = sizeWidth
      playVoiceBtnWidth.constant = sizeWidth
      self.swtichToPlayModeBtn.layer.cornerRadius = sizeWidth / 2
      
      let cancelDistance = abs(self.cancelVoiceBtn.imui_centerX - self.recordVoiceBtn.imui_right)
      let cancelProgress = (pointInSuperView.x - self.recordVoiceBtn.imui_right) > 0 ? min(pointInSuperView.x - self.recordVoiceBtn.imui_right, cancelDistance) : 0
      sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(cancelProgress / cancelDistance) * 20.0
      cancelVoiceBtnWidth.constant = sizeWidth
      cancelVoiceBtnHeight.constant = sizeWidth
      self.cancelVoiceBtn.layer.cornerRadius = sizeWidth/2
    }
    
    // Drag out from recordVoiceBtn to PlayVoiceBtn
    if self.swtichToPlayModeBtn.frame.contains(pointInSuperView) {
      self.setSelectedStatus(button: self.swtichToPlayModeBtn)
    } else {
      self.setDeselectStatus(button: self.swtichToPlayModeBtn)
    }
    
    // Drag out from recordVoiceBtn to cancelVoiceBtn
    if self.cancelVoiceBtn.frame.contains(pointInSuperView) {
      self.setSelectedStatus(button: self.cancelVoiceBtn)
    } else {
      self.setDeselectStatus(button: self.cancelVoiceBtn)
    }
    
    if recognizer.state == .ended {
      
      if self.cancelVoiceBtn.isSelected {
        self.recordHelper.cancelRecording()
        self.resetSubViewsStyle()
        return
      }
      
      if self.swtichToPlayModeBtn.isSelected {
        // TODO: set to play mode
//        do {
//          let voiceData = try! Data(contentsOf: URL(fileURLWithPath: self.recordHelper.recordPath!))
//          IMUIAudioPlayerHelper.sharedInstance.playAudioWithData(voiceData)
//          self.resetSubViewsStyle()
//          return
//        } catch {
//          print("could not load voice file")
//        }
        self.recordHelper.stopRecord()
        return
      }
      
      self.finishRecordVoice()
    }
  }
  
  func setSelectedStatus(button: UIButton) {
    button.backgroundColor = UIColor(netHex: 0x979797)
    button.isSelected = true
  }
  
  func setDeselectStatus(button: UIButton) {
    button.backgroundColor = UIColor.clear
    button.isSelected = false
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

