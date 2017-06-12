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
  var statusViewCache = IMUIViewCache<IMUIMessageStatusViewProtocal>()
  
  var bubbleContentViewCache = [String:IMUIViewCache<IMUIMessageContentViewProtocal>]()
  
  subscript(bubbleContentType: String) -> IMUIViewCache<IMUIMessageContentViewProtocal>? {
    if let contentViewCache = bubbleContentViewCache[bubbleContentType] {
      return contentViewCache
    }
    
    bubbleContentViewCache[bubbleContentType] = IMUIViewCache<IMUIMessageContentViewProtocal>()
    return bubbleContentViewCache[bubbleContentType]
  }
}
