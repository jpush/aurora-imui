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

protocol IMUIInputViewDelegate: NSObjectProtocol {
  // sendText
  func sendTextMessage(_ messageText: String)
  func sendPhotoMessage(_ imageArr: [UIImage])
  // recordVoice
  
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
  func switchOutOfRecordingVoiceMode(recordVoiceBtn: UIButton)
  func startRecordingVoice()
  func finishRecordingVoice(_ voiceData: Data, durationTime: Double)
  func cancelRecordingVoice()
  
  // photo
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
  func switchOutOfSelectPhotoMode(photoBtn: UIButton)
  func showMoreView()
  func photoClick(photoBtn: UIButton)
  func finishSelectedPhoto(_ photoArr: [UIImage])
  
  // camera
  func switchIntoCameraMode(cameraBtn: UIButton)
  func switchOutOfCameraMode()
  func finishShootPicture(picture: UIImage)
  func finishShoootVideo(videoData: Data, durationTime: Double)
  
}


class IMUIInputView: UIView {
  
  public var inputViewStatus: IMUIInputStatus = .none
  public weak var inputViewDelegate: IMUIInputViewDelegate?
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var bottomMergin: NSLayoutConstraint!
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
    
    self.inputTextView.textContainer.lineBreakMode = .byWordWrapping
    self.inputTextView.delegate = self
  }
  
  @IBAction func clickMicBtn(_ sender: Any) {
    self.inputTextView.resignFirstResponder()
    self.inputViewDelegate?.switchIntoRecordingVoiceMode(recordVoiceBtn: sender as! UIButton)
  }
  
  @IBAction func clickPhotoBtn(_ sender: Any) {
    self.inputTextView.resignFirstResponder()
    self.inputViewDelegate?.switchIntoSelectPhotoMode(photoBtn: sender as! UIButton)
  }
  
  @IBAction func clickCameraBtn(_ sender: Any) {
    self.inputTextView.resignFirstResponder()
    self.inputViewDelegate?.switchIntoCameraMode(cameraBtn: sender as! UIButton)
  }

  @IBAction func clickSendBtn(_ sender: Any) {

  }
  
  func keyboardFrameChanged(_ notification: Notification) {
    let dic = NSDictionary(dictionary: (notification as NSNotification).userInfo!)
    let keyboardValue = dic.object(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
    let bottomDistance = UIScreen.main.bounds.size.height - keyboardValue.cgRectValue.origin.y
    let duration = Double(dic.object(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber)
    
    UIView.animate(withDuration: duration) {
      self.bottomMergin.constant = bottomDistance
      self.superview?.layoutIfNeeded()
    }
  }
}

extension IMUIInputView: UITextViewDelegate {
  func textViewDidChange(_ textView: UITextView) {
    let textViewFitSize = textView.sizeThatFits(CGSize(width: self.view.imui_width, height: CGFloat(MAXFLOAT)))
    self.inputTextViewHeight.constant = textViewFitSize.height
  }
  
}
