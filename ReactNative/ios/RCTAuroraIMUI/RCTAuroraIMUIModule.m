//
//  RCTAuroraIMUIModule.m
//  RCTAuroraIMUIModule
//
//  Created by oshumini on 2017/6/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "RCTAuroraIMUIModule.h"
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>
#import "RCTAuroraIMUIFileManager.h"

@interface RCTAuroraIMUIModule () {
}

@end

@implementation RCTAuroraIMUIModule
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

+ (id)allocWithZone:(NSZone *)zone {
  static RCTAuroraIMUIModule *sharedInstance = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    sharedInstance = [super allocWithZone:zone];
  });
  return sharedInstance;
}

- (id)init {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
  
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(messageDidLoad:)
                                               name:kMessageListDidLoad object:nil];
  self = [super init];
  [RCTAuroraIMUIFileManager createDirectory:@"RCTAuroraIMUI" atFilePath:[NSHomeDirectory() stringByAppendingPathComponent:@"Documents"]];
  return self;
}

- (void)messageDidLoad:(NSNotification *) notification {
  [self.bridge.eventDispatcher sendAppEventWithName:@"IMUIMessageListDidLoad"
                                               body:nil];
}

RCT_EXPORT_METHOD(appendMessages:(NSArray *)messages) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kAppendMessages object: messages];
}

RCT_EXPORT_METHOD(removeMessage:(NSString *)messageId) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kRemoveMessage object: messageId];
}

RCT_EXPORT_METHOD(removeAllMessage) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kRemoveAllMessages object: nil];
}

RCT_EXPORT_METHOD(updateMessage:(NSDictionary *)message) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kUpdateMessge object: message];
}

RCT_EXPORT_METHOD(insertMessagesToTop:(NSArray *)messages) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kInsertMessagesToTop object: messages];
}

RCT_EXPORT_METHOD(scrollToBottom:(BOOL) animate) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kScrollToBottom object: @(animate)];
}

RCT_EXPORT_METHOD(hidenFeatureView:(BOOL) animate) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kHidenFeatureView object: @(animate)];
}

RCT_EXPORT_METHOD(stopPlayVoice) {
  [[IMUIAudioPlayerHelper sharedInstance] stopAudio];
}

RCT_EXPORT_METHOD(layoutInputView) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kLayoutInputView object: nil];
}

RCT_EXPORT_METHOD(scaleImage:(NSDictionary *)dic
                  callback:(RCTResponseSenderBlock)callback) {
  if (![[NSFileManager defaultManager] fileExistsAtPath:dic[@"path"] ?: @""]) {
    callback(@[@{@"code": @(1),
                 @"description": @"File could not be found."
                 }]);
    return;
  }
  
  NSNumber *width = dic[@"width"] ?: @(0);
  NSNumber *height = dic[@"height"] ?: @(0);
  CGRect rect = CGRectMake(0, 0, width.floatValue, height.floatValue);
  
  UIImage *originImg = [UIImage imageWithContentsOfFile:dic[@"path"]];
  
  UIGraphicsBeginImageContext( rect.size );
  [originImg drawInRect:rect];
  UIImage *scaledImg = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();
  
  NSData *imageData = UIImageJPEGRepresentation(scaledImg, 1);
  NSString *filePath = [RCTAuroraIMUIFileManager getPath];
  if ([imageData writeToFile: filePath atomically: true]) {
    callback(@[@{@"code": @(0),
                 @"thumbPath": filePath
                 }]);
  } else {
    callback(@[@{@"code": @(1),
                 @"description": @"File could not be writed."
                 }]);
  }
}
// only return jpeg
RCT_EXPORT_METHOD(compressImage:(NSDictionary *)dic
                  callback:(RCTResponseSenderBlock)callback) {
  if (![[NSFileManager defaultManager] fileExistsAtPath:dic[@"path"] ?: @""]) {
    callback(@[@{@"code": @(1),
                 @"description": @"File could not be found."
                 }]);
    return;
  }
  
  UIImage *img = [UIImage imageWithContentsOfFile:dic[@"path"]];
  NSNumber *compressionQuality = dic[@"compressionQuality"] ?: @(1);
  NSData *imageData = UIImageJPEGRepresentation(img, compressionQuality.floatValue);;
  NSString *filePath = [RCTAuroraIMUIFileManager getPath];
  
  if ([imageData writeToFile: filePath atomically: true]) {
    callback(@[@{@"code": @(0),
                 @"thumbPath": filePath
                 }]);
  } else {
    callback(@[@{@"code": @(1),
                 @"description": @"File could not be writed."
                 }]);
  }
}

@end
