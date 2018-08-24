//
//  MyUser.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class MyUser: NSObject, IMUIUserProtocol {

  public override init() {
    super.init()
  }
  
  func userId() -> String {
    return ""
  }
  
  func displayName() -> String {
    return ""
  }
  
  func Avatar() -> UIImage {
    return UIImage(named: "defoult_header")!
  }
  
  func avatarUrlString() -> String? {
    return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg"
  }
}
