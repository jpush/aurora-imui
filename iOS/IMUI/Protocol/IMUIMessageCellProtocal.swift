//
//  IMUIMessageCellProtocal.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

public protocol IMUIMessageCellProtocal {
  func presentCell(with message: IMUIMessageModelProtocol, delegate: IMUIMessageMessageCollectionViewDelegate?)
  func didDisAppearCell()
}

public extension IMUIMessageCellProtocal {
  func didDisAppearCell() {}
}
