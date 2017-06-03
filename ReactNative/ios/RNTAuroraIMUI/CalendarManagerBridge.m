//
//  CalendarManagerBridge.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/20.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"
#endif
//#import "RCTSwiftBridgeModule.h"


@interface RCT_EXTERN_MODULE(CalendarManager, NSObject)

RCT_EXTERN_METHOD(printMessageWithMessage:(NSString *)message)
//RCT_EXTERN_METHOD(printMessage:(NSString *_Nonnull)message)
//- (void)saySomeWithMessage:(NSString * _Nonnull)message;
@end
