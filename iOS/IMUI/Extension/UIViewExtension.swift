//
//  UIViewExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public extension UIView {
  
  
  var imui_left: CGFloat {
    set {
      var frame = self.frame
      frame.origin.x = newValue
      self.frame = frame
    }
    get {
      return self.frame.origin.x
    }
    
  }

  var imui_top: CGFloat {
    set {
      var frame = self.frame
      frame.origin.y = newValue
      self.frame = frame
    }
    
    get {
      return self.frame.origin.y;
    }
  }

  var imui_right: CGFloat {
    set {
      var frame = self.frame
      let distance = newValue - frame.origin.x
      frame.size.width = distance
      self.frame = frame
      
    }
    
    get {
      return self.frame.origin.x + self.frame.width;
    }
  }

  var imui_bottom: CGFloat {
    set {
      var frame = self.frame
      let distance = newValue - frame.origin.y
      frame.size.width = distance
      self.frame = frame
    }
    
    get {
      return self.frame.origin.y + self.frame.size.height;
    }
  }
  
  var imui_centerX: CGFloat {
    set {
      var center = self.center
      center.x = newValue
      self.center = center
    }
    
    get {
      return self.center.x
    }
  }
  
  var imui_centerY: CGFloat {
    set {
      var center = self.center
      center.y = newValue
      self.center = center
    }
    
    get {
      return self.center.y
    }
  }
  
  var imui_width: CGFloat {
    set {
      var frame = self.frame
      frame.size.width = newValue
      self.frame = frame
    }
    
    get {
      return self.frame.size.width
    }
  }
  
  var imui_height: CGFloat {
    set {
      var frame = self.frame
      frame.size.height = newValue
      self.frame = frame
    }
    
    get {
      return self.frame.size.height
    }
  }

  var imui_origin: CGPoint {
    set {
      var frame = self.frame
      frame.origin = newValue
      self.frame = frame
    }
    
    get {
      return self.frame.origin
    }
  }
  
  var imui_size: CGSize {
    set {
      var frame = self.frame
      frame.size = newValue
      self.frame = frame
    }
    
    get {
      return self.frame.size
    }
  }
  

  func move(with vector: CGVector) {
    var center = self.center
    center.x += vector.dx
    center.y += vector.dy
    self.center = center
  }

  func positionInLeftSide(with point: CGPoint) -> Bool {
    if point.x < self.imui_centerX {
      return true
    } else {
      return false
    }
  }
}
