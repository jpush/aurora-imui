//
//  IMUIInputViewManager.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/21.
//  Copyright © 2017年 Facebook. All rights reserved.
//
#import <UIKit/UIKit.h>
#import <React/RCTViewManager.h>
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>

@interface IMUIInputViewManager : RCTViewManager

@end

@implementation IMUIInputViewManager

RCT_EXPORT_MODULE()
- (UIView *)view
{
  IMUIInputView *inputView = [[IMUIInputView alloc] init];
  return inputView;
}


@end
