//
//  IMUIInputView2.swift
//  sample
//
//  Created by oshumini on 2018/4/12.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import UIKit

class IMUIInputView: IMUICustomInputView {

  var inputViewBottomItemArr = [IMUIFeatureIconModel]()
  var inputViewRightItemArr = [IMUIFeatureIconModel]()
  var inputViewLeftItemArr = [IMUIFeatureIconModel]()
  
  var currentType:IMUIFeatureType = .voice
  
  override public init(frame: CGRect) {
    super.init(frame: frame)
    
    //    inputTextView.delegate = self
    //    self.featureView.delegate = self
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
    
    let bundle = Bundle.imuiInputViewBundle()
    self.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), in: .bottom, forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    self.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), in: .right, forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    
    
    self.registerForFeatureView(UINib(nibName: "IMUIRecordVoiceCell", bundle: bundle),
                                                forCellWithReuseIdentifier: "IMUIRecordVoiceCell")
    self.registerForFeatureView(UINib(nibName: "IMUIGalleryContainerCell", bundle: bundle),
                                                forCellWithReuseIdentifier: "IMUIGalleryContainerCell")
    self.registerForFeatureView(UINib(nibName: "IMUICameraCell", bundle: bundle),
                                                forCellWithReuseIdentifier: "IMUICameraCell")
    self.registerForFeatureView(UINib(nibName: "IMUIEmojiCell", bundle: bundle),
                                                forCellWithReuseIdentifier: "IMUIEmojiCell")
    
    inputViewBottomItemArr.append(IMUIFeatureIconModel(featureType: .voice,
                                                       UIImage.imuiImage(with: "input_item_mic"),
                                                       UIImage.imuiImage(with:"input_item_mic")))
    
    inputViewBottomItemArr.append(IMUIFeatureIconModel(featureType: .gallery,
                                                       UIImage.imuiImage(with: "input_item_photo"),
                                                       UIImage.imuiImage(with:"input_item_photo")))
    
    inputViewBottomItemArr.append(IMUIFeatureIconModel(featureType: .camera,
                                                       UIImage.imuiImage(with: "input_item_camera"),
                                                       UIImage.imuiImage(with:"input_item_camera")))
    
    inputViewRightItemArr.append(IMUIFeatureIconModel(featureType: .emoji,
                                                      UIImage.imuiImage(with: "input_item_emoji"),
                                                      UIImage.imuiImage(with:"input_item_emoji")))
    
    inputViewRightItemArr.append(IMUIFeatureIconModel(featureType: .none,
                                                      UIImage.imuiImage(with: "input_item_send"),
                                                      UIImage.imuiImage(with:"input_item_send_message_selected"),
                                                      0,
                                                      false))
  }
  
}

extension IMUIInputView: IMUIInputViewDataSource {
  
  func imuiInputView(_ inputBarItemListView: UICollectionView, numberForItemAt position: IMUIInputViewItemPosition) -> Int {
    switch position {
    case .right:
      return self.inputViewRightItemArr.count
    case .bottom:
      return self.inputViewBottomItemArr.count
    default:
      return 0
    }
  }
  
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                     _ position: IMUIInputViewItemPosition,
                     sizeForIndex indexPath: IndexPath) -> CGSize {
    return CGSize(width: 20, height: 20)
  }
  
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                     _ position: IMUIInputViewItemPosition,
                     cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    
    var dataArr:[IMUIFeatureIconModel]
    switch position {
    case .bottom:
      dataArr = self.inputViewBottomItemArr
    case .right:
      dataArr = self.inputViewRightItemArr
    default:
      dataArr = self.inputViewLeftItemArr
    }
    
    let cellIdentifier = "IMUIFeatureListIconCell"
    let cell = inputBarItemListView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! IMUIFeatureListIconCell
    cell.layout(with: dataArr[indexPath.item],onClickCallback: { cell in
      print("click")
      self.currentType = cell.featureData!.featureType
      self.showFeatureView()
      self.reloadFeaturnView()
      
      switch cell.featureData!.featureType {
      case .none:
        //            self.delegate?.onClickSend?(with: cell)
        break
      default:
        //            self.delegate?.onSelectedFeature?(with: cell)
        break
      }
    })
    return cell
  }
  
  func imuiInputView(_ featureView: UICollectionView,
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
      break
    case .gallery:
      CellIdentifier = "IMUIGalleryContainerCell"
      break
    default:
      break
    }
    var cell = featureView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIFeatureCellProtocol
    cell.activateMedia()
    //    cell.featureDelegate = self.delegate
    return cell as! UICollectionViewCell
  }
}

extension IMUIInputView: IMUIInputViewDelegate {
  
}
