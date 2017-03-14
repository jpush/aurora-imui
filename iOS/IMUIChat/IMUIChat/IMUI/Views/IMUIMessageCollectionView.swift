//
//  IMUIMessageCollectionView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIMessageCollectionView: UIView {

  @IBOutlet var view: UIView!
  @IBOutlet weak var messageCollectionView: UICollectionView!

  var chatDataManager = IMUIChatDataManager()
  
  
  open override func awakeFromNib() {
    super.awakeFromNib()
    
  }
  
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    view = Bundle.main.loadNibNamed("IMUIMessageCollectionView", owner: self, options: nil)?[0] as! UIView
    self.addSubview(view)
    view.frame = self.bounds
    self.chatDataManager = IMUIChatDataManager()
    self.setupMessageCollectionView()
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    IMUIMessageCellLayout.CellWidth = self.imui_width
    IMUIMessageCellLayout.avatarSize = CGSize(width: 40, height: 40)
    IMUIMessageCellLayout.bubbleMaxWidth = 200
    
  }
  
  func setupMessageCollectionView() {
    self.messageCollectionView.delegate = self
    self.messageCollectionView.dataSource = self
    
    self.messageCollectionView.register(IMUIBaseMessageCell.self, forCellWithReuseIdentifier: IMUIBaseMessageCell.self.description())
    self.messageCollectionView.isScrollEnabled = true
  }
  
  open func appendMessage(with message: IMUIMessageModel) {
    self.chatDataManager.appendMessage(with: message)
    self.messageCollectionView.reloadData()
    let endIndex = IndexPath(item: chatDataManager.endIndex - 1, section: 0)
    self.messageCollectionView.scrollToItem(at: endIndex, at: .bottom, animated: true)
  }
  
  open func insertMessage(with message: IMUIMessageModel) {
    self.chatDataManager.insertMessage(with: message)
    self.messageCollectionView.reloadData()
  }
  
  open func insertMessages(with messages:[IMUIMessageModel]) {
    self.chatDataManager.insertMessages(with: messages)
    self.messageCollectionView.reloadData()
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIMessageCollectionView: UICollectionViewDelegate, UICollectionViewDataSource {
  
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return self.chatDataManager.count
  }
  
  func numberOfSections(in collectionView: UICollectionView) -> Int {
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    
    return CGSize(width: messageCollectionView.imui_width, height: chatDataManager[indexPath.item].layout.cellHeight)
    
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {

    let cell = collectionView.dequeueReusableCell(withReuseIdentifier: IMUIBaseMessageCell.self.description(), for: indexPath) as! IMUIBaseMessageCell
    cell.presentCell(with: self.chatDataManager[indexPath.item])
    return cell
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      didSelectItemAt indexPath: IndexPath) {
    
  }

}
