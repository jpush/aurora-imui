//
//  IMUIMessageStatusView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public class IMUIMessageDefaultStatusView: UIButton, IMUIMessageStatusViewProtocal {

  static var lineWidth: CGFloat = 2
  
  var progress: CGFloat = 0 {
    didSet {
      self.setNeedsDisplay()
    }
  }
  var updater: CADisplayLink! = nil
  
  var progressLayer = CAShapeLayer()
  
  var progressColor: UIColor = UIColor.red {
    didSet {
      self.setNeedsDisplay()
    }
  }

  override init(frame: CGRect) {
    super.init(frame: frame)
    
    updater = CADisplayLink(target: self, selector: #selector(self.updateProgress))
    updater.frameInterval = 1
    
    self.setImage(UIImage.imuiImage(with: "message_status_fail"), for: .selected)
  }
  
  func updateProgress() {
    if progress > 100 {
      progress = 0
    }
    progress = progress + 1
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  
  override public func draw(_ rect: CGRect) {
    
    if progress == 0 { return }
    
    let contex = UIGraphicsGetCurrentContext()
    contex!.saveGState()
    contex?.setLineWidth(IMUIMessageDefaultStatusView.lineWidth)
    
    // draw circular
    let drawInRect = self.bounds.insetBy(dx: IMUIMessageDefaultStatusView.lineWidth,
                                         dy: IMUIMessageDefaultStatusView.lineWidth)
    contex?.strokePath()
    
    // draw progress circular
    let center = CGPoint(x: self.bounds.midX, y: self.bounds.midY)
    let progressPath = UIBezierPath(arcCenter: center,
                                    radius: drawInRect.width/2,
                                    startAngle: -CGFloat.pi/2 + 2 * CGFloat.pi * progress/100.0,
                                    endAngle: -CGFloat.pi/2 + 2 * CGFloat.pi * progress/50.0,
                                    clockwise: true).cgPath
    
    contex?.setStrokeColor(self.progressColor.cgColor)
    contex?.addPath(progressPath)
    contex?.strokePath()
  }
  
  // MARK: - IMUIMessageStatusViewProtocal
  public func layoutFailedStatus() {
    self.isSelected = true
    updater.isPaused = true
    self.progress = 0
  }
  
  public func layoutSendingStatus() {
    self.isSelected = false
    updater.add(to: RunLoop.current, forMode: RunLoopMode.commonModes)
    updater.isPaused = false
  }
  
  public func layoutSuccessStatus() {
    self.isSelected = false
    updater.isPaused = true
    self.progress = 0
    
  }
}
