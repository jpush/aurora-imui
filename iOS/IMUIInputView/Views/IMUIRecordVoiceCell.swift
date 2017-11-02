//
//  IMUIRecordVoiceCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation

class IMUIRecordVoiceCell: UICollectionViewCell, IMUIFeatureCellProtocol {
  static var buttonNormalWith: CGFloat = 46.0
  
  @IBOutlet weak var recordVoiceBtn: UIButton!
  @IBOutlet weak var timeLable: UILabel!
  @IBOutlet weak var swtichToPlayModeBtn: UIButton!
  @IBOutlet weak var cancelVoiceBtn: UIButton!
  @IBOutlet weak var playVoiceBtn: IMUIProgressButton!
  @IBOutlet weak var playCancelBtn: UIButton!
  @IBOutlet weak var sendVoiceBtn: UIButton!
  
  @IBOutlet weak var playVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var playVoiceBtnWidth: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnHeight: NSLayoutConstraint!
  @IBOutlet weak var cancelVoiceBtnWidth: NSLayoutConstraint!
  
  var finishiRecorderCache:(voiceFilePath: String, duration: TimeInterval)?
  var featureDelegate: IMUIFeatureViewDelegate?
  
  func inactivateMedia() {
    recordHelper.stopRecord()
  }
  
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
    
    self.playVoiceBtn.clipColor = UIColor.init(netHex: 0xE1E1E3)
    self.playVoiceBtn.progressColor = UIColor.init(netHex: 0x6BC6E7)
    self.resetSubViewsStyle()
    
    let gestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(handlePan))
    self.recordVoiceBtn.addGestureRecognizer(gestureRecognizer)
    
  }

  // -MARK: RecordVoice
  @IBAction func finishiRecordVoiceCallback(_ sender: Any) {
    self.finishRecordVoice()
  }
  
  @IBAction func startRecordVoice(_ sender: Any) {

    switch AVAudioSession.sharedInstance().recordPermission() {
      case AVAudioSessionRecordPermission.granted:
        self.swtichToPlayModeBtn.isHidden = false
        self.cancelVoiceBtn.isHidden = false
        
        UIView.animate(withDuration: 0.2) {
          self.contentView.layoutIfNeeded()
        }
        
        recordHelper.startRecordingWithPath(self.getRecorderPath(),
                                            startRecordCompleted: {
                                              
        }) { (duration, meter) in
          let seconds = Int(duration)
          self.timeLable.text = "\(String(format: "%02d", seconds / 60)):\(String(format: "%02d", seconds % 60))"
          
        }
      case AVAudioSessionRecordPermission.denied:
        break
      
      case AVAudioSessionRecordPermission.undetermined:
        AVAudioSession.sharedInstance().requestRecordPermission({ (granted) in })
        
        break
      default:
        break
    }
    

  }
  
  func finishRecordVoice() {
    if AVAudioSession.sharedInstance().recordPermission() == .granted {
      self.swtichToPlayModeBtn.isHidden = true
      self.cancelVoiceBtn.isHidden = true
      self.resetSubViewsStyle()
      
      let finishiRecorder = recordHelper.finishRecordingCompletion()
//      self.inputViewDelegate?.finishRecordVoice?(finishiRecorder.voiceFilePath, durationTime: finishiRecorder.duration)
      self.featureDelegate?.didRecordVoice(with: finishiRecorder.voiceFilePath, durationTime: finishiRecorder.duration)
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
    
    self.recordVoiceBtn.isHidden = false
    self.playCancelBtn.isHidden = true
    self.sendVoiceBtn.isHidden = true
    self.playVoiceBtn.isHidden = true
    self.playVoiceBtn.progress = 0
    self.timeLable.text = "按住说话"
  }

  // -MARK: Play Voice
  func switchToPlayVoiceModel() {
    self.recordVoiceBtn.isHidden = true
    self.cancelVoiceBtn.isHidden = true
    self.swtichToPlayModeBtn.isHidden = true
    self.playVoiceBtn.isHidden = false
    self.playCancelBtn.isHidden = false
    self.sendVoiceBtn.isHidden = false
  }
  
  @IBAction func cancelPlayVoice(_ sender: Any) {
    recordHelper.stopRecord()
    recordHelper.cancelledDeleteWithCompletion()
    self.resetSubViewsStyle()
  }
  
  @IBAction func sendRecordedVoice(_ sender: Any) {
//    self.inputViewDelegate?.finishRecordVoice?(self.finishiRecorderCache!.voiceFilePath, durationTime: self.finishiRecorderCache!.duration)
    self.featureDelegate?.didRecordVoice(with: self.finishiRecorderCache!.voiceFilePath, durationTime: self.finishiRecorderCache!.duration)
  }
  
  @IBAction func playRecordedVoice(_ sender: IMUIProgressButton) {
    if sender.isSelected {
      // to pause voice
      self.stopVoice()
    } else {
      // to play voice
      self.playVoice()
    }
    
    sender.isSelected = !sender.isSelected
  }
  
  func stopVoice() {
    IMUIAudioPlayerHelper.sharedInstance.stopAudio()
  }
  
  func playVoice() {
    do {
      let voiceData = try Data(contentsOf: URL(fileURLWithPath: recordHelper.recordPath!))

      IMUIAudioPlayerHelper.sharedInstance.playAudioWithData("",voiceData, progressCallback: { (identify, currentTime, duration) in
        self.playVoiceBtn.progress = CGFloat(currentTime/duration)
        
      }, finishCallBack: { (identify) in
        self.playVoiceBtn.isSelected = false
      }, stopCallBack: {id in })
    } catch {
      print("fail to play recorded voice!")
      print(error)
    }
  }
  
  @objc func handlePan(recognizer:UIPanGestureRecognizer) {
    let pointInSuperView = recognizer.location(in: self.contentView)
    
    // touch move out from recordVoiceBtn
    if !self.recordVoiceBtn.frame.contains(pointInSuperView) {
      let playDistance =  abs(self.swtichToPlayModeBtn.imui_centerX - self.recordVoiceBtn.imui_left)
      let playProgress = (self.recordVoiceBtn.imui_left - pointInSuperView.x) > 0 ? min((self.recordVoiceBtn.imui_left - pointInSuperView.x), playDistance) : 0
      
      var sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(playProgress / playDistance) * 30.0
      playVoiceBtnHeight.constant = sizeWidth
      playVoiceBtnWidth.constant = sizeWidth
      self.swtichToPlayModeBtn.layer.cornerRadius = sizeWidth / 2
      
      let cancelDistance = abs(self.cancelVoiceBtn.imui_centerX - self.recordVoiceBtn.imui_right)
      let cancelProgress = (pointInSuperView.x - self.recordVoiceBtn.imui_right) > 0 ? min(pointInSuperView.x - self.recordVoiceBtn.imui_right, cancelDistance) : 0
      sizeWidth = IMUIRecordVoiceCell.buttonNormalWith + abs(cancelProgress / cancelDistance) * 30.0
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
        self.switchToPlayVoiceModel()
        self.finishiRecorderCache = recordHelper.finishRecordingCompletion()
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
    recorderPath?.append("\(dateFormatter.string(from: now))-MySound.m4a")
    print("\(recorderPath)")
    return recorderPath!
  }
  
}

