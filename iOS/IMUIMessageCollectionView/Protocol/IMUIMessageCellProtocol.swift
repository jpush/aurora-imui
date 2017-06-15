//
//  IMUIMessageCellProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

protocol IMUIMessageCellProtocol {
  func presentCell(with message: IMUIMessageModelProtocol, viewCache: IMUIReuseViewCache, delegate: IMUIMessageMessageCollectionViewDelegate?)
  func didDisAppearCell()
}

extension IMUIMessageCellProtocol {
  func didDisAppearCell() {}
}
