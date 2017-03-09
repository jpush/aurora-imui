//
//  IMUIFeatureView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

enum IMUIFeatureType {
  case recorder
  case gallery
  case camera
  case location
}

@objc protocol IMUIFeatureViewDelegate: NSObjectProtocol {
  
  @objc func didSelectPhoto(with images: [UIImage])
  @objc func didRecordVoice(with voiceData: Data)
  @objc func didShotPicture(with image: UIImage)
  @objc func didRecordVideo(with voiceData: Data)
}

class IMUIFeatureView: UIView {
  
  var view: UIView!
  @IBOutlet weak var showGalleryBtn: UIButton!
  
  var currentType: IMUIFeatureType?
  var delegate: IMUIFeatureViewDelegate?
  
  @IBOutlet weak var featureCollectionView: UICollectionView!
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    self.featureCollectionView.delegate = self
    self.featureCollectionView.dataSource = self
  }
  
  required init?(coder aDecoder: NSCoder) {
    
    super.init(coder: aDecoder)
    
    view = Bundle.main.loadNibNamed("IMUIFeatureView", owner: self, options: nil)?[0] as! UIView
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  open func layoutFeature(with type: IMUIFeatureType) {
    currentType = type
    
    switch type {
    case .recorder:
      self.layoutFeatureToRecordVoice()
      break
    case .camera:
      self.layoutTocCamera()
      break
    case .gallery:
      self.layoutToGallery()
      break
    default:
      break
    }
  }
  
  func layoutFeatureToRecordVoice() {
    self.showGalleryBtn.isHidden = true
    self.featureCollectionView.bounces = false
    self.featureCollectionView.reloadData()
    
  }
  
  func layoutToGallery() {
    self.showGalleryBtn.isHidden = false
    self.featureCollectionView.bounces = true
    self.featureCollectionView.reloadData()
  }
  
  func layoutTocCamera() {
    self.showGalleryBtn.isHidden = true
    self.featureCollectionView.bounces = false
    self.featureCollectionView.reloadData()
  }
}


extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return 0
  }
  
  func numberOfSections(in collectionView: UICollectionView) -> Int {
    return 1
  }
  
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    
    return CGSize.zero
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    return UICollectionViewCell()
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      didSelectItemAt indexPath: IndexPath) {
    
  }
}
