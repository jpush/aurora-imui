//
//  RCTAuroraIMUIModule.h
//  RCTAuroraIMUI
//
//  Created by oshumini on 2017/6/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//
#import <Foundation/Foundation.h>

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTEventDispatcher.h"
#import "React/RCTBridgeModule.h"
#endif

#define kAppendMessages @"kAppendMessage"
#define kRemoveMessage @"kRemoveMessage"
#define kRemoveAllMessages @"kRemoveAllMessages"
#define kInsertMessagesToTop @"kInsertMessagesToTop"
#define kUpdateMessge @"kUpdateMessge"
#define kScrollToBottom @"kScrollToBottom"
#define kScrollToBottom @"kScrollToBottom"
#define kHidenFeatureView @"kHidenFeatureView"
#define kMessageListDidLoad @"kMessageListDidLoad"
#define kLayoutInputView @"kLayoutInputView"

@interface RCTAuroraIMUIModule : NSObject <RCTBridgeModule>
+ (NSString *)getPath;
@end
