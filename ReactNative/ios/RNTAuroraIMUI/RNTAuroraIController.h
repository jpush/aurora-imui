//
//  AuroraIMUIController.h
//  RNTAuroraIMUI
//
//  Created by oshumini on 2017/6/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//
#import <Foundation/Foundation.h>

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"
#endif

#define kAppendMessages @"kAppendMessage"
#define kInsertMessagesToTop @"kInsertMessagesToTop"
#define kUpdateMessge @"kUpdateMessge"
#define kScrollToBottom @"kScrollToBottom"
#define kScrollToBottom @"kScrollToBottom"
#define kHidenFeatureView @"kHidenFeatureView"

@interface RNTAuroraIController : NSObject <RCTBridgeModule>

@end
