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
  open var rAvatarUrl: String?
  public override init() {
    super.init()
  }
  
  @objc convenience init(userDic: NSDictionary) {
    self.init()
    self.rUserId = userDic.object(forKey: "userId") as? String
    self.rDisplayName = userDic.object(forKey: "displayName") as? String
    self.rAvatarFilePath = userDic.object(forKey: "avatarPath") as? String
    if FileManager.default.fileExists(atPath: self.rAvatarFilePath ?? "") {
      
    } else {
      self.rAvatarUrl = self.rAvatarFilePath
      self.rAvatarFilePath = ""
    }
    
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
    if let path = self.rAvatarFilePath {
      let fileManager = FileManager.default
      if fileManager.fileExists(atPath: path) {
        return UIImage(contentsOfFile: path)!
      }
    }
    return UIImage()
  }
  
  public func avatarUrlString() -> String? {
    return self.rAvatarUrl
  }
}
