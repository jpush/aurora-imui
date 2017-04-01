//
//  ViewController.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/23.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  @IBOutlet weak var myInputView: IMUIInputView!
  
  
  
  let dataManager = IMUIChatDataManager()
  let chatLayout = IMUIChatLayout()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.myInputView.inputViewDelegate = self
  }
  
  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    
  }
}


// MARK: - IMUIInputViewDelegate
extension ViewController: IMUIInputViewDelegate {
  
  func sendTextMessage(_ messageText: String) {
    let message = MyMessageModel(text: messageText)
    messageCollectionView.appendMessage(with: message)
  }
  
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {
    
  }
  
  func finishRecordingVoice(_ voicePath: String, durationTime: Double) {
    let message = MyMessageModel(voicePath: voicePath)
    messageCollectionView.appendMessage(with: message)
  }
  
  func finishShootPicture(picture: Data) {
    let imgPath = self.getPath()
    do {
      try picture.write(to: URL(fileURLWithPath: imgPath))
      DispatchQueue.main.async {
        let message = MyMessageModel(imagePath: imgPath)
        self.messageCollectionView.appendMessage(with: message)
      }
    } catch {
      print("write image file error")
    }
    
  }
  
  func finishSelectedPhoto(_ photoArr: [Data]) {
    for data in photoArr {
      finishShootPicture(picture: data)
    }
  }
  
  func finishSelectedVideo(_ VideoArr: [URL]) {
    
  }
  
  
  func finishShootVideo(videoPath: String, durationTime: Double) {
    let message = MyMessageModel(videoPath: videoPath)
    self.messageCollectionView.appendMessage(with: message)
  }
  
  
  func getPath() -> String {
    var recorderPath:String? = nil
    let now:Date = Date()
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "yy-MMMM-dd"
    recorderPath = "\(NSHomeDirectory())/Documents/"
    
    dateFormatter.dateFormat = "yyyy-MM-dd-hh-mm-ss"
    recorderPath?.append("\(dateFormatter.string(from: now))-image")
    return recorderPath!
  }
}
