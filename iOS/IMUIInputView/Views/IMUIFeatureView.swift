//
//  IMUIFeatureView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import MobileCoreServices
import Photos

private var CellIdentifier = ""



public protocol IMUIFeatureCellProtocol {
  
  var featureDelegate: IMUIFeatureViewDelegate? { set get }
  func activateMedia()
  func inactivateMedia()
}

public extension IMUIFeatureCellProtocol {

  var featureDelegate: IMUIFeatureViewDelegate? {
    
    get { return nil }
    set { }
  }
  
  func activateMedia() {}
  func inactivateMedia() {}
}

// TODO: Need to  Restructure
open class IMUIFeatureView: UIView {
  @IBOutlet open weak var featureCollectionView: UICollectionView!
  
  var view: UIView!
  var currentType:IMUIFeatureType = .none
  
//  open weak var inputViewDelegate: IMUIInputViewDelegate?
//  weak var delegate: IMUIFeatureViewDelegate?
  weak var dataSource: IMUICustomInputViewDataSource?
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    self.setupAllViews()
//    self.addPhotoObserver()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIFeatureView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  func setupAllViews() {
    let bundle = Bundle.imuiInputViewBundle()
    
    self.featureCollectionView.register(UINib(nibName: "IMUIRecordVoiceCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIRecordVoiceCell")
    self.featureCollectionView.register(UINib(nibName: "IMUIGalleryContainerCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIGalleryContainerCell")
    self.featureCollectionView.register(UINib(nibName: "IMUICameraCell", bundle: bundle), forCellWithReuseIdentifier: "IMUICameraCell")
    self.featureCollectionView.register(UINib(nibName: "IMUIEmojiCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIEmojiCell")
    
    self.featureCollectionView.delegate = self
    self.featureCollectionView.dataSource = self
    
    self.featureCollectionView.reloadData()
  }
  
  public func register(_ cellClass: AnyClass?,forCellWithReuseIdentifier identifier: String) {
    self.featureCollectionView.register(cellClass, forCellWithReuseIdentifier: identifier)
  }
  
  public func register(_ nib: UINib?, forCellWithReuseIdentifier identifier: String) {
    self.featureCollectionView.register(nib, forCellWithReuseIdentifier: identifier)
  }
  
  open func layoutFeature(with type: IMUIFeatureType) {
    if currentType == type {
      return
    }
    
    currentType = type
    
    switch type {
    case .voice:
      self.layoutFeatureToRecordVoice()
      break
    case .camera:
      self.layoutTocCamera()
      break
    case .gallery:
      self.layoutToGallery()
      break
    case .emoji:
      self.layoutToEmoji()
      break
    case .none:
      self.layoutToNone()
      break
    default:
      break
    }
  }
  
  func layoutFeatureToRecordVoice() {
    self.featureCollectionView.bounces = false
    self.featureCollectionView.reloadData()
    
  }
  
  func layoutToGallery() {
    self.featureCollectionView.bounces = true
    self.featureCollectionView.reloadData()
  }
  
  func layoutTocCamera() {
    self.featureCollectionView.bounces = false
    self.featureCollectionView.reloadData()
  }
  
  func layoutToEmoji() {
    self.featureCollectionView.bounces = true
    self.featureCollectionView.reloadData()
  }
  
  func layoutToNone() {
    self.featureCollectionView.reloadData()
  }
  
  func clearAllSelectedGallery() {
    IMUIGalleryDataManager.selectedAssets = [PHAsset]()
    self.featureCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return 1
    switch currentType {
    case .none:
      return 0
    default:
      return 1
    }
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    collectionView.collectionViewLayout.invalidateLayout()
    return 1
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAt indexPath: IndexPath) -> CGSize {
    switch currentType {
      default:
        return self.featureCollectionView.imui_size
    }
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {

    return self.dataSource?.imuiInputView(collectionView, cellForItem: indexPath) ?? UICollectionViewCell()
  }
  
  public func collectionView(_ collectionView: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
    let endDisplayingCell = didEndDisplaying as! IMUIFeatureCellProtocol
    endDisplayingCell.inactivateMedia()
  }
  
  func updateSelectedAssets() {
//    self.delegate?.didChangeSelectedGallery(with: IMUIGalleryDataManager.selectedAssets)
  }
}
