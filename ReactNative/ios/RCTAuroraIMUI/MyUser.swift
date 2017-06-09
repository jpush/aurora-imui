//
//  MyUser.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

open class RCTUser: NSObject, IMUIUserProtocol {
  open var rUserId: String?
  open var rDisplayName: String?
  open var rAvatarFilePath: String?
  
  public override init() {
    super.init()
  }
  
  convenience init(userDic: NSDictionary) {
    self.init()
    self.rUserId = userDic.object(forKey: "userId") as? String
    self.rDisplayName = userDic.object(forKey: "displayName") as? String
    self.rAvatarFilePath = userDic.object(forKey: "avatarPath") as? String
  }
  
  public func userId() -> String {
    if let rUserId = self.rUserId {
      return rUserId
    } else {
      return ""
    }
  }
  
  public func displayName() -> String {
    if let rDisplayName = self.rDisplayName {
      return rDisplayName
    } else {
      return ""
    }
  }
  
  public func Avatar() -> UIImage {
    return UIImage(named: "defoult_header")!
  }
}
