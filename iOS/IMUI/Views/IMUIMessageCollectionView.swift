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
  weak var delegate: IMUIMessageMessageCollectionViewDelegate?
  
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
    IMUIMessageCellLayout.cellWidth = self.imui_width
  }
  
  func setupMessageCollectionView() {
    self.messageCollectionView.delegate = self
    self.messageCollectionView.dataSource = self
    
    self.messageCollectionView.register(IMUITextMessageCell.self, forCellWithReuseIdentifier: IMUITextMessageCell.self.description())
    self.messageCollectionView.register(IMUIImageMessageCell.self, forCellWithReuseIdentifier: IMUIImageMessageCell.self.description())
    self.messageCollectionView.register(IMUIVoiceMessageCell.self, forCellWithReuseIdentifier: IMUIVoiceMessageCell.self.description())
    self.messageCollectionView.register(IMUIVideoMessageCell.self, forCellWithReuseIdentifier: IMUIVideoMessageCell.self.description())
    
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

    var cellIdentify = ""
    let messageModel = self.chatDataManager[indexPath.item]
    
    switch messageModel.type {
    case .text:
      cellIdentify = IMUITextMessageCell.self.description()
      break
    case .image:
      cellIdentify = IMUIImageMessageCell.self.description()
      break
    case .voice:
      cellIdentify = IMUIVoiceMessageCell.self.description()
      break
    case .video:
      cellIdentify = IMUIVideoMessageCell.self.description()
      break
    default:
      break
    }
    
    let cell: IMUIMessageCellProtocal = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentify, for: indexPath) as! IMUIMessageCellProtocal
    
    cell.presentCell(with: messageModel, delegate: delegate)
    
    self.delegate?.willDisplayMessageCell(messageModel, cell: cell)
    
    return cell as! UICollectionViewCell
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      didSelectItemAt indexPath: IndexPath) {
    let messageModel = self.chatDataManager[indexPath.item]
    self.delegate?.didTapMessageCell(messageModel)
  }

  func collectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
    let messageModel = self.chatDataManager[forItemAt.item]
    self.delegate?.didEndDisplaying(messageModel, cell: didEndDisplaying)
  }
}
