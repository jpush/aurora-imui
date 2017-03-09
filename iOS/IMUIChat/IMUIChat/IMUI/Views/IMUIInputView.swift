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

  @objc func sendTextMessage(_ messageText: String)
}

extension IMUIInputViewDelegate {
  // sendText

  
  func sendPhotoMessage(_ imageArr: [UIImage]) {
  
  }
  // recordVoice
  
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton) {
  }
  func switchOutOfRecordingVoiceMode(recordVoiceBtn: UIButton) {}
  func startRecordingVoice() {}
  func finishRecordingVoice(_ voiceData: Data, durationTime: Double) {}
  func cancelRecordingVoice() {}
  
  // photo
  func switchIntoSelectPhotoMode(photoBtn: UIButton) {}
  func switchOutOfSelectPhotoMode(photoBtn: UIButton) {}
  func showMoreView() {}
  func photoClick(photoBtn: UIButton) {}
  func finishSelectedPhoto(_ photoArr: [UIImage]) {}
  
  // camera
  func switchIntoCameraMode(cameraBtn: UIButton) {}
  func switchOutOfCameraMode() {}
  func finishShootPicture(picture: UIImage) {}
  func finishShoootVideo(videoData: Data, durationTime: Double) {}
}


class IMUIInputView: UIView {
  
  public var inputViewStatus: IMUIInputStatus = .none
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
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
    inputViewDelegate?.switchIntoRecordingVoiceMode(recordVoiceBtn: sender as! UIButton)
    self.showFeatureView()
  }
  
  @IBAction func clickPhotoBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoSelectPhotoMode(photoBtn: sender as! UIButton)
    self.showFeatureView()
  }
  
  @IBAction func clickCameraBtn(_ sender: Any) {
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchIntoCameraMode(cameraBtn: sender as! UIButton)
    self.showFeatureView()
  }

  @IBAction func clickSendBtn(_ sender: Any) {
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

extension IMUIInputView: UITextViewDelegate {
  func textViewDidChange(_ textView: UITextView) {
    self.fitTextViewSize(textView)
  }
  
}
