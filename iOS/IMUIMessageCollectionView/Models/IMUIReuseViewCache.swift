//
//  IMUIReuseViewCache.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/18.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class IMUIReuseViewCache {
  var statusViewCache = IMUIViewCache<IMUIMessageStatusViewProtocol>()
  
  var bubbleContentViewCache = [String:IMUIViewCache<IMUIMessageContentViewProtocol>]()
  
  subscript(bubbleContentType: String) -> IMUIViewCache<IMUIMessageContentViewProtocol>? {
    if let contentViewCache = bubbleContentViewCache[bubbleContentType] {
      return contentViewCache
    }
    
    bubbleContentViewCache[bubbleContentType] = IMUIViewCache<IMUIMessageContentViewProtocol>()
    return bubbleContentViewCache[bubbleContentType]
  }
}
