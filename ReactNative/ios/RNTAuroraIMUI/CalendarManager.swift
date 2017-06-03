//
//  CalendarManager.swift
//  imuiDemo
//
//  Created by oshumini on 2017/5/20.
//  Copyright © 2017年 Facebook. All rights reserved.
//

import Foundation

// CalendarManager.swift

@objc(CalendarManager)
public class CalendarManager: NSObject {
  
  @objc func printMessage(message: String?) {
    print("SwiftReactModule::printMessage => \(message)")
  }
}
