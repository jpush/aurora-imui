//
//  IMUIStatusViewCache.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class IMUIStatusViewCache {
  static var inUseStatusViews = [String: IMUIMessageStatusViewProtocal]()
  static var notInUseStatusViews = [String: IMUIMessageStatusViewProtocal]()
  
  // if there are not status view in notInUseStatusViews will add this
  
  /*
  *  dequeue function will return the status View from cache
  *  @parameter statusView: if there are not status view in notInUseStatusViews will return this status view
  *
  */
  class func dequeue(statusView: IMUIMessageStatusViewProtocal) -> IMUIMessageStatusViewProtocal {
    print("in use count \(IMUIStatusViewCache.inUseStatusViews.count)  not in use count \(IMUIStatusViewCache.notInUseStatusViews.count)")
    
    if notInUseStatusViews.isEmpty {
      
      let view = statusView as! UIView
      inUseStatusViews[view.description] = statusView
      return statusView
    }
    
    for (key, view) in notInUseStatusViews {
      let statusView = view as! UIView
      inUseStatusViews[statusView.description] = view
      notInUseStatusViews.removeValue(forKey: key)
      return view
    }
    
    
    return statusView
  }
  
  class func switchStatusViewToNotInUse(statusView: IMUIMessageStatusViewProtocal) {
    let view = statusView as! UIView
    inUseStatusViews.removeValue(forKey: view.description)
    notInUseStatusViews[view.description] = statusView
    
  }
  
  class func clearAllStatusViews() {
    inUseStatusViews.removeAll()
    notInUseStatusViews.removeAll()
  }
}
