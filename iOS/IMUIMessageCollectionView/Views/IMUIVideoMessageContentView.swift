//
//  IMUIVideoMessageContentView.swift
//  sample
//
//  Created by oshumini on 2017/6/12.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation

public class IMUIVideoMessageContentView: UIView, IMUIMessageContentViewProtocal {

  lazy var videoView = UIImageView()
  lazy var playBtn = UIButton()
  lazy var videoDuration = UILabel()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.addSubview(self.videoView)
    videoView.addSubview(playBtn)
    videoView.addSubview(videoDuration)
    
    playBtn.frame = CGRect(origin: CGPoint.zero, size: CGSize(width: 56, height: 56))
    playBtn.setImage(UIImage.imuiImage(with: "video_play_btn"), for: .normal)
    videoDuration.textColor = UIColor.white
    videoDuration.font = UIFont.systemFont(ofSize: 10.0)
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func layoutContentView(message: IMUIMessageModelProtocol) {
    let layout = message.layout
    self.layoutVideo(with: message.mediaFilePath())
    videoView.frame = CGRect(origin: CGPoint.zero, size: layout.bubbleContentSize)
    playBtn.center = CGPoint(x: videoView.imui_width/2, y: videoView.imui_height/2)
    
    let durationX = videoView.imui_width - 30
    let durationY = videoView.imui_height - 24
    videoDuration.frame = CGRect(x: durationX,
                                 y: durationY,
                                 width: 30,
                                 height: 24)
    
    
  }
  

  
  func layoutVideo(with videoPath: String) {
    let asset = AVURLAsset(url: URL(fileURLWithPath: videoPath), options: nil)
    let seconds = Int (CMTimeGetSeconds(asset.duration))
    
    if seconds/3600 > 0 {
      videoDuration.text = "\(seconds/3600):\(String(format: "%02d", (seconds/3600)%60)):\(seconds%60)"
    } else {
      videoDuration.text = "\(seconds / 60):\(String(format: "%02d", seconds % 60))"
    }

    let serialQueue = DispatchQueue(label: "videoLoad")
    serialQueue.async {
      do {
        let imgGenerator = AVAssetImageGenerator(asset: asset)
        imgGenerator.appliesPreferredTrackTransform = true
        let cgImage = try imgGenerator.copyCGImage(at: CMTimeMake(0, 1), actualTime: nil)
        DispatchQueue.main.async {
          self.videoView.image = UIImage(cgImage: cgImage)
        }
      } catch {
        DispatchQueue.main.async {
          self.videoView.image = nil
        }
      }
    }
  }
}
