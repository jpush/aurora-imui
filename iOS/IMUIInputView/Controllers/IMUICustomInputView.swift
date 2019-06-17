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

fileprivate var IMUIFeatureViewHeight:CGFloat = 215
fileprivate var IMUIFeatureSelectorHeight:CGFloat = 46
fileprivate var IMUIShowFeatureViewAnimationDuration = 0.25

open class IMUICustomInputView: UIView {
  @objc open var inputTextViewPadding: UIEdgeInsets = UIEdgeInsets(top: 8, left: 12, bottom: 8, right: 12)
  @objc open var inputTextViewHeightRange: UIFloatRange = UIFloatRange(minimum: 20, maximum: 60)
  
  @objc open var inputTextViewTextColor: UIColor = UIColor(netHex: 0x555555)
  @objc open var inputTextViewFont: UIFont = UIFont.systemFont(ofSize: 18)
  
  var inputViewStatus: IMUIInputStatus = .none
  
  @objc open weak var inputViewDelegate: IMUICustomInputViewDelegate?
  
  fileprivate weak var inputViewDataSource: IMUICustomInputViewDataSource?
  @objc open var dataSource: IMUICustomInputViewDataSource? {
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
    view = bundle.loadNibNamed("IMUICustomInputView", owner: self, options: nil)?.first as? UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    self.inputTextView.textContainer.lineBreakMode = .byWordWrapping
    self.inputTextView.font = UIFont.systemFont(ofSize: 14)
    self.inputTextView.textColor = inputTextViewTextColor
    self.inputTextView.layoutManager.allowsNonContiguousLayout = false
    inputTextView.delegate = self
  }
  
  open override func awakeFromNib() {
    super.awakeFromNib()

    NotificationCenter.default.addObserver(self,
                                           selector: #selector(self.keyboardFrameChanged(_:)),
                                           name: UIResponder.keyboardWillChangeFrameNotification,
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

    self.bottomInputBarItemListView.position = .bottom
    self.leftInputBarItemListView.position = .left
    self.rightInputBarItemListView.position = .right
    
  }
  
  @objc public func layoutInputBar() {
    let bottomCount = self.inputViewDataSource?.imuiInputView(self.bottomInputBarItemListView.featureListCollectionView, numberForItemAt: .bottom) ?? 0
    
    if bottomCount == 0 {
      self.bottomInputBarItemListViewHeight.constant = 8
    }
    
    self.rightInputBarItemListViewWidth.constant = self.rightInputBarItemListView.totalWidth
    self.leftInputBarItemListViewWidth.constant = self.leftInputBarItemListView.totalWidth
    
    self.bottomInputBarItemListView.layoutFeatureListToCenter()
  }
  
  @objc public func setBackgroundColor(color: UIColor) {
    self.backgroundColor = color
    self.rightInputBarItemListView.featureListCollectionView.backgroundColor = color
    self.leftInputBarItemListView.featureListCollectionView.backgroundColor = color
    self.bottomInputBarItemListView.featureListCollectionView.backgroundColor = color
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
  
  @objc func keyboardFrameChanged(_ notification: Notification) {
    let dic = NSDictionary(dictionary: (notification as NSNotification).userInfo!)
    let keyboardValue = dic.object(forKey: UIResponder.keyboardFrameEndUserInfoKey) as! NSValue
    let bottomDistance = UIScreen.main.bounds.size.height - keyboardValue.cgRectValue.origin.y
    let duration = Double(dic.object(forKey: UIResponder.keyboardAnimationDurationUserInfoKey) as! NSNumber)
    
    UIView.animate(withDuration: duration) {
      if bottomDistance > 10.0 {
        IMUIFeatureViewHeight = bottomDistance
        self.inputViewDelegate?.keyBoardWillShow?(height: keyboardValue.cgRectValue.size.height, durationTime: duration)
        self.moreViewHeight.constant = IMUIFeatureViewHeight
      }
    }
    
    DispatchQueue.main.async {
        self.superview?.layoutIfNeeded()
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
      self.inputTextView.resignFirstResponder()
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
extension IMUICustomInputView: UITextViewDelegate {
  public func textViewDidChange(_ textView: UITextView) {
    self.fitTextViewSize(textView)

    self.inputViewDelegate?.textDidChange?(text: textView.text)
  }
  
  public func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
//    inputViewStatus = .text
    return true
  }
  
}
