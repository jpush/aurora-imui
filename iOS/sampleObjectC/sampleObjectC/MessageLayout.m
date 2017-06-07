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

- (instancetype)initWithIsOutGoingMessage:(BOOL)isOutgoing isNeedShowTime:(BOOL)isNeedShowTime bubbleContentSize:(CGSize)bubbleContentSize bubbleContentInsets:(UIEdgeInsets)contentInset {
  self = [super init];
  if (self) {
    _layout = [[IMUIMessageCellLayout alloc] initWithIsOutGoingMessage: isOutgoing isNeedShowTime: isNeedShowTime bubbleContentSize: bubbleContentSize bubbleContentInsets: contentInset];
    _isOutgoing = isOutgoing;
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

- (UIEdgeInsets)bubbleContentInset {
  return _layout.bubbleContentInset;
}

- (UIEdgeInsets)cellContentInset {
  return _layout.cellContentInset;
}

- (id <IMUIMessageStatusViewProtocal> _Nonnull)statusView {
  return _layout.statusView;
}

- (CGRect)statusViewFrame {
  return _layout.statusViewFrame;
}

- (CGRect)nameLabelFrame {
  return _layout.nameLabelFrame;
}
@end
