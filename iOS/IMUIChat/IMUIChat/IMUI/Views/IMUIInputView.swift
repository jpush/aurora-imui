//
//  IMUIInputView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/28.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

enum IMUIInputStatus {
  case microphone
  case photo
  case camera
  case none
}

var IMUIFeatureViewHeight:CGFloat = 215
var IMUIShowFeatureViewAnimationDuration = 0.25

@objc protocol IMUIInputViewDelegate: NSObjectProtocol {

  @objc optional func sendTextMessage(_ messageText: String)
  @objc optional func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
  @objc optional func sendPhotoMessage(_ imageArr: [UIImage])
  
  // RecordVoice
  @objc optional func switchOutOfRecordingVoiceMode(recordVoiceBtn: UIButton)
  @objc optional func startRecordingVoice()
  @objc optional func finishRecordingVoice(_ voicePath: String, durationTime: Double)
  @objc optional func cancelRecordingVoice()
  
  // Photo
  @objc optional func switchIntoSelectPhotoMode(photoBtn: UIButton)
  @objc optional func switchOutOfSelectPhotoMode(photoBtn: UIButton)
  @objc optional func showMoreView()
  @objc optional func photoClick(photoBtn: UIButton)
  @objc optional func finishSelectedPhoto(_ photoArr: [UIImage])
  
  // Camera
  @objc optional func switchIntoCameraMode(cameraBtn: UIButton)
  @objc optional func switchOutOfCameraMode()
  @objc optional func finishShootPicture(picture: Data)
  @objc optional func finishShootVideo(videoPath: String, durationTime: Double)
}

extension IMUIInputViewDelegate {
}


class IMUIInputView: UIView {
  
  public var inputViewStatus: IMUIInputStatus = .none
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
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    NotificationCenter.default.addObserver(self,
                                           selector: #selector(self.keyboardFrameChanged(_:)),
                                           name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                           object: nil)
    
  }
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    view = Bundle.main.loadNibNamed("IMUIInputView", owner: self, options: nil)?[0] as! UIView
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    
  }
  
  @IBAction func clickMicBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoRecordingVoiceMode?(recordVoiceBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .voice)
    self.showFeatureView()
  }
  
  @IBAction func clickPhotoBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoSelectPhotoMode?(photoBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .gallery)
    self.showFeatureView()
  }
  
  @IBAction func clickCameraBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoCameraMode?(cameraBtn: sender as! UIButton)
    self.featureView.layoutFeature(with: .camera)
    self.showFeatureView()
  }

  @IBAction func clickSendBtn(_ sender: Any) {
    if inputTextView.text != "" {
      inputViewDelegate?.sendTextMessage?(self.inputTextView.text)
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
