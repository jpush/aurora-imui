//
//  BundleExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/4.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

public extension Bundle {
  class func imuiBundle() -> Bundle {
    return Bundle(for: IMUIMessageCollectionView.self)
  }
}
