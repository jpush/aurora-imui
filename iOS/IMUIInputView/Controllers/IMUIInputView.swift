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
  case text
  case microphone
  case photo
  case camera
  case none
}

fileprivate var IMUIFeatureViewHeight:CGFloat = 215
fileprivate var IMUIShowFeatureViewAnimationDuration = 0.25

open class IMUIInputView: UIView {
  
  var inputViewStatus: IMUIInputStatus = .none
  open weak var inputViewDelegate: IMUIInputViewDelegate? {
    didSet {
      self.featureView.inputViewDelegate = self.inputViewDelegate
    }
  }
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
  @IBOutlet open weak var featureView: IMUIFeatureView!
  @IBOutlet weak var inputTextView: UITextView!
  @IBOutlet weak var micBtn: UIButton!
  @IBOutlet weak var photoBtn: UIButton!
  @IBOutlet weak var cameraBtn: UIButton!
  @IBOutlet weak var sendBtn: UIButton!
  @IBOutlet weak var sendNumberLabel: UILabel!
  
  override public init(frame: CGRect) {
    super.init(frame: frame)
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    self.featureView.delegate = self
    print("fsad")
  }
  
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
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
//    view = Bundle.main.loadNibNamed("IMUIInputView", owner: self, options: nil)?[0] as! UIView
    
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    self.featureView.delegate = self
  }
  
  @IBAction func clickMicBtn(_ sender: Any) {
    self.leaveGalleryMode()
    inputViewStatus = .microphone
    
    self.inputTextView.resignFirstResponder()
    
    self.inputViewDelegate?.switchToMicrophoneMode?(recordVoiceBtn: sender as! UIButton)
    
    let serialQueue = DispatchQueue(label: "inputview")
    serialQueue.async {
      usleep(10000)
      DispatchQueue.main.async {
        self.featureView.layoutFeature(with: .voice)
        self.showFeatureView()
      }
    }
  }
  
  @IBAction func clickPhotoBtn(_ sender: Any) {
    self.leaveGalleryMode()
    inputViewStatus = .photo
    
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchToGalleryMode?(photoBtn: sender as! UIButton)
    DispatchQueue.main.async {
      self.featureView.layoutFeature(with: .gallery)
    }
    self.showFeatureView()
  }
  
  @IBAction func clickCameraBtn(_ sender: Any) {
    self.leaveGalleryMode()
    inputViewStatus = .camera
    
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchToCameraMode?(cameraBtn: sender as! UIButton)
    DispatchQueue.main.async {
      self.featureView.layoutFeature(with: .camera)
    }
    self.showFeatureView()
  }

  @IBAction func clickSendBtn(_ sender: Any) {
    if IMUIGalleryDataManager.selectedAssets.count > 0 {
      self.inputViewDelegate?.didSeletedGallery?(AssetArr: IMUIGalleryDataManager.selectedAssets)
      self.featureView.clearAllSelectedGallery()
      self.updateSendBtnToPhotoSendStatus(with: 0)
      return
    }
    
    if inputTextView.text != "" {
      inputViewDelegate?.sendTextMessage?(self.inputTextView.text)
      inputTextView.text = ""
      fitTextViewSize(inputTextView)
    }
  }
  
  func leaveGalleryMode() {
    featureView.clearAllSelectedGallery()
    self.updateSendBtnToPhotoSendStatus(with: 0)
  }
  
  func keyboardFrameChanged(_ notification: Notification) {
    let dic = NSDictionary(dictionary: (notification as NSNotification).userInfo!)
    let keyboardValue = dic.object(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
    let bottomDistance = UIScreen.main.bounds.size.height - keyboardValue.cgRectValue.origin.y
    let duration = Double(dic.object(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber)
    
    UIView.animate(withDuration: duration) {
    if bottomDistance > 10.0 {
      IMUIFeatureViewHeight = bottomDistance
      self.inputViewDelegate?.keyBoardWillShow?(height: keyboardValue.cgRectValue.size.height, durationTime: duration)
      self.moreViewHeight.constant = IMUIFeatureViewHeight
    }
      self.superview?.layoutIfNeeded()
    }
  }
  
  func fitTextViewSize(_ textView: UITextView) {
      let textViewFitSize = textView.sizeThatFits(CGSize(width: self.view.imui_width, height: CGFloat(MAXFLOAT)))
      self.inputTextViewHeight.constant = textViewFitSize.height
    }
  
  open func showFeatureView() {
    UIView.animate(withDuration: IMUIShowFeatureViewAnimationDuration) {
      self.moreViewHeight.constant = 253
      self.superview?.layoutIfNeeded()
    }
  }
  
  open func hideFeatureView() {
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
  public func textViewDidChange(_ textView: UITextView) {
    self.fitTextViewSize(textView)
  }
  
  public func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
    inputViewStatus = .text
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
