//
//  IMUIFeatureListView.swift
//  sample
//
//  Created by oshumini on 2017/9/20.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
@objc public protocol IMUIFeatureListDelegate: NSObjectProtocol {

  @objc optional func onSelectedFeature(with cell: IMUIFeatureListIconCell)
  @objc optional func onClickSend(with cell: IMUIFeatureListIconCell)
}



class IMUIFeatureListView: UIView {


  @IBOutlet var view: UIView!
  @IBOutlet weak var featureListCollectionView: UICollectionView!
  
  open weak var delegate:  IMUIFeatureListDelegate?
  
  var featureListDataSource:[IMUIFeatureIconModel] = [IMUIFeatureIconModel]()
  
  open var currentFeature: IMUIFeatureType = .none
  
  open override func awakeFromNib() {
    super.awakeFromNib()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    let bundle = Bundle.imuiBundle()
    view = bundle.loadNibNamed("IMUIFeatureListView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    
    self.setupAllData()
    self.setupAllViews() 
  }
  
  func setupAllData() {
    
    featureListDataSource.append(IMUIFeatureIconModel(featureType: .voice,
                                                      UIImage.imuiImage(with: "input_item_mic"),
                                                      UIImage.imuiImage(with:"input_item_mic")))
    
    featureListDataSource.append(IMUIFeatureIconModel(featureType: .gallery,
                                                      UIImage.imuiImage(with: "input_item_photo"),
                                                      UIImage.imuiImage(with:"input_item_photo")))
    
    featureListDataSource.append(IMUIFeatureIconModel(featureType: .camera,
                                                      UIImage.imuiImage(with: "input_item_camera"),
                                                      UIImage.imuiImage(with:"input_item_camera")))
    
    featureListDataSource.append(IMUIFeatureIconModel(featureType: .emoji,
                                                      UIImage.imuiImage(with: "input_item_emoji"),
                                                      UIImage.imuiImage(with:"input_item_emoji")))
    
    featureListDataSource.append(IMUIFeatureIconModel(featureType: .none,
                                                      UIImage.imuiImage(with: "input_item_send"),
                                                      UIImage.imuiImage(with:"input_item_send_message_selected"),
                                                      0,
                                                      false))
  }
  
  func setupAllViews() {
    
    let bundle = Bundle.imuiInputViewBundle()
    self.featureListCollectionView.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    self.featureListCollectionView.delegate = self
    self.featureListCollectionView.dataSource = self
    self.layoutFeatureListToCenter()
  }
  
  func layoutFeatureListToCenter() {
    self.featureListCollectionView.reloadData()
    var insets = self.featureListCollectionView.contentInset
    let frameWidth = self.view.imui_width
    let totalCellWidth = CGFloat(self.featureListDataSource.count * 46)
    let totalCellSpace = CGFloat((self.featureListDataSource.count - 1) * 15)
    
    var leftInsets = (frameWidth - totalCellSpace - totalCellWidth)/2
    if leftInsets <= 0 {
      leftInsets = 0
    }
    
    insets.left = leftInsets
    self.featureListCollectionView.contentInset = insets
  }
  
  override var bounds: CGRect {
    didSet {
      self.layoutFeatureListToCenter()
    }
  }
  
  public func updateSendButton(with count: Int?, isAllowToSend: Bool?) {
    featureListDataSource.last?.isAllowToSend = isAllowToSend
    featureListDataSource.last?.photoCount = count
    self.featureListCollectionView.reloadData()
  }

}

extension IMUIFeatureListView: UICollectionViewDelegate {

}

extension IMUIFeatureListView: UICollectionViewDataSource {
  
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return self.featureListDataSource.count
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    collectionView.collectionViewLayout.invalidateLayout()
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    return CGSize(width: 46, height: 46)
  }
  
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    var cellIdentifier = "IMUIFeatureListIconCell"
    let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! IMUIFeatureListIconCell
    cell.layout(with: self.featureListDataSource[indexPath.item],onClickCallback: { cell in
      switch cell.featureData!.featureType {
        case .none:
          self.delegate?.onClickSend?(with: cell)
        break
        default:
          self.delegate?.onSelectedFeature?(with: cell)
        break
      }
    })
    return cell
  }
  
}
