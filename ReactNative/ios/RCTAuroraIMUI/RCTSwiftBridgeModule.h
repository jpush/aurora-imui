//
//  RCTSwiftBridgeModule.h
//  imuiDemo
//
//  Created by oshumini on 2017/5/20.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#ifndef RCTSwiftBridgeModule_h
#define RCTSwiftBridgeModule_h


#endif /* RCTSwiftBridgeModule_h */

#import <Foundation/Foundation.h>
#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"
#endif

#define RCT_EXTERN_MODULE(objc_name, objc_supername) \
RCT_EXTERN_REMAP_MODULE(objc_name, objc_name, objc_supername)

#define RCT_EXTERN_REMAP_MODULE(js_name, objc_name, objc_supername) \
objc_name : objc_supername \
@end \
@interface objc_name (RCTExternModule) <RCTBridgeModule> \
@end \
@implementation objc_name (RCTExternModule) \
RCT_EXPORT_MODULE(js_name)

#define RCT_EXTERN_METHOD(method) \
RCT_EXTERN_REMAP_METHOD(, method)

#define RCT_EXTERN_REMAP_METHOD(js_name, method) \
- (void)__rct_export__##method { \
__attribute__((used, section("__DATA,RCTExport"))) \
__attribute__((__aligned__(1))) \
static const char *__rct_export_entry__[] = { __func__, #method, #js_name }; \
}
