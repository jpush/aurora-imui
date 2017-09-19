//
//  IMUICameraCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos
import AVFoundation


private enum SessionSetupResult {
		case success
		case notAuthorized
		case configurationFailed
}

private enum LivePhotoMode {
		case on
		case off
}


// TODO: Need to Restructure
@available(iOS 8.0, *)
class IMUICameraCell: UICollectionViewCell, IMUIFeatureCellProtocal {
  
  @IBOutlet weak var cameraView: IMUICameraView!
  
//  var inConfiging: Bool = false
//  var currentCameraDeviceType: AVCaptureDevicePosition = .back
//  
//  var currentCameraDevice: AVCaptureDeviceInput?
  
  weak var delegate: IMUIInputViewDelegate?
  
  var isActivity = true
  
  var inputViewDelegate: IMUIInputViewDelegate? {
    set {
      self.delegate = newValue
    }
    
    get {
      return self.delegate
    }
  }
  
  override func awakeFromNib() {
    super.awakeFromNib()
    cameraView.recordVideoCallback = {(path, duration) in
      self.inputViewDelegate?.finishRecordVideo?(videoPath: path, durationTime: duration)
    }
    
    cameraView.shootPictureCallback = { imageData in
      self.inputViewDelegate?.didShootPicture?(picture: imageData)
    }
  }
  
  func activateMedia() {
    isActivity = true
    self.cameraView.activateMedia()
  }
  
  func inactivateMedia() {
    self.cameraView.inactivateMedia()
  }
}
