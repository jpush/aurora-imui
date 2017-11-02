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
  case emoji
  case none
}

public protocol IMUIFeatureViewDelegate: NSObjectProtocol {
  
  func didSelectPhoto(with images: [UIImage])
  func didRecordVoice(with voicePath: String, durationTime: Double)
  func didShotPicture(with image: Data)
  func didRecordVideo(with videoPath: String, durationTime: Double)
  func didSeletedEmoji(with emoji: IMUIEmojiModel)
  func didChangeSelectedGallery(with gallerys: [PHAsset])
}

public extension IMUIFeatureViewDelegate {
  func didSelectPhoto(with images: [UIImage]) {}
  func didRecordVoice(with voicePath: String, durationTime: Double) {}
  func didShotPicture(with image: Data) {}
  func didRecordVideo(with videoPath: String, durationTime: Double) {}
  func didSeletedEmoji(with emoji: IMUIEmojiModel) {}
  func didChangeSelectedGallery() {}
}

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
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  weak var delegate: IMUIFeatureViewDelegate?
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    self.setupAllViews()
    self.addPhotoObserver()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIFeatureView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    self.addPhotoObserver()
  }
  
  func addPhotoObserver() {
    PHPhotoLibrary.requestAuthorization { (status) in
      if status == .authorized {
        PHPhotoLibrary.shared().register(self)
      }
    }
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
    if currentType != .gallery { return }
    IMUIGalleryDataManager.selectedAssets = [PHAsset]()
    self.featureCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
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
    var cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIFeatureCellProtocol
    cell.activateMedia()
    cell.featureDelegate = self.delegate
    return cell as! UICollectionViewCell
  }
  
  public func collectionView(_ collectionView: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
    let endDisplayingCell = didEndDisplaying as! IMUIFeatureCellProtocol
    endDisplayingCell.inactivateMedia()
  }
  
  func updateSelectedAssets() {
    self.delegate?.didChangeSelectedGallery(with: IMUIGalleryDataManager.selectedAssets)
  }
  
}

extension IMUIFeatureView: PHPhotoLibraryChangeObserver {
  public func photoLibraryDidChange(_ changeInstance: PHChange) {
    DispatchQueue.global(qos: .background).async {
      IMUIGalleryDataManager.updateAssets()
      
      DispatchQueue.main.async {
        self.featureCollectionView.reloadData()
      }
    }
    
  }
}
