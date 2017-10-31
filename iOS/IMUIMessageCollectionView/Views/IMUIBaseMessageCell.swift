//
//  IMUIBaseMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


enum IMUIMessageCellType {
  case incoming
  case outgoing
}

open class IMUIBaseMessageCell: UICollectionViewCell, IMUIMessageCellProtocol {
  @objc open static var avatarCornerRadius:CGFloat = 0
  
  var bubbleView: IMUIMessageBubbleView
  lazy var avatarImage = UIImageView()
  lazy var timeLabel = UILabel()
  lazy var nameLabel = UILabel()
  
  weak var statusView: UIView?
  weak var bubbleContentView: IMUIMessageContentViewProtocol?
  var bubbleContentType = ""
  
  weak var delegate: IMUIMessageMessageCollectionViewDelegate?
  var message: IMUIMessageModelProtocol?
  
  override init(frame: CGRect) {

    bubbleView = IMUIMessageBubbleView(frame: CGRect.zero)
    super.init(frame: frame)
    
    self.contentView.addSubview(self.bubbleView)
    self.contentView.addSubview(self.avatarImage)
    self.contentView.addSubview(self.timeLabel)
    self.contentView.addSubview(self.nameLabel)
    avatarImage.layer.masksToBounds = true
    self.avatarImage.layer.cornerRadius = CGFloat(IMUIBaseMessageCell.avatarCornerRadius)
    
    let bubbleGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapBubbleView))
    let longPressBubbleGesture = UILongPressGestureRecognizer(target: self, action: #selector(self.longTapBubbleView))
    bubbleGesture.numberOfTapsRequired = 1
    self.bubbleView.isUserInteractionEnabled = true
    self.bubbleView.addGestureRecognizer(bubbleGesture)
    self.bubbleView.addGestureRecognizer(longPressBubbleGesture)
    
    let avatarGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapHeaderImage))
    avatarGesture.numberOfTapsRequired = 1
    avatarImage.isUserInteractionEnabled = true
    avatarImage.addGestureRecognizer(avatarGesture)
    
    nameLabel.font = IMUIMessageCellLayout.nameLabelTextFont
    
    self.setupSubViews()
  }
  
  fileprivate func setupSubViews() {
    timeLabel.textAlignment = .center
    timeLabel.textColor = IMUIMessageCellLayout.timeStringColor
    timeLabel.font = IMUIMessageCellLayout.timeStringFont
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func layoutCell(with layout: IMUIMessageCellLayoutProtocol, viewCache: IMUIReuseViewCache) {
    self.timeLabel.frame = layout.timeLabelFrame
    self.avatarImage.frame = layout.avatarFrame
    self.bubbleView.frame = layout.bubbleFrame
    self.nameLabel.frame = layout.nameLabelFrame
    
    self.removeStatusView(viewCache: viewCache)
    self.statusView = viewCache.statusViewCache.dequeue(layout: layout ) as? UIView
    self.contentView.addSubview(self.statusView!)
    self.addGestureForStatusView()
    self.nameLabel.textColor = IMUIMessageCellLayout.nameLabelTextColor
    self.statusView!.frame = layout.statusViewFrame
    
    
    let bubbleContentType = layout.bubbleContentType
    self.removeBubbleContentView(viewCache: viewCache, contentType: bubbleContentType)
    
    self.bubbleContentView = viewCache[bubbleContentType]!.dequeueContentView(layout: layout)
    self.bubbleContentType = bubbleContentType
    self.bubbleView.addSubview(self.bubbleContentView as! UIView)
    (self.bubbleContentView as! UIView).frame = UIEdgeInsetsInsetRect(CGRect(origin: CGPoint.zero, size: layout.bubbleFrame.size), layout.bubbleContentInset)
  }
  
  func addGestureForStatusView() {
    for recognizer in self.statusView?.gestureRecognizers ?? [] {
      self.statusView?.removeGestureRecognizer(recognizer)
    }
    
    let statusViewGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapSatusView))
    statusViewGesture.numberOfTapsRequired = 1
    self.statusView?.isUserInteractionEnabled = true
    self.statusView?.addGestureRecognizer(statusViewGesture)
  }
  
  func removeStatusView(viewCache: IMUIReuseViewCache) {
    if let view = self.statusView {
      viewCache.statusViewCache.switchViewToNotInUse(reuseView: self.statusView as! IMUIMessageStatusViewProtocol)
      view.removeFromSuperview()
    } else {
      for view in self.contentView.subviews {
        if let _ = view as? IMUIMessageStatusViewProtocol {
          viewCache.statusViewCache.switchViewToNotInUse(reuseView: view as! IMUIMessageStatusViewProtocol)
          view.removeFromSuperview()
        }
      }
    }
  }
  
  func removeBubbleContentView(viewCache: IMUIReuseViewCache, contentType: String) {
    for view in self.bubbleView.subviews {
      if let _ = view as? IMUIMessageContentViewProtocol {
        viewCache[self.bubbleContentType]?.switchViewToNotInUse(reuseView: view as! IMUIMessageContentViewProtocol)
        view.removeFromSuperview()
      }
    }
  }
  
  func setupData(with message: IMUIMessageModelProtocol) {
    self.avatarImage.image = message.fromUser.Avatar()
    self.bubbleView.backgroundColor = UIColor.init(netHex: 0xE7EBEF)
    self.timeLabel.text = message.timeString
    self.nameLabel.text = message.fromUser.displayName()
    self.bubbleContentView?.layoutContentView(message: message)
    self.message = message
    
    self.bubbleView.setupBubbleImage(resizeBubbleImage: message.resizableBubbleImage)
    
    let statusView = self.statusView as! IMUIMessageStatusViewProtocol
    switch message.messageStatus {
      case .sending:
        statusView.layoutSendingStatus()
        break
      case .failed:
        statusView.layoutFailedStatus()
        break
      case .success:
        statusView.layoutSuccessStatus()
        break
      case .mediaDownloading:
        statusView.layoutMediaDownloading()
        break
      case .mediaDownloadFail:
        statusView.layoutMediaDownloadFail()
    }
    
    if message.isOutGoing {
      self.nameLabel.textAlignment = .right
    } else {
      self.nameLabel.textAlignment = .left
    }
  }
  
  func presentCell(with message: IMUIMessageModelProtocol, viewCache: IMUIReuseViewCache, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    
    self.layoutCell(with: message.layout, viewCache: viewCache)
    self.setupData(with: message)
    self.delegate = delegate
  }
  
  @objc func tapBubbleView() {
    self.delegate?.messageCollectionView?(didTapMessageBubbleInCell: self, model: self.message!)
  }
  
  @objc func longTapBubbleView(sender: UITapGestureRecognizer) {
    if (sender.state == .began) {
        self.delegate?.messageCollectionView?(beganLongTapMessageBubbleInCell: self, model: self.message!)
    }
    
  }
  
  @objc func tapHeaderImage() {
    self.delegate?.messageCollectionView?(didTapHeaderImageInCell: self, model: self.message!)
  }
  
  @objc func tapSatusView() {
    self.delegate?.messageCollectionView?(didTapStatusViewInCell: self, model: self.message!)
  }
  
  func didDisAppearCell() {
  }
}
