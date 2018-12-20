//
//  IMUIInputView2.swift
//  sample
//
//  Created by oshumini on 2018/4/12.
//  Copyright © 2018年 HXHG. All rights reserved.
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

public enum IMUIFeatureType {
  case voice
  case gallery
  case camera
  case location
  case emoji
  case empty
  case none
}

public protocol IMUIFeatureViewDelegate: NSObjectProtocol {
  
  func didSelectPhoto(with images: [UIImage])
  func startRecordVoice()
  func didRecordVoice(with voicePath: String, durationTime: Double)
  func didShotPicture(with image: Data)
  func startRecordVideo()
  func didRecordVideo(with videoPath: String, durationTime: Double)
  func didSeletedEmoji(with emoji: IMUIEmojiModel)
  func didChangeSelectedGallery(with gallerys: [PHAsset])
  func cameraFullScreen()
  func cameraRecoverScreen()
}

public extension IMUIFeatureViewDelegate {
  func didSelectPhoto(with images: [UIImage]) {}
  func startRecordVoice() {}
  func didRecordVoice(with voicePath: String, durationTime: Double) {}
  func didShotPicture(with image: Data) {}
  func startRecordVideo() {}
  func didRecordVideo(with videoPath: String, durationTime: Double) {}
  func didSeletedEmoji(with emoji: IMUIEmojiModel) {}
  func didChangeSelectedGallery() {}
  func cameraFullScreen() {}
  func cameraRecoverScreen() {}
}


open class IMUIInputView: IMUICustomInputView {

  struct IMUIInputViewData {
    var left: [IMUIFeatureIconModel]
    var right: [IMUIFeatureIconModel]
    var bottom: [IMUIFeatureIconModel]
    
    var allObjects: [String: [IMUIFeatureIconModel]] {
      return [
        "left": left,
        "right": right,
        "bottom": bottom
      ]
    }
    
    func dataWithPositon(position: IMUIInputViewItemPosition) -> [IMUIFeatureIconModel] {
      switch position {
      case .left:
        return left
      case .right:
        return right
      case .bottom:
        return bottom
      }
    }
  }
  
  var inputBarItemData = IMUIInputViewData(left: [IMUIFeatureIconModel](),
                                          right: [IMUIFeatureIconModel](),
                                          bottom: [IMUIFeatureIconModel]())
  
  var currentType:IMUIFeatureType = .voice
  @objc public weak var delegate: IMUIInputViewDelegate?
  
  override public init(frame: CGRect) {
    super.init(frame: frame)
  }
  
  open override func awakeFromNib() {
    super.awakeFromNib()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    self.setupInputViewData()
    self.inputViewDelegate = self
    self.dataSource = self
  }

  func setupInputViewData() {
    
    
    inputBarItemData.bottom.append(IMUIFeatureIconModel(featureType: .voice,
                                                       UIImage.imuiImage(with: "input_item_mic"),
                                                       UIImage.imuiImage(with:"input_item_mic")))

    inputBarItemData.bottom.append(IMUIFeatureIconModel(featureType: .gallery,
                                                       UIImage.imuiImage(with: "input_item_photo"),
                                                       UIImage.imuiImage(with:"input_item_photo")))

    inputBarItemData.bottom.append(IMUIFeatureIconModel(featureType: .camera,
                                                       UIImage.imuiImage(with: "input_item_camera"),
                                                       UIImage.imuiImage(with:"input_item_camera")))

    inputBarItemData.bottom.append(IMUIFeatureIconModel(featureType: .emoji,
                                                      UIImage.imuiImage(with: "input_item_emoji"),
                                                      UIImage.imuiImage(with:"input_item_emoji")))
    
    inputBarItemData.bottom.append(IMUIFeatureIconModel(featureType: .none,
                                                      UIImage.imuiImage(with: "input_item_send"),
                                                      UIImage.imuiImage(with:"input_item_send_message_selected"),
                                                      0,
                                                      false))
    self.registerCellForInputView()
  }
  
  // for react native dynamic, config inputViewBar with json
  @objc public func setupDataWithDic(dic:[String:[String]]) {
    var newData = IMUIInputViewData(left: [IMUIFeatureIconModel](),
                                    right: [IMUIFeatureIconModel](),
                                    bottom: [IMUIFeatureIconModel]())
    
    for (positionStr, itemArr) in dic {
      let position = self.convertStringToPosition(str: positionStr)
      for itemStr in itemArr {
        switch position {
          case .left:
            if let item = self.convertStringToIconModel(itemStr: itemStr) {
              newData.left.append(item)
            }
            break
          case .right:
            if let item = self.convertStringToIconModel(itemStr: itemStr) {
              newData.right.append(item)
            }
            break
          case .bottom:
            if let item = self.convertStringToIconModel(itemStr: itemStr) {
              newData.bottom.append(item)
            }
            break
        }
      }
    }
    self.inputBarItemData = newData
    self.layoutInputBar()
    self.reloadData()
  }
  
