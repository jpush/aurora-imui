//
//  IMUIGalleryDataManager.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos

class IMUIGalleryDataManager: NSObject {
  static var maxSeletedCount = 9
  
  static var targetSize = CGSize(width: 200, height: 200)
  
  private static var _allAssets = [PHAsset]()
  static var allAssets : [PHAsset] {
    get {
      if _allAssets.count < 1 {
        updateAssets()
      }
      
      return _allAssets
    }
  }
  
  class func updateAssets(){
    let options = PHFetchOptions()
    _allAssets.removeAll()
    options.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]
    let assetFetchResult = PHAsset.fetchAssets(with: options)
    assetFetchResult.enumerateObjects({(asset, _, _) in
      if asset.mediaType == .image {
        _allAssets.append(asset)
      }
    })
  }
  
  static var selectedAssets = [PHAsset]()
  
  class func insertSelectedAssets(with asset: PHAsset) -> Bool {
    if IMUIGalleryDataManager.selectedAssets.count < IMUIGalleryDataManager.maxSeletedCount {
      IMUIGalleryDataManager.selectedAssets.append(asset)
      return true
    } else {
      return false
    }
  }
}
