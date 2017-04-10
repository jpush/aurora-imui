//
//  IMUIInputView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/28.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos

enum IMUIInputStatus {
  case microphone
  case photo
  case camera
  case none
}

var IMUIFeatureViewHeight:CGFloat = 215
var IMUIShowFeatureViewAnimationDuration = 0.25

protocol IMUIInputViewDelegate: NSObjectProtocol {
  
  func sendTextMessage(_ messageText: String)
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
  func sendPhotoMessage(_ imageArr: [UIImage])
  
  // RecordVoice
  func switchOutOfRecordingVoiceMode(recordVoiceBtn: UIButton)
  func startRecordingVoice()
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
  func cancelRecordingVoice()
  
  // Photo
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
  func switchOutOfSelectPhotoMode(photoBtn: UIButton)
  func showMoreView()
  func photoClick(photoBtn: UIButton)
  func finishSelectedPhoto(_ photoArr: [Data])
  func finishSelectedVideo(_ VideoArr: [URL])

  func finishiSeletedGallery(AssetArr: [PHAsset])
  // Camera
  func switchIntoCameraMode(cameraBtn: UIButton)
  func switchOutOfCameraMode()
  func finishShootPicture(picture: Data)
  func finishShootVideo(videoPath: String, durationTime: Double)
}



extension IMUIInputViewDelegate {
  func sendTextMessage(_ messageText: String) {}
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {}
  func sendPhotoMessage(_ imageArr: [UIImage]) {}
  
  // RecordVoice
  func switchOutOfRecordingVoiceMode(recordVoiceBtn: UIButton) {}
  func startRecordingVoice() {}
  func finishRecordingVoice(_ voicePath: String, durationTime: Double) {}
  func cancelRecordingVoice() {}
  
  // Photo
  func switchIntoSelectPhotoMode(photoBtn: UIButton) {}
  func switchOutOfSelectPhotoMode(photoBtn: UIButton) {}
  func showMoreView() {}
  func photoClick(photoBtn: UIButton) {}
  func finishSelectedPhoto(_ photoArr: [UIImage]) {}
  func finishSelectedVideo(_ VideoArr: [URL]) {}
  func finishiSeletedGallery(AssetArr: [PHAsset]) {}
  // Camera
  func switchIntoCameraMode(cameraBtn: UIButton) {}
  func switchOutOfCameraMode() {}
  func finishShootPicture(picture: Data) {}
  func finishShootVideo(videoPath: String, durationTime: Double) {}
}


class IMUIInputView: UIView {
  
  public var inputViewStatus: IMUIInputStatus = .microphone
  open weak var inputViewDelegate: IMUIInputViewDelegate? {
    didSet {
      self.featureView.inputViewDelegate = self.inputViewDelegate
    }
  }
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
  @IBOutlet weak var featureView: IMUIFeatureView!
  @IBOutlet weak var inputTextView: UITextView!
  @IBOutlet weak var micBtn: UIButton!
  @IBOutlet weak var photoBtn: UIButton!
  @IBOutlet weak var cameraBtn: UIButton!
  @IBOutlet weak var sendBtn: UIButton!
  @IBOutlet weak var sendNumberLabel: UILabel!
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    NotificationCenter.default.addObserver(self,
                                           selector: #selector(self.keyboardFrameChanged(_:)),
                                           name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                           object: nil)
    self.sendNumberLabel.isHidden = true
    self.sendNumberLabel.layer.masksToBounds = true
    self.sendNumberLabel.layer.cornerRadius = self.sendNumberLabel.imui_width/2
    self.sendNumberLabel.layer.shadowOffset = CGSize(width: 2, height: 2)
    self.sendNumberLabel.layer.shadowRadius = 5
    self.sendNumberLabel.layer.shadowOpacity = 0.5
  }
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    view = Bundle.main.loadNibNamed("IMUIInputView", owner: self, options: nil)?[0] as! UIView
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    self.featureView.delegate = self
  }
  
  @IBAction func clickMicBtn(_ sender: Any) {
    
    self.inputTextView.resignFirstResponder()
    self.inputViewDelegate?.switchIntoRecordingVoiceMode(recordVoiceBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .voice)
    self.showFeatureView()
  }
  
  @IBAction func clickPhotoBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoSelectPhotoMode(photoBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .gallery)
    self.showFeatureView()
  }
  
  @IBAction func clickCameraBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoCameraMode(cameraBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .camera)
    self.showFeatureView()
  }

  @IBAction func clickSendBtn(_ sender: Any) {
    if IMUIGalleryDataManager.selectedAssets.count > 0 {
      self.inputViewDelegate?.finishiSeletedGallery(AssetArr: IMUIGalleryDataManager.selectedAssets)
      self.featureView.clearAllSelectedGallery()
      return
    }
    
    if inputTextView.text != "" {
      inputViewDelegate?.sendTextMessage(self.inputTextView.text)
      inputTextView.text = ""
      fitTextViewSize(inputTextView)
    }
  }
  
  func keyboardFrameChanged(_ notification: Notification) {
    let dic = NSDictionary(dictionary: (notification as NSNotification).userInfo!)
    let keyboardValue = dic.object(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
    let bottomDistance = UIScreen.main.bounds.size.height - keyboardValue.cgRectValue.origin.y
    let duration = Double(dic.object(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber)
    if bottomDistance > 10.0 {
      IMUIFeatureViewHeight = bottomDistance
    }
    
    UIView.animate(withDuration: duration) {
      self.moreViewHeight.constant = bottomDistance
      
      self.superview?.layoutIfNeeded()
    }
  }
  
  func fitTextViewSize(_ textView: UITextView) {
      let textViewFitSize = textView.sizeThatFits(CGSize(width: self.view.imui_width, height: CGFloat(MAXFLOAT)))
      self.inputTextViewHeight.constant = textViewFitSize.height
    }
  
  func showFeatureView() {
    UIView.animate(withDuration: IMUIShowFeatureViewAnimationDuration) { 
      self.moreViewHeight.constant = IMUIFeatureViewHeight
      self.superview?.layoutIfNeeded()
    }
  }
  
  func hideFeatureView() {
    UIView.animate(withDuration: IMUIShowFeatureViewAnimationDuration) { 
      self.moreViewHeight.constant = 0
      self.superview?.layoutIfNeeded()
    }
  }
  
  deinit {
    NotificationCenter.default.removeObserver(self)
  }
}

// MARK: - UITextViewDelegate
extension IMUIInputView: UITextViewDelegate {
  func textViewDidChange(_ textView: UITextView) {
    self.fitTextViewSize(textView)
  }
  
  func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
    self.featureView.layoutFeature(with: .none)
    return true
  }
  
}


extension IMUIInputView: IMUIFeatureViewDelegate {
  func didChangeSelectedGallery(with gallerys: [PHAsset]) {
    if gallerys.count == 0 {
      self.sendBtn.isEnabled = inputTextView.text == ""
    } else {
      self.sendBtn.isEnabled = true
    }
    
    self.updateSendBtnToPhotoSendStatus(with: gallerys.count)
  }
  
  func updateSendBtnToPhotoSendStatus(with number: Int) {

    self.sendBtn.isSelected = number > 0
    self.sendNumberLabel.isHidden = !(number > 0)
    self.sendNumberLabel.text = "\(number)"
  }
}