  @objc public func isNeedShowBottomView() -> Bool{
    return !inputBarItemData.bottom.isEmpty
  }
  
  fileprivate func convertStringToIconModel(itemStr: String) ->IMUIFeatureIconModel? {
    if itemStr == "voice" {
      return IMUIFeatureIconModel(featureType: .voice,
                                  UIImage.imuiImage(with: "input_item_mic"),
                                  UIImage.imuiImage(with:"input_item_mic"))
    }
    
    if itemStr == "gallery" {
      return IMUIFeatureIconModel(featureType: .gallery,
                           UIImage.imuiImage(with: "input_item_photo"),
                           UIImage.imuiImage(with:"input_item_photo"))
    }
    
    if itemStr == "camera" {
      return IMUIFeatureIconModel(featureType: .camera,
                           UIImage.imuiImage(with: "input_item_camera"),
                           UIImage.imuiImage(with:"input_item_camera"))
    }
    
    if itemStr == "emoji" {
      return IMUIFeatureIconModel(featureType: .emoji,
                           UIImage.imuiImage(with: "input_item_emoji"),
                           UIImage.imuiImage(with:"input_item_emoji"))
    }
    
    if itemStr == "send" {
      return IMUIFeatureIconModel(featureType: .none,
                           UIImage.imuiImage(with: "input_item_send"),
                           UIImage.imuiImage(with:"input_item_send_message_selected"),
                           0,
                           false)
    }
    return nil
  }
  
  
  fileprivate func registerCellForInputView() {
    let bundle = Bundle.imuiInputViewBundle()
    self.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), in: .bottom, forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    self.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), in: .right, forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    self.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), in: .left, forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    
    self.registerForFeatureView(UINib(nibName: "IMUIRecordVoiceCell", bundle: bundle),
                                forCellWithReuseIdentifier: "IMUIRecordVoiceCell")
    self.registerForFeatureView(UINib(nibName: "IMUIGalleryContainerCell", bundle: bundle),
                                forCellWithReuseIdentifier: "IMUIGalleryContainerCell")
    self.registerForFeatureView(UINib(nibName: "IMUICameraCell", bundle: bundle),
                                forCellWithReuseIdentifier: "IMUICameraCell")
    self.registerForFeatureView(UINib(nibName: "IMUIEmojiCell", bundle: bundle),
                                forCellWithReuseIdentifier: "IMUIEmojiCell")
    
    self.registerForFeatureView(IMUIEmptyContainerCell.self, forCellWithReuseIdentifier: "IMUIEmptyContainerCell")
  }
  
  // need dynamic get send model for react-native custom layout
  fileprivate var sendModel: IMUIFeatureIconModel {
    let position = self.findSendPosition()
    let model = inputBarItemData.dataWithPositon(position: position.position)[position.index]
    return model
  }
  
  fileprivate func findSendPosition() -> (position:IMUIInputViewItemPosition, index:Int) {

    for (key, arr) in inputBarItemData.allObjects  {
      for model in arr {
        if model.featureType == .none {
          let position = self.convertStringToPosition(str: key)
          let index = arr.index(of: model)
          return (position, index!)
        }
      }
    }
    
    return (.bottom,1)
  }
  
  fileprivate func convertStringToPosition(str: String) -> IMUIInputViewItemPosition {
    if str == "left" {
      return .left
    }
    
    if str == "right" {
      return .right
    }
    
    return .bottom
  }
  
  fileprivate func switchToFeature(type: IMUIFeatureType, button: UIButton) {
    self.featureView.layoutFeature(with: type)
    switch type {
    case .voice:
      self.delegate?.switchToMicrophoneMode?(recordVoiceBtn: button)
      break
    case .camera:
      self.delegate?.switchToCameraMode?(cameraBtn: button)
      break
    case .gallery:
      self.delegate?.switchToGalleryMode?(photoBtn: button)
      break
    case .emoji:
      self.delegate?.switchToEmojiMode?(cameraBtn: button)
      break
    default:
      break
    }
  }
}

extension IMUIInputView: IMUICustomInputViewDataSource {
  
  public func imuiInputView(_ inputBarItemListView: UICollectionView, numberForItemAt position: IMUIInputViewItemPosition) -> Int {
    switch position {
    case .right:
      return self.inputBarItemData.right.count
    case .left:
      return self.inputBarItemData.left.count
    case .bottom:
      return self.inputBarItemData.bottom.count
    }
  }
  
  public func imuiInputView(_ inputBarItemListView: UICollectionView,
                     _ position: IMUIInputViewItemPosition,
                     sizeForIndex indexPath: IndexPath) -> CGSize {
    return CGSize(width: 30, height: 30)
  }
  
