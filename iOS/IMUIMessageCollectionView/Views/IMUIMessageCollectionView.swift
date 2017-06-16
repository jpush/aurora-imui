//
//  IMUIMessageCollectionView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


open class IMUIMessageCollectionView: UIView {

  @IBOutlet var view: UIView!
  @IBOutlet open weak var messageCollectionView: UICollectionView!

  var viewCache = IMUIReuseViewCache()
  
  var chatDataManager = IMUIChatDataManager()
  open weak var delegate: IMUIMessageMessageCollectionViewDelegate?
  
  open override func awakeFromNib() {
    super.awakeFromNib()
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    let bundle = Bundle.imuiBundle()
    view = bundle.loadNibNamed("IMUIMessageCollectionView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    self.chatDataManager = IMUIChatDataManager()
    self.setupMessageCollectionView()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    let bundle = Bundle.imuiBundle()
    view = bundle.loadNibNamed("IMUIMessageCollectionView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
    self.chatDataManager = IMUIChatDataManager()
    self.setupMessageCollectionView()
  }
  
  override open func layoutSubviews() {
    super.layoutSubviews()
    IMUIMessageCellLayout.cellWidth = self.imui_width
  }
  
  func setupMessageCollectionView() {

    self.messageCollectionView.delegate = self
    self.messageCollectionView.dataSource = self
    
    self.messageCollectionView.register(IMUIBaseMessageCell.self, forCellWithReuseIdentifier: IMUIBaseMessageCell.self.description())
    
    self.messageCollectionView.isScrollEnabled = true
  }
  
  open func register(_ cellClass: AnyClass?, forCellWithReuseIdentifier identifier: String) {
    self.messageCollectionView.register(cellClass, forCellWithReuseIdentifier: identifier)
  }
  
  open subscript(index: Int) -> IMUIMessageProtocol {
    return chatDataManager[index]
  }
  
  open subscript(msgId: String) -> IMUIMessageProtocol? {
    return chatDataManager[msgId]
  }
  
  open var messageCount: Int {
    return chatDataManager.count
  }
  
  open func scrollToBottom(with animated: Bool) {
    if chatDataManager.count == 0 { return }
    let endIndex = IndexPath(item: chatDataManager.endIndex - 1, section: 0)
    self.messageCollectionView.scrollToItem(at: endIndex, at: .bottom, animated: animated)
  }
  
  open func appendMessage(with message: IMUIMessageProtocol) {
    self.chatDataManager.appendMessage(with: message)
    self.messageCollectionView.reloadData()
    self.scrollToBottom(with: true)
  }
  
  open func insertMessage(with message: IMUIMessageProtocol) {
    self.chatDataManager.insertMessage(with: message)
    self.messageCollectionView.reloadData()
  }
  
  open func insertMessages(with messages:[IMUIMessageProtocol]) {
    self.chatDataManager.insertMessages(with: messages)
    self.messageCollectionView.reloadData()
  }
  
  open func updateMessage(with message:IMUIMessageProtocol) {
    self.chatDataManager.updateMessage(with: message)
    if let index = chatDataManager.index(of: message) {
      let indexPath = IndexPath(item: index, section: 0)
      self.messageCollectionView.reloadItems(at: [indexPath])
    }
  }
}

// MARK: - UICollectionViewDelegate, UICollectionViewDataSource
extension IMUIMessageCollectionView: UICollectionViewDelegate, UICollectionViewDataSource {
  
  public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return self.chatDataManager.count
  }
  
  public func numberOfSections(in collectionView: UICollectionView) -> Int {
    collectionView.collectionViewLayout.invalidateLayout()
    return 1
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
    let height = self.delegate?.messageCollectionView?(messageCollectionView: collectionView, heightForItemAtIndexPath: indexPath, messageModel: chatDataManager[indexPath.item])
    if let _ = height {
      return CGSize(width: messageCollectionView.imui_width, height: CGFloat(height!.floatValue))
    }
    
    let messageModel = chatDataManager[indexPath.item]
    if messageModel is IMUIMessageModelProtocol {
      let message = messageModel as! IMUIMessageModelProtocol
      return CGSize(width: messageCollectionView.imui_width, height: message.layout.cellHeight)
    }
    
    return CGSize.zero
  }
  
  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      referenceSizeForFooterInSection section: Int) -> CGSize {
    return CGSize.zero
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    
    var cellIdentify = ""
    let messageModel = self.chatDataManager[indexPath.item]
    cellIdentify = IMUIBaseMessageCell.self.description()

    let customCell = self.delegate?.messageCollectionView?(messageCollectionView: collectionView, forItemAt: indexPath, messageModel: messageModel)
    if let _ = customCell {
      return customCell!
    }
    
    let cell: IMUIMessageCellProtocol = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentify, for: indexPath) as! IMUIMessageCellProtocol
    
    cell.presentCell(with: messageModel as! IMUIMessageModelProtocol, viewCache: viewCache, delegate: delegate)
    self.delegate?.messageCollectionView?(collectionView,
                                         willDisplayMessageCell: cell as! UICollectionViewCell,
                                         forItemAt: indexPath,
                                         model: messageModel)
    
    return cell as! UICollectionViewCell
  }
  
  public func collectionView(_ collectionView: UICollectionView,
                      didSelectItemAt indexPath: IndexPath) {
    let messageModel = self.chatDataManager[indexPath.item]
    
    self.delegate?.messageCollectionView?(collectionView, forItemAt: indexPath, model: messageModel)
  }


  public func collectionView(_ collectionView: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath) {
    let messageModel = self.chatDataManager[forItemAt.item]
    
    if messageModel is IMUIMessageModelProtocol {
      (didEndDisplaying as! IMUIMessageCellProtocol).didDisAppearCell()
      self.delegate?.messageCollectionView?(collectionView, didEndDisplaying: didEndDisplaying, forItemAt: forItemAt, model: messageModel)
    }
    
  }
}


extension IMUIMessageCollectionView: UIScrollViewDelegate {
  public func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
    self.delegate?.messageCollectionView?(self.messageCollectionView)
  }
}
