//
//  IMUILiveVideoMessageCell.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUILiveVideoMessageCell: IMUIBaseMessageCell {


  var videoView = UIView()
  var videoReader = IMUIVideoFileLoader()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    bubbleView.addSubview(self.videoView)
  }
  
  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
  }
  
  override func didDisAppearCell() {
    self.videoReader.isNeedToStopVideo = true
  }
  
  override func presentCell(with message: IMUIMessageModelProtocol, viewCache: IMUIReuseViewCache, delegate: IMUIMessageMessageCollectionViewDelegate?) {
    super.presentCell(with: message, viewCache: viewCache, delegate: delegate)
    self.layoutVideo(with: message.mediaFilePath())
    let layout = message.layout as! IMUIMessageCellLayout
    self.videoView.frame = UIEdgeInsetsInsetRect(CGRect(origin: CGPoint.zero, size: layout.bubbleFrame.size), layout.bubbleContentInset)
  }
  
  func layoutVideo(with videoPath: String) {
    self.videoView.layer.contents = nil
    self.videoReader.loadVideoFile(with: URL(fileURLWithPath: videoPath)) { (videoFrameImage) in
      DispatchQueue.main.async {
        self.videoView.layer.contents = videoFrameImage
      }
    }
  }
}
