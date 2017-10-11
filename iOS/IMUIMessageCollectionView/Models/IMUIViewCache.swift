//
//  IMUIViewCache.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/18.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class IMUIViewCache<T> {
  var inUseViews = [Int: T]()
  var notInUseViews = [Int: T]()
  
  /*
   *  dequeue function will return the status View from cache
   *  @parameter statusView: if there are not status view in notInUseStatusViews will return layout.statusView
   *
   */
  func dequeue(layout: IMUIMessageCellLayoutProtocol) -> T {
//    print("statusView in use count \(self.inUseViews.count)  not in use count \(self.notInUseViews.count)")
    
    if notInUseViews.isEmpty {
      let view = layout.statusView as! UIView
      inUseViews[view.hashValue] = view as? T
      return view as! T
    }
    
    for (key, view) in notInUseViews {
      let statusView = view as! UIView
      inUseViews[statusView.hashValue] = view
      notInUseViews.removeValue(forKey: key)
      return view
    }
    
    return layout.statusView as! T
  }
  
  func dequeueContentView(layout: IMUIMessageCellLayoutProtocol) -> T {
    print("statusView in use count \(self.inUseViews.count)  not in use count \(self.notInUseViews.count)")
    
    if notInUseViews.isEmpty {
      let view = layout.bubbleContentView as! UIView
      inUseViews[view.hashValue] = view as? T
      return view as! T
    }
    
    for (key, view) in notInUseViews {
      let bubbleContentView = view as! UIView
      inUseViews[bubbleContentView.hashValue] = view
      notInUseViews.removeValue(forKey: key)
      return view
    }
    
    return layout.bubbleContentView as! T
  }
  
  func switchViewToNotInUse(reuseView: T) { // Bug!!
    let view = reuseView as! UIView
    if let view = inUseViews[view.hashValue] as? UIView {
      inUseViews.removeValue(forKey: view.hashValue)
      notInUseViews[view.hashValue] = reuseView
      print("switchViewToNotInUse")
    }

  }
  
  func clearAllViews() {
    inUseViews.removeAll()
    notInUseViews.removeAll()
  }
}
