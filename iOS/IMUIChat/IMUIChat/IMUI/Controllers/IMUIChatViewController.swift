//
//  IMUIChatViewController.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation
import MediaPlayer

class IMUIChatViewController: UIViewController {

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
extension IMUIChatViewController: IMUIInputViewDelegate {
  
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
  
  func finishShootVideo(videoPath: String, durationTime: Double) {
    let message = MyMessageModel(videoPath: videoPath)
    self.messageCollectionView.appendMessage(with: message)
//    do {
//      let videoAsset = AVAsset(url: URL(fileURLWithPath: videoPath))
//      let reader = try AVAssetReader(asset: videoAsset)
//      let videoTracks = videoAsset.tracks(withMediaType: AVMediaTypeVideo)
//      let videoTrack = videoTracks[0]
//
//      let options = [String(kCVPixelBufferPixelFormatTypeKey): kCVPixelFormatType_32BGRA]
//      let videoReaderOutput = AVAssetReaderTrackOutput(track: videoTrack, outputSettings: options)
//      reader.add(videoReaderOutput)
//      reader.startReading()
//      
//      while reader.status == .reading && videoTrack.nominalFrameRate > 0 {
//        let videoBuffer = videoReaderOutput.copyNextSampleBuffer()
//        if videoBuffer == nil { return }
//        
//        let image = self.imageFromSampleBuffer(sampleBuffer: videoBuffer!)
//        self.
//      }
//      
//    } catch {
//      print("can not load video file")
//    }
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
