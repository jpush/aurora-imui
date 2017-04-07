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
    let outGoingmessage = MyMessageModel(text: messageText, isOutGoing: true)
    let inCommingMessage = MyMessageModel(text: messageText, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {
    
  }
  
  func finishRecordingVoice(_ voicePath: String, durationTime: Double) {
    let outGoingmessage = MyMessageModel(voicePath: voicePath, isOutGoing: true)
    let inCommingMessage = MyMessageModel(voicePath: voicePath, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func finishShootPicture(picture: Data) {
    let imgPath = self.getPath()
    do {
      try picture.write(to: URL(fileURLWithPath: imgPath))
      DispatchQueue.main.async {
        let outGoingmessage = MyMessageModel(imagePath: imgPath, isOutGoing: true)
        let inCommingMessage = MyMessageModel(imagePath: imgPath, isOutGoing: false)
        self.messageCollectionView.appendMessage(with: outGoingmessage)
        self.messageCollectionView.appendMessage(with: inCommingMessage)
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
    let outGoingmessage = MyMessageModel(videoPath: videoPath, isOutGoing: true)
    let inCommingMessage = MyMessageModel(videoPath: videoPath, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
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
