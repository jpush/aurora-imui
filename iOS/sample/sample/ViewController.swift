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
  var imageUrlArray = [
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926548887&di=f107f4f8bd50fada6c5770ef27535277&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F67%2F23%2F69i58PICP37.jpg",//1
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926367926&di=ac707ee3e73241daaa5598730d28909d&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fapp%2Ficon%2F20160220%2F1455956985275086.jpg",//2
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926419519&di=c545a5d3310e88454d222623532e06b7&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fyouxi%2Fimages%2F2015%2F0701%2F20150701085247270.jpg",//3
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926596720&di=001e99492a3e684a63c07b204ff1c641&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01567057a188f70000018c1bc79411.jpg%40900w_1l_2o_100sh.jpg",//4
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926617378&di=01ade16186d4f0b6ef4fead945d142c4&imgtype=0&src=http%3A%2F%2Fimg1.tplm123.com%2F2008%2F04%2F04%2F3421%2F2309912507054.jpg",//5
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926710881&di=83ecd418f598bcadb9d74e5075397fc2&imgtype=0&src=http%3A%2F%2Fwww.missku.com%2Fd%2Ffile%2Fimport%2F2015%2F1211%2Fthumb_20151211142740226.jpg",//6
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926731796&di=e431578738f709fd75f17799a91ac4a9&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fbaike%2Fw%253D268%2Fsign%3D4c99e09935d3d539c13d08c50286e927%2F8c1001e93901213f3d7d8ebb57e736d12f2e950f.jpg",//7
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926752612&di=7a8d887ece70f73517b32803a2e048cd&imgtype=0&src=http%3A%2F%2Fimg10.360buyimg.com%2FpopWaterMark%2Fg15%2FM01%2F03%2F13%2FrBEhWVLh4JEIAAAAAAB99-puGocAAIKSwLztRsAAH4P213.jpg",//8
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926795383&di=1ce1c07257fa6918c4fbbeb3ee4e1eef&imgtype=0&src=http%3A%2F%2Fd.ifengimg.com%2Fw600%2Fp0.ifengimg.com%2Fpmop%2F2018%2F0322%2FB02F8FEE6DF6ECD3358F1EB877ECABC93268790E_size31_w643_h643.jpeg",//9
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926793631&di=76964940e9b139ec8960ebf3dc360c8c&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20170828%2F84c750b9293744549a169ae3d80a0dab.jpeg",//10
  ]
  
  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  @IBOutlet weak var myInputView: IMUIInputView!
  var currentType:IMUIFeatureType = .voice
  
  var imageViewArr = [MyImageView]()
  
  let imageManage: PHCachingImageManager = PHCachingImageManager()
  
  var inputViewBottomItemArr = [IMUIFeatureIconModel]()
  var inputViewRightItemArr = [IMUIFeatureIconModel]()
  var inputViewLeftItemArr = [IMUIFeatureIconModel]()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.myInputView.delegate = self
//    self.myInputView.inputViewDelegate = self 
    self.messageCollectionView.delegate = self
    
    self.messageCollectionView.messageCollectionView.register(MessageEventCollectionViewCell.self, forCellWithReuseIdentifier: MessageEventCollectionViewCell.self.description())
    
  }
  
  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)
    for urlStr in self.imageUrlArray {
      let outGoingmessage = MyMessageModel(imageUrl: urlStr, isOutGoing: true)
      self.messageCollectionView.appendMessage(with: outGoingmessage)
    }
  }
}


// MARK: - IMUIInputViewDelegate
extension ViewController: IMUIInputViewDelegate {
  
  func keyBoardWillShow(height: CGFloat, durationTime: Double) {
    self.messageCollectionView.scrollToBottom(with: true)
  }
  
  func sendTextMessage(_ messageText: String) {
    let outGoingmessage = MyMessageModel(text: messageText, isOutGoing: true)
    let inCommingMessage = MyMessageModel(text: messageText, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func switchToMicrophoneMode(recordVoiceBtn: UIButton) {
//    self.showToast(alert: "switchToMicrophoneMode")
  }
  
  func switchToCameraMode(cameraBtn: UIButton) {
//    self.showToast(alert: "switchToCameraMode")
  }
  
  func switchToEmojiMode(cameraBtn: UIButton) {
//    self.showToast(alert: "switchToEmojiMode")
  }
  
  func didShootPicture(picture: Data) {
    let imgPath = self.getPath()
    DispatchQueue.main.async {
      let imageView = MyImageView()
      self.imageViewArr.append(imageView)
      
      do {
        try picture.write(to: URL(fileURLWithPath: imgPath))
        DispatchQueue.main.async {
          let outGoingmessage = MyMessageModel(imagePath: imgPath, isOutGoing: true)
          self.messageCollectionView.appendMessage(with: outGoingmessage)
        }
      } catch {
        print("write image file error")
      }
    }
    
    
  }
  
  func startRecordVoice() {
//    self.showToast(alert: "startRecordVoice")
  }
  
  func startRecordVideo() {
//    self.showToast(alert: "startRecordVideo")
  }
  
  func finishRecordVideo(videoPath: String, durationTime: Double) {
    let outGoingmessage = MyMessageModel(videoPath: videoPath, isOutGoing: true)
    let inCommingMessage = MyMessageModel(videoPath: videoPath, isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func finishRecordVoice(_ voicePath: String, durationTime: Double) {
    
    let outGoingmessage = MyMessageModel(voicePath: voicePath, duration: CGFloat(durationTime), isOutGoing: true)
    let inCommingMessage = MyMessageModel(voicePath: voicePath, duration: CGFloat(durationTime), isOutGoing: false)
    self.messageCollectionView.appendMessage(with: outGoingmessage)
    self.messageCollectionView.appendMessage(with: inCommingMessage)
  }
  
  func didSeletedGallery(AssetArr: [PHAsset]) {
    for asset in AssetArr {
      switch asset.mediaType {
      case .image:
        let option = PHImageRequestOptions()
        option.isSynchronous = true
        option.isNetworkAccessAllowed = true
        
        imageManage.requestImage(for: asset, targetSize: CGSize(width: asset.pixelWidth/4, height: asset.pixelHeight/4), contentMode: .aspectFill, options: option, resultHandler: { [weak self] (image, _) in
          let imageData = image!.pngData()
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
    DispatchQueue.main.async {
        self.myInputView.hideFeatureView()
//      self.myInputView
    }
    
  }
  
  func showToast(alert: String) {
    
    let toast = UIAlertView(title: alert, message: nil, delegate: nil, cancelButtonTitle: nil)
    toast.show()
    toast.dismiss(withClickedButtonIndex: 0, animated: true)
  }
}

