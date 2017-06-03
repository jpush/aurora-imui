//
//  IMUIProgressButton.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/15.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import CoreGraphics


class IMUIProgressButton: UIButton {

  static var lineWidth: CGFloat = 2
  
  var progressLayer = CAShapeLayer()
  
  override var isSelected: Bool {
    didSet {
      self.progress = 0
    }
  }
  var progress: CGFloat = 0 {
    didSet {
      self.setNeedsDisplay()
    }
  }
  
  var progressColor: UIColor = UIColor.red {
    didSet {
      self.setNeedsDisplay()
    }
  }
  
  var clipColor: UIColor = UIColor.green {
    didSet {
      self.setNeedsDisplay()
    }
  }
  
  override func awakeFromNib() {
    super.awakeFromNib()
    self.layer.masksToBounds = false
    self.layer.addSublayer(progressLayer)
    
    progressLayer.frame = self.bounds
    progressLayer.strokeEnd = 0
    progressLayer.strokeColor = UIColor.green.cgColor
    progressLayer.lineWidth = IMUIProgressButton.lineWidth
  }
  
  open func setProgress(progress: CGFloat, animated: Bool) {
  
  }
  
  override func draw(_ rect: CGRect) {


    let contex = UIGraphicsGetCurrentContext()
    contex!.saveGState()
    contex?.setLineWidth(IMUIProgressButton.lineWidth)
    
    // draw circular
    let drawInRect = self.bounds.insetBy(dx: IMUIProgressButton.lineWidth, dy: IMUIProgressButton.lineWidth)
    let clipPath: CGPath = UIBezierPath(roundedRect: drawInRect,
                                        cornerRadius: self.imui_width / 2).cgPath
    contex?.setStrokeColor(self.clipColor.cgColor)
    contex?.addPath(clipPath)
    contex?.strokePath()
    
    // draw progress circular
    let center = CGPoint(x: self.bounds.midX, y: self.bounds.midY)
    let progressPath = UIBezierPath(arcCenter: center,
                                    radius: drawInRect.width/2,
                                    startAngle: -CGFloat.pi/2,
                                    endAngle: -CGFloat.pi/2 + 2 * CGFloat.pi * progress,
                                    clockwise: true).cgPath
    
    contex?.setStrokeColor(self.progressColor.cgColor)
    contex?.addPath(progressPath)
    contex?.strokePath()
  }
}
