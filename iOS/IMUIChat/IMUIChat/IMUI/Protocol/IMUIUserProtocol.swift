//
//  IMUIUserProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public protocol IMUIUserProtocol {
  func userId() -> String
  func displayName() -> String
  func Avatar() -> UIImage
}
