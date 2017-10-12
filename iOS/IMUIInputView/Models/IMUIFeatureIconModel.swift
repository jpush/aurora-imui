//
//  IMUIFeatureIconModel.swift
//  sample
//
//  Created by oshumini on 2017/9/22.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public class IMUIFeatureIconModel: NSObject {
  var featureType: IMUIFeatureType
  var defoultImage: UIImage?
  var selectedImage: UIImage?
  
  // send button
  public var photoCount: Int?
  public var isAllowToSend: Bool?
  
  public init(featureType: IMUIFeatureType,
       _ defoultImage: UIImage?,
       _ selectedImage: UIImage?,
       _ photoCount: Int?,
       _ isAllowToSend: Bool?) {
    self.featureType = featureType
    self.defoultImage = defoultImage
    self.selectedImage = selectedImage
    self.photoCount = photoCount
    self.isAllowToSend = isAllowToSend
    super.init()
  }
  
  convenience init(featureType: IMUIFeatureType,
                   _ defoultImage: UIImage?,
                   _ selectedImage: UIImage?) {
    self.init(featureType: featureType, defoultImage, selectedImage, nil, nil)
  }
}
