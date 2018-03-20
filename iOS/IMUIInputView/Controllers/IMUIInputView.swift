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
  @objc open var inputTextViewPadding: UIEdgeInsets = UIEdgeInsets(top: 8, left: 12, bottom: 8, right: 12)
  @objc open var inputTextViewHeightRange: UIFloatRange = UIFloatRange(minimum: 17, maximum: 60)
  
  @objc open var inputTextViewTextColor: UIColor = UIColor(netHex: 0x555555)
  @objc open var inputTextViewFont: UIFont = UIFont.systemFont(ofSize: 18)
  
  var inputViewStatus: IMUIInputStatus = .none
  @objc open weak var inputViewDelegate: IMUIInputViewDelegate? {
    didSet {
      self.featureView.inputViewDelegate = self.inputViewDelegate
    }
  }
  @IBOutlet var view: UIView!
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
  @IBOutlet open weak var featureSelectorView: IMUIFeatureListView!
  @IBOutlet open weak var featureView: IMUIFeatureView!
  @IBOutlet open weak var inputTextView: UITextView!
  @IBOutlet weak var micBtn: UIButton!
  @IBOutlet weak var photoBtn: UIButton!
  @IBOutlet weak var cameraBtn: UIButton!
  @IBOutlet weak var sendBtn: UIButton!
  
  @IBOutlet weak var paddingLeft: NSLayoutConstraint!
  @IBOutlet weak var paddingRight: NSLayoutConstraint!
  @IBOutlet weak var paddingTop: NSLayoutConstraint!
  @IBOutlet weak var paddingBottom: NSLayoutConstraint!
  
  override public init(frame: CGRect) {
    super.init(frame: frame)
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    self.inputTextView.textContainer.lineBreakMode = .byWordWrapping
    self.inputTextView.font = UIFont.systemFont(ofSize: 14)
    self.inputTextView.textColor = inputTextViewTextColor
    self.inputTextView.layoutManager.allowsNonContiguousLayout = false
    inputTextView.delegate = self
    self.featureView.delegate = self
  }
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    NotificationCenter.default.addObserver(self,
                                           selector: #selector(self.keyboardFrameChanged(_:)),
                                           name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                           object: nil)
    
    self.paddingLeft.constant = self.inputTextViewPadding.left
    self.paddingRight.constant = self.inputTextViewPadding.right
    self.paddingTop.constant = self.inputTextViewPadding.top
    self.paddingBottom.constant = self.inputTextViewPadding.bottom
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineFragmentPadding = 0
    inputTextView.textContainerInset = .zero
    
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
    }
  }
  
  func fitTextViewSize(_ textView: UITextView) {
      let textViewFitSize = textView.sizeThatFits(CGSize(width: textView.imui_width, height: CGFloat(MAXFLOAT)))
      if textViewFitSize.height <= inputTextViewHeightRange.minimum {
        self.inputTextViewHeight.constant = inputTextViewHeightRange.minimum
        return
      }

      let newValue = textViewFitSize.height > inputTextViewHeightRange.maximum ? inputTextViewHeightRange.maximum : textViewFitSize.height
      if newValue != self.inputTextViewHeight.constant {
        self.inputTextViewHeight.constant = 120
        DispatchQueue.main.async {
          self.inputTextViewHeight.constant = newValue
          textView.scrollRangeToVisible(NSRange(location: 0, length: 0))
        }
      }
    }

  // 该接口提供给 React Native 用于处理 第三库（react-navigation 或 react-native-router-flux ）可能导致的布局问题。
  @objc public func layoutInputView() {
      self.fitTextViewSize(self.inputTextView)
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
      self.featureView.currentType = .none
      self.featureView.featureCollectionView.reloadData()
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
    self.inputViewDelegate?.textDidChange?(text: textView.text)
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
      self.inputViewDelegate?.textDidChange?(text: "")
      fitTextViewSize(inputTextView)
    }
    
    self.updateSendBtnToPhotoSendStatus()
  }
}

extension IMUIInputView: IMUIFeatureViewDelegate {
  
  public func cameraRecoverScreen() {
    self.inputViewDelegate?.cameraRecoverScreen?()
  }
  
  public func cameraFullScreen() {
    self.inputViewDelegate?.cameraFullScreen?()
  }
  
  
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
      let inputStr = "\(self.inputTextView.text!)\(emoji.emoji!)"
      self.inputTextView.text = inputStr
      self.fitTextViewSize(self.inputTextView)
      self.updateSendBtnToPhotoSendStatus()
      self.inputViewDelegate?.textDidChange?(text: inputStr)
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
