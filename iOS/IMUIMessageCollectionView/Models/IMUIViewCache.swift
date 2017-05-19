//
//  IMUIViewCache.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/18.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class IMUIViewCache {
  var inUseStatusViews = [Int: IMUIMessageStatusViewProtocal]()
  var notInUseStatusViews = [Int: IMUIMessageStatusViewProtocal]()
  
  /*
   *  dequeue function will return the status View from cache
   *  @parameter statusView: if there are not status view in notInUseStatusViews will return layout.statusView
   *
   */
  func dequeue(layout: IMUIMessageCellLayoutProtocal) -> IMUIMessageStatusViewProtocal {
    print("in use count \(self.inUseStatusViews.count)  not in use count \(self.notInUseStatusViews.count)")
    
    if notInUseStatusViews.isEmpty {
      let view = layout.statusView as! UIView
      inUseStatusViews[view.hashValue] = view as? IMUIMessageStatusViewProtocal
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
  
  func switchStatusViewToNotInUse(statusView: IMUIMessageStatusViewProtocal) {
    let view = statusView as! UIView
    inUseStatusViews.removeValue(forKey: view.hashValue)
    notInUseStatusViews[view.hashValue] = statusView
  }
  
  func clearAllStatusViews() {
    inUseStatusViews.removeAll()
    notInUseStatusViews.removeAll()
  }
}
