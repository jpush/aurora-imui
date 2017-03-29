//
//  IMUIFeatureView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

private var CellIdentifier = ""

enum IMUIFeatureType {
  case voice
  case gallery
  case camera
  case location
  case none
}

@objc protocol IMUIFeatureViewDelegate: NSObjectProtocol {
  
  @objc func didSelectPhoto(with images: [UIImage])
  @objc func didRecordVoice(with voiceData: Data)
  @objc func didShotPicture(with image: UIImage)
  @objc func didRecordVideo(with voiceData: Data)
}

protocol IMUIFeatureCellProtocal {
  var inputViewDelegate: IMUIInputViewDelegate? { set get }
}

class IMUIFeatureView: UIView, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
  @IBOutlet weak var showGalleryBtn: UIButton!
  @IBOutlet weak var featureCollectionView: UICollectionView!
  
    @IBAction func showGalleryBtnPressed(_ sender: Any) {
        let imagePickerController = UIImagePickerController()
        imagePickerController.delegate = self
        imagePickerController.allowsEditing = true
        imagePickerController.sourceType = UIImagePickerControllerSourceType.photoLibrary
        self.rootViewController.present(imagePickerController, animated: true) {
        }
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let image = info[UIImagePickerControllerOriginalImage] as! UIImage
        self.inputViewDelegate?.sendPhotoMessage!([image])
    }

    var rootViewController : UIViewController {
        get{
            let appRootVC = UIApplication.shared.keyWindow?.rootViewController
            var topVC = appRootVC
            while (topVC?.presentedViewController != nil) {
                topVC = topVC?.presentedViewController
            }
            return topVC!
        }
    }

  var view: UIView!

  var currentType:IMUIFeatureType = .none
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
    
  open override func awakeFromNib() {
    super.awakeFromNib()
    self.setupAllViews()
  }
  
  
  required init?(coder aDecoder: NSCoder) {
    
    super.init(coder: aDecoder)
    
    view = Bundle.main.loadNibNamed("IMUIFeatureView", owner: self, options: nil)?[0] as! UIView
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  func setupAllViews() {
    self.featureCollectionView.delegate = self
    self.featureCollectionView.dataSource = self
    self.featureCollectionView.register(UINib(nibName: "IMUIRecordVoiceCell", bundle: nil), forCellWithReuseIdentifier: "IMUIRecordVoiceCell")
    self.featureCollectionView.register(UINib(nibName: "IMUIGalleryCell", bundle: nil), forCellWithReuseIdentifier: "IMUIGalleryCell")
    self.featureCollectionView.register(UINib(nibName: "IMUICameraCell", bundle: nil), forCellWithReuseIdentifier: "IMUICameraCell")
    self.showGalleryBtn.isHidden = true
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
  
  func layoutToNone() {
    self.showGalleryBtn.isHidden = true
    self.featureCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    switch currentType {
    case .gallery:
      return IMUIGalleryDataManager.allAssets.count
    case .none:
      return 0
    default:
      return 1
    }
  }
  
  func numberOfSections(in collectionView: UICollectionView) -> Int {
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    switch currentType {
    case .gallery:
        return self.featureCollectionView.imui_size
    default:
      return self.featureCollectionView.imui_size
    }
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  func collectionView(_ collectionView: UICollectionView,
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
    cell.inputViewDelegate = self.inputViewDelegate!
    return cell as! UICollectionViewCell
  }
  
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath)!
        if cell is IMUIGalleryCell {
            let galleryCell = cell as! IMUIGalleryCell
            galleryCell.clicked()
        }
    }
}
