//
//  IMUIRecordVoiceHelper.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation


let maxRecordTime = 60.0
typealias CompletionCallBack = () -> Void
typealias TimerTickCallback = (TimeInterval, CGFloat) -> Void //  recordDuration, meter

typealias IMUIFinishRecordVoiceCallBack = () -> Data


class IMUIRecordVoiceHelper: NSObject {

  var startRecordCallBack: CompletionCallBack?
  var timerTickCallBack: TimerTickCallback?
  
  var recorder: AVAudioRecorder?
  var recordPath: String?

  var timerFireDate: Date?
  var recordDuration: TimeInterval {
    get {
      if timerFireDate != nil {
        return Date().timeIntervalSince(timerFireDate!)
      } else {
        return 0.0
      }
    }
  }
  
  var updater: CADisplayLink! = nil
  
  override init() {
    super.init()
  }
  
  deinit {
    self.stopRecord()
    self.recordPath = nil
  }
  
  func resetTimer() {
    self.timerFireDate = nil
    
    updater?.invalidate()
    updater = nil
  }
  
  func cancelRecording() {
    if self.recorder == nil { return }
    
    if self.recorder?.isRecording != false {
      self.recorder?.stop()
    }
    
    self.recorder = nil
    self.resetTimer()
  }
  
  func stopRecord() {
    self.cancelRecording()
    self.resetTimer()

  }
  
  func startRecordingWithPath(_ path:String,
                              startRecordCompleted: @escaping CompletionCallBack,
                              timerTickCallback: @escaping TimerTickCallback) {
    
    print("Action - startRecordingWithPath:")
    self.startRecordCallBack = startRecordCompleted
    self.timerTickCallBack = timerTickCallback

    self.timerFireDate = Date()
    self.recordPath = path
    
    let audioSession:AVAudioSession = AVAudioSession.sharedInstance()
    do {
      try audioSession.setCategory(AVAudioSessionCategoryPlayAndRecord)
    } catch let error as NSError {
      print("could not set session category")
      print(error.localizedDescription)
    }
    
    do {
      try audioSession.setActive(true)
    } catch let error as NSError {
      print("could not set session active")
      print(error.localizedDescription)
    }
    
//    let recordSettings:[String : AnyObject] = [
//      AVFormatIDKey: NSNumber(value: kAudioFormatAppleIMA4 as UInt32),
//      AVNumberOfChannelsKey: 1 as AnyObject,
//      AVSampleRateKey : 16000.0 as AnyObject
//    ]
    
    let recordSettings = [
      AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
      AVSampleRateKey: 16000,
      AVNumberOfChannelsKey: 1,
      AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
    ]
    
    do {
      self.recorder = try AVAudioRecorder(url: URL(fileURLWithPath: self.recordPath!), settings: recordSettings)
      self.recorder!.delegate = self
      self.recorder!.isMeteringEnabled = true
      self.recorder!.prepareToRecord()
      self.recorder?.record(forDuration: 160.0)
    } catch let error as NSError {
      recorder = nil
      print(error.localizedDescription)
    }
    
    if ((self.recorder?.record()) != false) {

    } else {
      print("fail record")
    }

    self.updater = CADisplayLink(target: self, selector: #selector(self.trackAudio))
    updater.add(to: RunLoop.current, forMode: RunLoopMode.commonModes)
    updater.frameInterval = 1
    
    if self.startRecordCallBack != nil {
      DispatchQueue.main.async(execute: self.startRecordCallBack!)
      
    }
  }
  
  @objc func trackAudio() {
    self.timerTickCallBack?(recordDuration, 0.0)
  }
  
  open func finishRecordingCompletion() -> (voiceFilePath: String, duration: TimeInterval){
    let duration = recordDuration
    self.stopRecord()
    if let _ = recordPath {
      return (voiceFilePath: recordPath!, duration: duration)
    } else {
      return ("",0)
    }
  }
  
  func cancelledDeleteWithCompletion() {
    self.stopRecord()
    if self.recordPath == nil { return }
    
    let fileManager:FileManager = FileManager.default
    if fileManager.fileExists(atPath: self.recordPath!) == true {
      do {
        try fileManager.removeItem(atPath: self.recordPath!)
      } catch let error as NSError {
        print("can no to remove the voice file \(error.localizedDescription)")
      }
    } else {

    }
    
    
  }
  
  open func playVoice(_ recordPath:String) {
    do {
      print("\(recordPath)")
      let player:AVAudioPlayer = try AVAudioPlayer(contentsOf: URL(fileURLWithPath: recordPath))
      player.volume = 1
      player.delegate = self
      player.numberOfLoops = -1
      player.prepareToPlay()
      player.play()
      
    } catch let error as NSError {
      print("get AVAudioPlayer is fail \(error)")
    }
  }
  
}


// MARK: - AVAudioPlayerDelegate
extension IMUIRecordVoiceHelper : AVAudioPlayerDelegate {
  func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
    print("finished playing \(flag)")
    
  }
  
  func audioPlayerDecodeErrorDidOccur(_ player: AVAudioPlayer, error: Error?) {
    if let e = error {
      print("\(e.localizedDescription)")
    }
  }
}

// MARK: - AVAudioRecorderDelegate
extension IMUIRecordVoiceHelper : AVAudioRecorderDelegate {
  
}
