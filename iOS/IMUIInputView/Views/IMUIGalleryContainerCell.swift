//
//  IMUIGalleryContainerCell.swift
//  sample
//
//  Created by oshumini on 2017/10/12.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos
private var CellIdentifier = ""

class IMUIGalleryContainerCell: UICollectionViewCell, IMUIFeatureCellProtocol {
  
  var featureDelegate: IMUIFeatureViewDelegate?
  
  
  @IBOutlet weak var permissionDenyedView: IMUIPermissionDenyedView!
  @IBOutlet weak var galleryCollectionView: UICollectionView!
  
  override func awakeFromNib() {
    super.awakeFromNib()
    self.setupAllView()
    self.addPhotoObserver()
  }
  
  func addPhotoObserver() {
    PHPhotoLibrary.requestAuthorization { (status) in
      DispatchQueue.main.async {
        switch status {
        case .authorized:
          PHPhotoLibrary.shared().register(self)
          self.permissionDenyedView.isHidden = true
          break
        default:
          self.permissionDenyedView.type = "相册"
          self.permissionDenyedView.isHidden = false
          break
        }
      }
    }
  }

  func setupAllView() {
    let bundle = Bundle.imuiInputViewBundle()
    self.galleryCollectionView.register(UINib(nibName: "IMUIGalleryCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIGalleryCell")
    self.galleryCollectionView.delegate = self
    self.galleryCollectionView.dataSource = self
    self.galleryCollectionView.reloadData()
  }
  
  func activateMedia() {
    self.galleryCollectionView.reloadDataHorizontalNoScroll()
    if IMUIGalleryDataManager.allAssets.count > 0 {
//      self.galleryCollectionView.scrollToItem(at: IndexPath(item: 0, section: 0), at: .left, animated: false)
    }
  }
  
  func inactivateMedia() {
  }
}

extension IMUIGalleryContainerCell: UICollectionViewDelegate {
  
}

extension IMUIGalleryContainerCell: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
  
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return IMUIGalleryDataManager.allAssets.count
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    collectionView.collectionViewLayout.invalidateLayout()
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAt indexPath: IndexPath) -> CGSize {
    
    var galleryHeight = 254
    return CGSize(width: galleryHeight, height: galleryHeight)
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    CellIdentifier = "IMUIGalleryCell"
    let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath) as! IMUIGalleryCell
    cell.asset = IMUIGalleryDataManager.allAssets[indexPath.row]
    return cell
  }
  
  public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
    let cell = collectionView.cellForItem(at: indexPath)
    if cell is IMUIGalleryCell {
      let galleryCell = cell as! IMUIGalleryCell
      galleryCell.clicked()
      self.featureDelegate?.didSelectPhoto(with: [])
    }
  }
}

extension IMUIGalleryContainerCell: PHPhotoLibraryChangeObserver {
  public func photoLibraryDidChange(_ changeInstance: PHChange) {
    DispatchQueue.global(qos: .background).async {
      IMUIGalleryDataManager.updateAssets()
      
      DispatchQueue.main.async {
        self.galleryCollectionView.reloadData()
      }
    }
    
  }
}
