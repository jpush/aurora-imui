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
  case emoji
  case none
}

fileprivate var IMUIFeatureViewHeight:CGFloat = 215
fileprivate var IMUIFeatureSelectorHeight:CGFloat = 46
fileprivate var IMUIShowFeatureViewAnimationDuration = 0.25

open class IMUIInputView: UIView {
  
  var inputViewStatus: IMUIInputStatus = .none
  @objc open weak var inputViewDelegate: IMUIInputViewDelegate? {
    didSet {
      self.featureView.inputViewDelegate = self.inputViewDelegate
    }
  }
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
  @IBOutlet weak var featureSelectorView: IMUIFeatureListView!
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
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    self.featureView.delegate = self
    self.featureSelectorView.delegate = self
  }
  
  func leaveGalleryMode() {
    featureView.clearAllSelectedGallery()
    self.updateSendBtnToPhotoSendStatus()
  }
  
  @objc func keyboardFrameChanged(_ notification: Notification) {
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
  
  @objc public func hideFeatureView() {
    UIView.animate(withDuration: IMUIShowFeatureViewAnimationDuration) { 
      self.moreViewHeight.constant = 0
      self.inputTextView.resignFirstResponder()
      self.superview?.layoutIfNeeded()
    }
  }
  
  deinit {
    self.featureView.clearAllSelectedGallery()
    NotificationCenter.default.removeObserver(self)
  }
}

// MARK: - UITextViewDelegate
extension IMUIInputView: UITextViewDelegate {
  public func textViewDidChange(_ textView: UITextView) {
    self.fitTextViewSize(textView)
    self.updateSendBtnToPhotoSendStatus()
  }
  
  public func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
    inputViewStatus = .text
    return true
  }
  
}

extension IMUIInputView: IMUIFeatureListDelegate {
  public func onSelectedFeature(with cell: IMUIFeatureListIconCell) {
    print("onSelectedFeature")
    switch cell.featureData!.featureType {
    case .voice:
      self.clickMicBtn(cell: cell)
      break
    case .gallery:
      self.clickPhotoBtn(cell: cell)
      break
    case .camera:
      self.clickCameraBtn(cell: cell)
      break
    case .emoji:
      self.clickEmojiBtn(cell: cell)
      break
    default:
      break
    }
  }
  
  public func onClickSend(with cell: IMUIFeatureListIconCell) {
//    print("onClickSend")
    self.clickSendBtn(cell: cell)
  }
  
  
  func clickMicBtn(cell: IMUIFeatureListIconCell) {
    self.leaveGalleryMode()
    inputViewStatus = .microphone
    
    self.inputTextView.resignFirstResponder()
    
    self.inputViewDelegate?.switchToMicrophoneMode?(recordVoiceBtn: cell.featureIconBtn)
    
    let serialQueue = DispatchQueue(label: "inputview")
    serialQueue.async {
      usleep(10000)
      DispatchQueue.main.async {
        self.featureView.layoutFeature(with: .voice)
        self.showFeatureView()
      }
    }
  }
  
  func clickPhotoBtn(cell: IMUIFeatureListIconCell) {
    self.leaveGalleryMode()
    inputViewStatus = .photo
    
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchToGalleryMode?(photoBtn: cell.featureIconBtn)
    DispatchQueue.main.async {
      self.featureView.layoutFeature(with: .gallery)
    }
    self.showFeatureView()
  }
  
  func clickCameraBtn(cell: IMUIFeatureListIconCell) {
    self.leaveGalleryMode()
    inputViewStatus = .camera
    
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchToCameraMode?(cameraBtn: cell.featureIconBtn)
    DispatchQueue.main.async {
      self.featureView.layoutFeature(with: .camera)
    }
    self.showFeatureView()
  }
  
  func clickEmojiBtn(cell: IMUIFeatureListIconCell) {
    self.leaveGalleryMode()
    inputViewStatus = .emoji
    
    inputTextView.resignFirstResponder()
    inputViewDelegate?.switchToEmojiMode?(cameraBtn: cell.featureIconBtn)
    
    DispatchQueue.main.async {
      self.featureView.layoutFeature(with: .emoji)
    }
    self.showFeatureView()
  }
  
  func clickSendBtn(cell: IMUIFeatureListIconCell) {
    if IMUIGalleryDataManager.selectedAssets.count > 0 {
      self.inputViewDelegate?.didSeletedGallery?(AssetArr: IMUIGalleryDataManager.selectedAssets)
      self.featureView.clearAllSelectedGallery()
      self.updateSendBtnToPhotoSendStatus()
      return
    }
    
    if inputTextView.text != "" {
      inputViewDelegate?.sendTextMessage?(self.inputTextView.text)
      inputTextView.text = ""
      fitTextViewSize(inputTextView)
    }
    
    self.updateSendBtnToPhotoSendStatus()
  }
}

extension IMUIInputView: IMUIFeatureViewDelegate {
  public func didChangeSelectedGallery(with gallerys: [PHAsset]) {
      self.updateSendBtnToPhotoSendStatus()
  }
  
  public func updateSendBtnToPhotoSendStatus() {
    var isAllowToSend = false
    var seletedPhotoCount = IMUIGalleryDataManager.selectedAssets.count
    if seletedPhotoCount > 0 {
      isAllowToSend = true
    }
    
    if inputTextView.text != "" {
      isAllowToSend = true
    }
    
    self.featureSelectorView.updateSendButton(with: seletedPhotoCount, isAllowToSend: isAllowToSend)
  }
  
  public func didSelectPhoto(with images: [UIImage]) {
    self.updateSendBtnToPhotoSendStatus()
  }
  
  public func didSeletedEmoji(with emoji: IMUIEmojiModel) {
    switch emoji.emojiType {
    case .emoji:
      self.inputTextView.text.append(emoji.emoji!)
      self.fitTextViewSize(self.inputTextView)
      self.updateSendBtnToPhotoSendStatus()
    default:
      return
    }
    
  }
  
  public func didRecordVoice(with voicePath: String, durationTime: Double) {
    self.inputViewDelegate?.finishRecordVoice?(voicePath, durationTime: durationTime)
  }
  
  public func didShotPicture(with image: Data) {
    self.inputViewDelegate?.didShootPicture?(picture: image)
  }
  
  public func didRecordVideo(with videoPath: String, durationTime: Double) {
    self.inputViewDelegate?.finishRecordVideo?(videoPath: videoPath, durationTime: durationTime)
  }
}
