//
//  IMUIFeatureListIconCell.swift
//  sample
//
//  Created by oshumini on 2017/9/20.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIFeatureListIconCell: UICollectionViewCell {

  
  @IBOutlet weak var photoNumberLabel: UILabel!
  @IBOutlet public weak var featureIconBtn: UIButton!
  open var featureData: IMUIFeatureIconModel?
  private var onClickCallback: ((IMUIFeatureListIconCell) -> ())?
  
  override public func awakeFromNib() {
    super.awakeFromNib()
    self.photoNumberLabel.layer.cornerRadius = self.photoNumberLabel.imui_width/2
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
  }
  
  public func layout(with data: IMUIFeatureIconModel,onClickCallback: @escaping (IMUIFeatureListIconCell) -> ()) {
    self.featureData = data
    self.onClickCallback = onClickCallback
    self.featureIconBtn.setImage(data.defoultImage, for: .normal)
    self.featureIconBtn.setImage(data.selectedImage, for: .selected)
    
    switch featureData!.featureType {
    case .none:
      self.photoNumberLabel.isHidden = false
      self.layoutSendIcon(with: data)
      break
    default:
      self.photoNumberLabel.isHidden = true
      break
    }
  }
  
  func layoutSendIcon(with data: IMUIFeatureIconModel) {
    if let count = data.photoCount {
      if count == 0 {
        self.photoNumberLabel.isHidden  = true
      }
      self.photoNumberLabel.text = "\(count)"
    }
    
    if let isAllowToSend = data.isAllowToSend {
        self.featureIconBtn.isSelected = isAllowToSend
    }
  }
  
  @IBAction func onClickIcon(_ sender: Any) {
    self.onClickCallback?(self)
  }
}
