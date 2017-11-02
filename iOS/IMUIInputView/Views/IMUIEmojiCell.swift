//
//  IMUIEmojiCell.swift
//  sample
//
//  Created by oshumini on 2017/9/26.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIEmojiCell: UICollectionViewCell, IMUIFeatureCellProtocol {
  var featureDelegate: IMUIFeatureViewDelegate?
  
  @IBOutlet weak var emojiCollectionView: UICollectionView!
  @IBOutlet weak var emojiSeletorView: UICollectionView!

  var emojiDataArr = [IMUIEmojiModel]()
  
  override func awakeFromNib() {
    super.awakeFromNib()
    let bundle = Bundle.imuiInputViewBundle()
    self.emojiCollectionView.register(UINib(nibName: "IMUIEmojiItemCell", bundle: bundle), forCellWithReuseIdentifier: "IMUIEmojiItemCell")
    emojiCollectionView.delegate = self
    emojiCollectionView.dataSource = self
    emojiCollectionView.contentInset = UIEdgeInsets(top: 0, left: 16, bottom: 10, right: 16)
    self.setupAllData(identifier: "com.apple.emoji")
    self.emojiCollectionView.reloadData()
  }
  
  func setupAllData(identifier: String) {
    let bundle = Bundle.imuiInputViewBundle()
    guard let path = bundle.path(forResource: "IMUIInputViewAssets.bundle/\(identifier)/Info", ofType: "plist") else {
      return
    }
    
    guard let dic = NSDictionary(contentsOfFile: path), let arr = dic["emoticons"] as? NSArray else {
      return
    }
    
    self.emojiDataArr = arr.map { (item) -> IMUIEmojiModel in
      let dic = item as! NSDictionary
      return IMUIEmojiModel(emojiType: .emoji, emoji: (dic.object(forKey: "contents") as! String))
    }
  }
  
  func activateMedia() {
    
  }
  
  func inactivateMedia() {
    
  }
}

extension IMUIEmojiCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
  
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return self.emojiDataArr.count
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAt indexPath: IndexPath) -> CGSize {
    return CGSize(width: 30, height: 30)
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    let model = self.emojiDataArr[indexPath.item]
    
    var cell = collectionView.dequeueReusableCell(withReuseIdentifier: "IMUIEmojiItemCell", for: indexPath) as! IMUIEmojiItemCell
    cell.layout(with: model)
    return cell
  }
  
  public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
//    self.delegate?.didSeletedEmoji?(emoji: self.emojiDataArr[indexPath.item])
    self.featureDelegate?.didSeletedEmoji(with: self.emojiDataArr[indexPath.item])
  }
  
  public func collectionView(_ collectionView: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
  }
  
}

