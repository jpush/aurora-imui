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
  static var inUseStatusViews = [Int: IMUIMessageStatusViewProtocal]()
  static var notInUseStatusViews = [Int: IMUIMessageStatusViewProtocal]()
  
  /*
  *  dequeue function will return the status View from cache
  *  @parameter statusView: if there are not status view in notInUseStatusViews will return layout.statusView
  *
  */
  class func dequeue(layout: IMUIMessageCellLayoutProtocal) -> IMUIMessageStatusViewProtocal {
    print("in use count \(IMUIStatusViewCache.inUseStatusViews.count)  not in use count \(IMUIStatusViewCache.notInUseStatusViews.count)")
    
    if notInUseStatusViews.isEmpty {
      let view = layout.statusView as! UIView
      inUseStatusViews[view.hashValue] = view as! IMUIMessageStatusViewProtocal
      return view as! IMUIMessageStatusViewProtocal
    }
    
    for (key, view) in notInUseStatusViews {
      let statusView = view as! UIView
      inUseStatusViews[statusView.hashValue] = view
      notInUseStatusViews.removeValue(forKey: key)
      return view
    }
    
    return layout.statusView
  }
  
  class func switchStatusViewToNotInUse(statusView: IMUIMessageStatusViewProtocal) {
    let view = statusView as! UIView
    inUseStatusViews.removeValue(forKey: view.hashValue)
    notInUseStatusViews[view.hashValue] = statusView
  }
  
  class func clearAllStatusViews() {
    inUseStatusViews.removeAll()
    notInUseStatusViews.removeAll()
  }
}
