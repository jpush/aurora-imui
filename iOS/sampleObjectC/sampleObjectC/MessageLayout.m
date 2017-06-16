//
//  MessageLayout.m
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "MessageLayout.h"
#import "sampleObjectC-Swift.h"

@interface MessageLayout()
@property(strong, nonatomic)IMUIMessageCellLayout *layout;
@property(assign, nonatomic)BOOL isOutgoing;
@end

@implementation MessageLayout



- (instancetype)init
{
  self = [super init];
  if (self) {

  }
  return self;
}

- (instancetype)initWithIsOutGoingMessage:(BOOL)isOutgoing isNeedShowTime:(BOOL)isNeedShowTime bubbleContentSize:(CGSize)bubbleContentSize bubbleContentInsets:(UIEdgeInsets)contentInset contentType:(NSString *)contentType {
  
  self = [super init];
  if (self) {
    _layout = [[IMUIMessageCellLayout alloc] initWithIsOutGoingMessage: isOutgoing isNeedShowTime: isNeedShowTime bubbleContentSize: bubbleContentSize bubbleContentInsets: contentInset];
    _isOutgoing = isOutgoing;
    _bubbleContentType = contentType;
  }
  return self;
}

- (CGFloat)cellHeight {
  return _layout.cellHeight;
}

- (CGRect)avatarFrame {
  return _layout.avatarFrame;
}

- (CGRect)timeLabelFrame {
  return _layout.timeLabelFrame;
}

- (CGRect)bubbleFrame {
  return _layout.bubbleFrame;
}

- (CGSize) bubbleContentSize {
  return _layout.bubbleContentSize;
}

- (UIEdgeInsets)bubbleContentInset {
  return _layout.bubbleContentInset;
}

- (UIEdgeInsets)cellContentInset {
  return _layout.cellContentInset;
}

- (id <IMUIMessageStatusViewProtocol> _Nonnull)statusView {
  return _layout.statusView;
}

- (CGRect)statusViewFrame {
  return _layout.statusViewFrame;
}

- (CGRect)nameLabelFrame {
  return _layout.nameLabelFrame;
}

- (id <IMUIMessageContentViewProtocol> _Nonnull)bubbleContentView {
  if ([_bubbleContentType isEqual: @"Text"]) {
    return [IMUITextMessageContentView new];
  }
  
  if ([_bubbleContentType isEqual: @"Voice"]) {
    return [IMUIVoiceMessageContentView new];
  }

  if ([_bubbleContentType isEqual: @"Image"]) {
    return [IMUIImageMessageContentView new];
  }
  
  if ([_bubbleContentType isEqual: @"Video"]) {
    return [IMUIVideoMessageContentView new];
  }
  
  return [IMUITextMessageContentView new];
}

- (NSString * _Nonnull)bubbleContentType {
  return _bubbleContentType;
}
@end
