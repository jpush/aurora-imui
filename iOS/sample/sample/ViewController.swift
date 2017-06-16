//
//  ViewController.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/23.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos
class MyImageView: UIImageView {
  
}

class ViewController: UIViewController {

  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  @IBOutlet weak var myInputView: IMUIInputView!
  
  var imageViewArr = [MyImageView]()
  
  let imageManage: PHCachingImageManager = PHCachingImageManager()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    print("\(UIView())")
    self.myInputView.inputViewDelegate = self
    self.messageCollectionView.delegate = self
    
    self.messageCollectionView.messageCollectionView.register(MessageEventCollectionViewCell.self, forCellWithReuseIdentifier: MessageEventCollectionViewCell.self.description())
  }
  
  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
  }
  
}


// MARK: - IMUIInputViewDelegate
extension ViewController: IMUIInputViewDelegate {
  
  func sendTextMessage(_ messageText: String) {
//    let outGoingmessage = MyMessageModel(text: messageText, isOutGoing: true)
//    let inCommingMessage = MyMessageModel(text: messageText, isOutGoing: false)
//    self.messageCollectionView.appendMessage(with: outGoingmessage)
//    self.messageCollectionView.appendMessage(with: inCommingMessage)
    
    let msgId = "\(NSDate().timeIntervalSince1970 * 1000)"
    let event = MessageEventModel(msgId: msgId, eventText: messageText)
    self.messageCollectionView.appendMessage(with: event)
  }
  
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {
    
  }
  
  func didShootPicture(picture: Data) {
    let imgPath = self.getPath()
    
    let imageView = MyImageView()
    self.imageViewArr.append(imageView)
    
    do {
      try picture.write(to: URL(fileURLWithPath: imgPath))
      DispatchQueue.main.async {
        let outGoingmessage = MyMessageModel(imagePath: imgPath, isOutGoing: true)
//        let inCommingMessage = MyMessageModel(imagePath: imgPath, isOutGoing: false)
        self.messageCollectionView.appendMessage(with: outGoingmessage)
//        self.messageCollectionView.appendMessage(with: inCommingMessage)
      }
    } catch {
      print("write image file error")
    }
    
  }
  
  func finishRecordVideo(videoPath: String, durationTime: Double) {
    let outGoingmessage = MyMessageModel(videoPath: videoPath, isOutGoing: true)
    let inCommingMessage = MyMessageModel(videoPath: videoPath, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func finishRecordVoice(_ voicePath: String, durationTime: Double) {
    let outGoingmessage = MyMessageModel(voicePath: voicePath, isOutGoing: true)
    let inCommingMessage = MyMessageModel(voicePath: voicePath, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func didSeletedGallery(AssetArr: [PHAsset]) {
    for asset in AssetArr {
      switch asset.mediaType {
      case .image:
        let option = PHImageRequestOptions()
        option.isSynchronous = true
        
        imageManage.requestImage(for: asset, targetSize: CGSize(width: 100.0, height: 100.0), contentMode: .aspectFill, options: option, resultHandler: { [weak self] (image, _) in
          let imageData = UIImagePNGRepresentation(image!)
          self?.didShootPicture(picture: imageData!)
        })
        break
      default:
        break
      }
    }
  }
  
  func getPath() -> String {
    var recorderPath:String? = nil
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "yy-MMMM-dd"
    recorderPath = "\(NSHomeDirectory())/Documents/"
    recorderPath?.append("\(NSDate.timeIntervalSinceReferenceDate)")
    return recorderPath!
  }
}


// MARK - IMUIMessageMessageCollectionViewDelegate
extension ViewController: IMUIMessageMessageCollectionViewDelegate {

// custom view
  func messageCollectionView(messageCollectionView: UICollectionView, forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> UICollectionViewCell? {
    if messageModel is MessageEventModel {
      var cellIdentify = MessageEventCollectionViewCell.self.description()
      let cell = messageCollectionView.dequeueReusableCell(withReuseIdentifier: cellIdentify, for: forItemAt) as! MessageEventCollectionViewCell
      let message = messageModel as! MessageEventModel
      cell.presentCell(eventText: message.eventText)
      return cell
    } else {
      return nil
    }
    
  }
  
  func messageCollectionView(messageCollectionView: UICollectionView, heightForItemAtIndexPath forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> NSNumber? {
    if messageModel is MessageEventModel {
      return 20.0
    } else {
      return nil
    }
  }
  
  func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageProtocol) {
    self.showToast(alert: "tap message bubble")
  }
  
  func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell, model: IMUIMessageProtocol) {
    self.showToast(alert: "tap header image")
  }
  
  func messageCollectionView(didTapStatusViewInCell: UICollectionViewCell, model: IMUIMessageProtocol) {
    self.showToast(alert: "tap status View")
  }

  
  func messageCollectionView(_: UICollectionView, willDisplayMessageCell: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageProtocol) {
  
  }
  
  func messageCollectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageProtocol) {
  
  }
  
  func messageCollectionView(_ willBeginDragging: UICollectionView) {
    self.myInputView.hideFeatureView()
  }
  
  func showToast(alert: String) {
    
    let toast = UIAlertView(title: alert, message: nil, delegate: nil, cancelButtonTitle: nil)
    toast.show()
    toast.dismiss(withClickedButtonIndex: 0, animated: true)
  }
}
