//
//  IMUIInputView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/28.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos

@objc public enum IMUIInputViewItemPosition: UInt {
  case left = 0
  case right
  case bottom
}

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

open class IMUICustomInputView: UIView {
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
  
  fileprivate weak var inputViewDataSource: IMUIInputViewDataSource?
  @objc open var dataSource: IMUIInputViewDataSource? {
    set {
      self.inputViewDataSource = newValue
      self.rightInputBarItemListView.dataSource = newValue
      self.leftInputBarItemListView.dataSource = newValue
      self.bottomInputBarItemListView.dataSource = newValue
      self.featureView.dataSource = newValue
      self.reloadData()
      self.layoutInputBar()
    }
    
    get {
      return self.inputViewDataSource
    }
  }
  
  @IBOutlet var view: UIView!
  
  
  @IBOutlet weak var moreViewHeight: NSLayoutConstraint!
  
  @IBOutlet weak var inputTextViewHeight: NSLayoutConstraint!
  
  @IBOutlet open weak var bottomInputBarItemListView: IMUIFeatureListView!
  @IBOutlet weak var leftInputBarItemListView: IMUIFeatureListView!
  @IBOutlet weak var rightInputBarItemListView: IMUIFeatureListView!
  @IBOutlet weak var leftInputBarItemListViewWidth: NSLayoutConstraint!
  @IBOutlet weak var rightInputBarItemListViewWidth: NSLayoutConstraint!
  @IBOutlet weak var bottomInputBarItemListViewHeight: NSLayoutConstraint!
  
  @IBOutlet open weak var featureView: IMUIFeatureView!
  
  @IBOutlet open weak var inputTextView: UITextView!
  // to adjust textView Padding
  @IBOutlet weak var paddingLeft: NSLayoutConstraint!
  @IBOutlet weak var paddingRight: NSLayoutConstraint!
  @IBOutlet weak var paddingTop: NSLayoutConstraint!
  @IBOutlet weak var paddingBottom: NSLayoutConstraint!
  
  override public init(frame: CGRect) {
    super.init(frame: frame)
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUICustomInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    self.inputTextView.textContainer.lineBreakMode = .byWordWrapping
    self.inputTextView.font = UIFont.systemFont(ofSize: 14)
    self.inputTextView.textColor = inputTextViewTextColor
    self.inputTextView.layoutManager.allowsNonContiguousLayout = false
    inputTextView.delegate = self
    self.featureView.delegate = self
    
    
    
    // TEST
//    self.leftFeatureSelectorViewWidth.constant = 120
//    self.rightFeatureSelectorViewWidth.constant = 120
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
    view = bundle.loadNibNamed("IMUICustomInputView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    inputTextView.textContainer.lineFragmentPadding = 0
    inputTextView.textContainerInset = .zero
    
    inputTextView.textContainer.lineBreakMode = .byWordWrapping
    inputTextView.delegate = self
    self.featureView.delegate = self
    self.bottomInputBarItemListView.delegate = self
    
    self.bottomInputBarItemListView.position = .bottom
    self.leftInputBarItemListView.position = .left
    self.rightInputBarItemListView.position = .right
    
  }
  
  func layoutInputBar() {
    let bottomCount = self.inputViewDataSource?.imuiInputView(self.bottomInputBarItemListView.featureListCollectionView, numberForItemAt: .bottom) ?? 0
    
    if bottomCount == 0 {
      self.bottomInputBarItemListViewHeight.constant = 8
    }
    
    self.rightInputBarItemListViewWidth.constant = self.rightInputBarItemListView.totalWidth
    self.leftInputBarItemListViewWidth.constant = self.leftInputBarItemListView.totalWidth
  }
  
  public func register(_ cellClass: AnyClass?, in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {
    let featureSelectorList = self.getFeatureView(from: position)
    featureSelectorList.register(cellClass, forCellWithReuseIdentifier: identifier)
  }
  
  public func register(_ nib: UINib?,in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {
    let featureSelectorList = self.getFeatureView(from: position)
    featureSelectorList.register(nib, forCellWithReuseIdentifier: identifier)
  }
  
  public func registerForFeatureView(_ cellClass: AnyClass?, forCellWithReuseIdentifier identifier: String) {
    self.featureView.register(cellClass, forCellWithReuseIdentifier: identifier)
  }
  
  public func registerForFeatureView(_ nib: UINib?, forCellWithReuseIdentifier identifier: String) {
    self.featureView.register(nib, forCellWithReuseIdentifier: identifier)
  }

  func getFeatureView(from position: IMUIInputViewItemPosition) -> IMUIFeatureListView {
    switch position {
    case .left:
      return self.leftInputBarItemListView
    case .right:
      return self.rightInputBarItemListView
    case .bottom:
      return self.bottomInputBarItemListView
    }
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

  // TODO: update inputBar item status
  @objc public func updateInputBarItemCell(_ position: IMUIInputViewItemPosition,
                                           at index: Int) {
    let inputBarList = self.getFeatureView(from: position)
    inputBarList.featureListCollectionView.reloadItems(at: [IndexPath(item: index, section: 0)])
  }
  
  // TODO: switch featureView content
  @objc public func reloadFeaturnView() {
    self.featureView.featureCollectionView.reloadData()
  }
  
  @objc public func reloadData() {
    self.bottomInputBarItemListView.reloadData()
    self.rightInputBarItemListView.reloadData()
    self.leftInputBarItemListView.reloadData()
  }
  
  // 该接口提供给 React Native 用于处理 第三库（react-navigation 或 react-native-router-flux ）可能导致的布局问题。
  @objc public func layoutInputView() {
      self.fitTextViewSize(self.inputTextView)
  }
  
  @objc public func showFeatureView() {
    UIView.animate(withDuration: IMUIShowFeatureViewAnimationDuration) {
      self.moreViewHeight.constant = 253
      self.superview?.layoutIfNeeded()
    }
  }
  
  @objc public func hideFeatureView() {
//    self.moreViewHeight.constant = 0
//    self.featureView.currentType = .none
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
extension IMUICustomInputView: UITextViewDelegate {
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

extension IMUICustomInputView: IMUIFeatureListDelegate {
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

extension IMUICustomInputView: IMUIFeatureViewDelegate {
  
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
    
    self.bottomInputBarItemListView.updateSendButton(with: seletedPhotoCount, isAllowToSend: isAllowToSend)
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
