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

enum IMUIFeatureType {
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

protocol IMUIFeatureCellProtocal {
  var inputViewDelegate: IMUIInputViewDelegate? { set get }
}

// TODO: Need to  Restructure
class IMUIFeatureView: UIView, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
  @IBOutlet weak var showGalleryBtn: UIButton!
  @IBOutlet weak var featureCollectionView: UICollectionView!
  
    @IBAction func showGalleryBtnPressed(_ sender: Any) {
        let imagePickerController = UIImagePickerController()
        imagePickerController.delegate = self
        imagePickerController.allowsEditing = true
        imagePickerController.mediaTypes = [kUTTypeImage as String, kUTTypeMovie as String]
        self.rootViewController.present(imagePickerController, animated: true) {
        }
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let type = info[UIImagePickerControllerMediaType] as! String
        if type == (kUTTypeImage as String) {
            let data = UIImagePNGRepresentation(info[UIImagePickerControllerOriginalImage] as! UIImage)
            self.inputViewDelegate?.finishSelectedPhoto([data!])
        }else if type == (kUTTypeMovie as String){
            let url = info[UIImagePickerControllerMediaURL] as! URL
            self.inputViewDelegate?.finishSelectedVideo([url])
        }
        picker.dismiss(animated: true, completion: nil)
    }

    var rootViewController : UIViewController {
      let appRootVC = UIApplication.shared.keyWindow?.rootViewController
      var topVC = appRootVC
      while (topVC?.presentedViewController != nil) {
        topVC = topVC?.presentedViewController
      }
      return topVC!
    }

  var view: UIView!

  var currentType:IMUIFeatureType = .none
  
  open weak var inputViewDelegate: IMUIInputViewDelegate?
  
  weak var delegate: IMUIFeatureViewDelegate?
  
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

    self.featureCollectionView.register(UINib(nibName: "IMUIRecordVoiceCell", bundle: nil), forCellWithReuseIdentifier: "IMUIRecordVoiceCell")
    self.featureCollectionView.register(UINib(nibName: "IMUIGalleryCell", bundle: nil), forCellWithReuseIdentifier: "IMUIGalleryCell")
    self.featureCollectionView.register(UINib(nibName: "IMUICameraCell", bundle: nil), forCellWithReuseIdentifier: "IMUICameraCell")
    self.showGalleryBtn.isHidden = true
    
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
  
  func clearAllSelectedGallery() {
    if currentType != .gallery { return }
    IMUIGalleryDataManager.selectedAssets = [PHAsset]()
    self.featureCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIFeatureView: UICollectionViewDelegate, UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    switch currentType {
    case .gallery:
      return IMUIGalleryDataManager.allAssets.count
      return 0
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
      self.updateSelectedAssets()
    }
  }
  
  func updateSelectedAssets() {
    self.delegate?.didChangeSelectedGallery(with: IMUIGalleryDataManager.selectedAssets)
  }
  
}
