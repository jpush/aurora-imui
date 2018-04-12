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


//fileprivate var featureListMargin = 16.0
//fileprivate var featureListMargin = 8.0
//fileprivate var featureListBtnWidth = 46
//fileprivate var featureListBtnWidth = 28

open class IMUIFeatureListView: UIView {
  public static var featureListItemSpace: CGFloat = 8.0
  public static var featureListBtnWidth = 28
  
  @IBOutlet var view: UIView!
  @IBOutlet open weak var featureListCollectionView: UICollectionView!
  
  public weak var delegate:  IMUIFeatureListDelegate?
  
  fileprivate weak var _dataSource: IMUIInputViewDataSource?
  public weak var dataSource: IMUIInputViewDataSource? {
    set {
      self._dataSource = newValue
      self.layoutFeatureListToCenter()
    }
    
    get {
      return self._dataSource
    }
  }
  
  var featureListDataSource:[IMUIFeatureIconModel] = [IMUIFeatureIconModel]()
  
//  open var currentFeature: IMUIFeatureType = .none
  public var position: IMUIInputViewItemPosition?
  
  public var totalWidth: CGFloat {
    if self.position == .bottom {
      return self.bounds.size.width
    }
    
    let itemCount = self.dataSource?.imuiInputView(self.featureListCollectionView, numberForItemAt: self.position!) ?? 0
    var totalCellWidth: CGFloat = 0
    for index in 0..<itemCount {
      let indexPath = IndexPath(item: index, section: 0)
      let size = self.dataSource?.imuiInputView(self.featureListCollectionView, self.position!, sizeForIndex: indexPath) ?? CGSize.zero
      totalCellWidth += size.width
    }
    return totalCellWidth + IMUIFeatureListView.featureListItemSpace * CGFloat(itemCount + 1)
  }
  
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
    
//    featureListDataSource.append(IMUIFeatureIconModel(featureType: .camera,
//                                                      UIImage.imuiImage(with: "input_item_camera"),
//                                                      UIImage.imuiImage(with:"input_item_camera")))
//
//    featureListDataSource.append(IMUIFeatureIconModel(featureType: .emoji,
//                                                      UIImage.imuiImage(with: "input_item_emoji"),
//                                                      UIImage.imuiImage(with:"input_item_emoji")))
//
//    featureListDataSource.append(IMUIFeatureIconModel(featureType: .none,
//                                                      UIImage.imuiImage(with: "input_item_send"),
//                                                      UIImage.imuiImage(with:"input_item_send_message_selected"),
//                                                      0,
//                                                      false))
  }
  
  func setupAllViews() {
    
    let bundle = Bundle.imuiInputViewBundle()
    self.featureListCollectionView.register(UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIFeatureListIconCell")
    self.featureListCollectionView.delegate = self
    self.featureListCollectionView.dataSource = self
    
    
  }
  
  
  
  func layoutFeatureListToCenter() {
    self.featureListCollectionView.reloadData()
    var insets = self.featureListCollectionView.contentInset
    let frameWidth = self.view.imui_width
//    let totalCellWidth = CGFloat(self.featureListDataSource.count * IMUIFeatureListView.featureListBtnWidth)
    
    let itemCount = self.dataSource?.imuiInputView(self.featureListCollectionView, numberForItemAt: self.position!) ?? 0
    var totalCellWidth: CGFloat = 0
    for index in 0..<itemCount {
      let indexPath = IndexPath(item: index, section: 0)
      let size = self.dataSource?.imuiInputView(self.featureListCollectionView, self.position!, sizeForIndex: indexPath) ?? CGSize.zero
      totalCellWidth += size.width
    }
    
    let spaceWidth = (frameWidth - CGFloat(IMUIFeatureListView.featureListItemSpace * 2) - totalCellWidth) / CGFloat(itemCount - 1)
    print("frameWidth :\(frameWidth)  spaceWidth: \(spaceWidth)")
    (self.featureListCollectionView.collectionViewLayout as! UICollectionViewFlowLayout).minimumLineSpacing = spaceWidth
    
    insets.left = CGFloat(IMUIFeatureListView.featureListItemSpace)
    self.featureListCollectionView.contentInset = insets
  }
  
  override open var bounds: CGRect {
    didSet {
      self.layoutFeatureListToCenter()
    }
  }
  
  public func register(_ cellClass: AnyClass?,forCellWithReuseIdentifier identifier: String) {
    self.featureListCollectionView.register(cellClass, forCellWithReuseIdentifier: identifier)
  }
  
  public func register(_ nib: UINib?, forCellWithReuseIdentifier identifier: String) {
    self.featureListCollectionView.register(nib, forCellWithReuseIdentifier: identifier)
  }
  
  public func updateSendButton(with count: Int?, isAllowToSend: Bool?) {
//    featureListDataSource.last?.isAllowToSend = isAllowToSend
//    featureListDataSource.last?.photoCount = count
//    self.featureListCollectionView.reloadItems(at: [IndexPath(item: featureListDataSource.count - 1, section: 0)])
  }
  
  public func reloadData() {
      self.featureListCollectionView.reloadData()
  }
}

extension IMUIFeatureListView: UICollectionViewDelegate {

}

extension IMUIFeatureListView: UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
  
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return self.dataSource?.imuiInputView(self.featureListCollectionView, numberForItemAt: self.position!) ?? 0
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    collectionView.collectionViewLayout.invalidateLayout()
    return 1
  }

  public func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAt indexPath: IndexPath) -> CGSize {
    return self.dataSource?.imuiInputView(self.featureListCollectionView, self.position!, sizeForIndex: indexPath) ?? CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
//    let cellIdentifier = "IMUIFeatureListIconCell"
//    let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! IMUIFeatureListIconCell
//    cell.layout(with: self.featureListDataSource[indexPath.item],onClickCallback: { cell in
//      switch cell.featureData!.featureType {
//        case .none:
//          self.delegate?.onClickSend?(with: cell)
//        break
//        default:
//          self.delegate?.onSelectedFeature?(with: cell)
//        break
//      }
//    })
//    return cell
//        print("fafsdfafafasf")
        return self.dataSource?.imuiInputView(self.featureListCollectionView, self.position!, cellForItemAt: indexPath) ?? UICollectionViewCell()
    
//    return UICollectionViewCell()
  }
  
}
