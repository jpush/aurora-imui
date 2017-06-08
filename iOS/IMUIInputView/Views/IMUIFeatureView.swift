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

public enum IMUIFeatureType {
  case voice
  case gallery
  case camera
  case location
  case none
}

protocol IMUIFeatureViewDelegate: NSObjectProtocol {
  
  func didSelectPhoto(with images: [UIImage])
  func didRecordVoice(with voiceData: Data)
  func didShotPicture(with image: UIImage)
  func didRecordVideo(with voiceData: Data)
  func didChangeSelectedGallery(with gallerys: [PHAsset])
}

extension IMUIFeatureViewDelegate {
  func didSelectPhoto(with images: [UIImage]) {}
  func didRecordVoice(with voiceData: Data) {}
  func didShotPicture(with image: UIImage) {}
  func didRecordVideo(with voiceData: Data) {}
  func didChangeSelectedGallery() {}
}

public protocol IMUIFeatureCellProtocal {
  var inputViewDelegate: IMUIInputViewDelegate? { set get }
  func activateMedia()
  func inactivateMedia()
}

public extension IMUIFeatureCellProtocal {
  var inputViewDelegate: IMUIInputViewDelegate? {
    
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
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  weak var delegate: IMUIFeatureViewDelegate?
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    self.setupAllViews()
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
    self.featureCollectionView.register(UINib(nibName: "IMUIGalleryCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIGalleryCell")
    self.featureCollectionView.register(UINib(nibName: "IMUICameraCell", bundle: bundle), forCellWithReuseIdentifier: "IMUICameraCell")
    
    self.featureCollectionView.delegate = self
    self.featureCollectionView.dataSource = self
    
    self.featureCollectionView.reloadData()
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
  
  func layoutToNone() {
    self.featureCollectionView.reloadData()
  }
  
  func clearAllSelectedGallery() {
    if currentType != .gallery { return }
    IMUIGalleryDataManager.selectedAssets = [PHAsset]()
    self.featureCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource {
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    switch currentType {
    case .gallery:
      return IMUIGalleryDataManager.allAssets.count
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
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    switch currentType {
    case .gallery:
      let minWidth = min(self.featureCollectionView.imui_size.width, self.featureCollectionView.imui_size.height)
      return CGSize(width: minWidth, height: minWidth)
    default:
      return self.featureCollectionView.imui_size
    }
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    
    switch currentType {
    case .voice:
      CellIdentifier = "IMUIRecordVoiceCell"
    case .camera:
      CellIdentifier = "IMUICameraCell"
      break
    case .location:
      break
    case .gallery:
        CellIdentifier = "IMUIGalleryCell"
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIGalleryCell
        cell.asset = IMUIGalleryDataManager.allAssets[indexPath.row]
        return cell
    default:
      break
    }
    var cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIFeatureCellProtocal
    cell.activateMedia()
    cell.inputViewDelegate = self.inputViewDelegate
    return cell as! UICollectionViewCell
  }
  
  public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
    let cell = collectionView.cellForItem(at: indexPath)!
    if cell is IMUIGalleryCell {
      let galleryCell = cell as! IMUIGalleryCell
      galleryCell.clicked()
      self.updateSelectedAssets()
    }
  }
  
  public func collectionView(_ collectionView: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
    let endDisplayingCell = didEndDisplaying as! IMUIFeatureCellProtocal
    endDisplayingCell.inactivateMedia()
  }
  
  func updateSelectedAssets() {
    self.delegate?.didChangeSelectedGallery(with: IMUIGalleryDataManager.selectedAssets)
  }
  
}