  public func imuiInputView(_ inputBarItemListView: UICollectionView,
                     _ position: IMUIInputViewItemPosition,
                     cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    
    var dataArr:[IMUIFeatureIconModel]
    switch position {
    case .bottom:
      dataArr = self.inputBarItemData.bottom
    case .right:
      dataArr = self.inputBarItemData.right
    default:
      dataArr = self.inputBarItemData.left
    }
    
    let cellIdentifier = "IMUIFeatureListIconCell"
    let cell = inputBarItemListView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! IMUIFeatureListIconCell
    cell.layout(with: dataArr[indexPath.item],onClickCallback: { cell in
      if cell.featureData!.featureType != .none {
        self.currentType = cell.featureData!.featureType
        self.switchToFeature(type: self.currentType, button: cell.featureIconBtn)
        self.showFeatureView()
        self.reloadFeaturnView()
      }

      switch cell.featureData!.featureType {
      case .none:
        self.clickSendBtn(cell: cell)
        break
      default:
        break
      }
    })
    return cell
  }
  
  // featureView dataSource
  public func imuiInputView(_ featureView: UICollectionView,
                     cellForItem indexPath: IndexPath) -> UICollectionViewCell {
    var CellIdentifier = ""
    switch currentType {
    case .voice:
      CellIdentifier = "IMUIRecordVoiceCell"
    case .camera:
      CellIdentifier = "IMUICameraCell"
      break
    case .emoji:
      CellIdentifier = "IMUIEmojiCell"
      break
    case .location:
      // TODO:
      break
    case .gallery:
      CellIdentifier = "IMUIGalleryContainerCell"
      break
    case .empty:
      CellIdentifier = "IMUIEmptyContainerCell"
    default:
      break
    }
    var cell = featureView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIFeatureCellProtocol
    cell.activateMedia()
    cell.featureDelegate = self
    return cell as! UICollectionViewCell
  }
}


extension IMUIInputView: IMUIFeatureListDelegate {

  func clickSendBtn(cell: IMUIFeatureListIconCell) {
    if IMUIGalleryDataManager.selectedAssets.count > 0 {
      self.delegate?.didSeletedGallery?(AssetArr: IMUIGalleryDataManager.selectedAssets)
      self.featureView.clearAllSelectedGallery()
      self.updateSendBtnToPhotoSendStatus()
      return
    }
    
    if inputTextView.text != "" {
      delegate?.sendTextMessage?(self.inputTextView.text)
      inputTextView.text = ""
      self.delegate?.textDidChange?(text: "")
      fitTextViewSize(inputTextView)
    }
    
    self.updateSendBtnToPhotoSendStatus()
  }
  
  public func updateSendBtnToPhotoSendStatus() {
    var isAllowToSend = false
    let seletedPhotoCount = IMUIGalleryDataManager.selectedAssets.count
    if seletedPhotoCount > 0 {
      isAllowToSend = true
    }
    
    if inputTextView.text != "" {
      isAllowToSend = true
    }

    self.sendModel.isAllowToSend = isAllowToSend
    self.sendModel.photoCount = seletedPhotoCount
    
    let sendPosition = self.findSendPosition()
    self.updateInputBarItemCell(sendPosition.position, at: sendPosition.index)
  }
}

extension IMUIInputView: IMUIFeatureViewDelegate {
  public func cameraRecoverScreen() {
    self.delegate?.cameraRecoverScreen?()
  }
  
  public func cameraFullScreen() {
    self.delegate?.cameraFullScreen?()
  }
  
  public func didChangeSelectedGallery(with gallerys: [PHAsset]) {
    self.updateSendBtnToPhotoSendStatus()
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
      self.delegate?.textDidChange?(text: inputStr)
    default:
      return
    }
  }
  
  public func didRecordVoice(with voicePath: String, durationTime: Double) {
    self.delegate?.finishRecordVoice?(voicePath, durationTime: durationTime)
  }
  
  public func didShotPicture(with image: Data) {
    self.delegate?.didShootPicture?(picture: image)
  }
  
  public func startRecordVideo() {
    self.delegate?.startRecordVideo?()
  }
  
  public func startRecordVoice() {
    self.delegate?.startRecordVoice?()
  }
  
  public func didRecordVideo(with videoPath: String, durationTime: Double) {
    self.delegate?.finishRecordVideo?(videoPath: videoPath, durationTime: durationTime)
  }
  
}

extension IMUIInputView: IMUICustomInputViewDelegate {
  
  public func textDidChange(text: String) {
    self.updateSendBtnToPhotoSendStatus()
    self.delegate?.textDidChange?(text: text)
  }
  
  public func keyBoardWillShow(height: CGFloat, durationTime: Double) {
    self.currentType = .empty
    self.delegate?.keyBoardWillShow?(height: height, durationTime: durationTime)
    self.reloadFeaturnView()
  }
}

class IMUIEmptyContainerCell: UICollectionViewCell,IMUIFeatureCellProtocol { }
