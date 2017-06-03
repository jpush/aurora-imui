//
//  RNTAuroraIMUI-Bridging-Header.h
//  RNTAuroraIMUI
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"
#endif
