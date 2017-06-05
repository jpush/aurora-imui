//
//  RCTMessageListView.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/26.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTMessageListView.h"
#import <CoreGraphics/CoreGraphics.h>
#import "RCTInputView.h"
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>
#import "RCTAuroraIModule.h"

@implementation RCTMessageListView

- (instancetype)init {
  self = [super init];
  return self;
}


- (RCTMessageModel *)convertMessageDicToModel:(NSDictionary *)message {
  return [[RCTMessageModel alloc] initWithMessageDic: message];
}

- (instancetype)initWithFrame:(CGRect)frame {
  self = [super initWithFrame: frame];
  return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
  self = [super initWithCoder:aDecoder];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(appendMessages:)
                                                 name:kAppendMessages object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(insertMessagesToTop:)
                                                 name:kInsertMessagesToTop object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(updateMessage:)
                                                 name:kUpdateMessge object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(scrollToBottom:)
                                                 name:kScrollToBottom object:nil];
  }
  return self;
}

- (void)appendMessages:(NSNotification *) notification {
  NSArray *messages = [[notification object] copy];
  
  for (NSDictionary *message in messages) {
    RCTMessageModel * messageModel = [self convertMessageDicToModel:message];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      [self.messageList appendMessageWith: messageModel];
    });
  }
}

- (void)insertMessagesToTop:(NSNotification *) notification {
  NSArray *messages = [[notification object] copy];
  
  NSMutableArray *messageModels = @[].mutableCopy;
  for (NSDictionary *message in messages) {
    RCTMessageModel * messageModel = [self convertMessageDicToModel: message];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      [self.messageList appendMessageWith: messageModel];
    });
  }
}

- (void)updateMessage:(NSNotification *) notification {
  NSDictionary *message = [notification object];
  RCTMessageModel * messageModel = [self convertMessageDicToModel: message];
  
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.messageList updateMessageWith: messageModel];
  });
}

- (void)scrollToBottom:(NSNotification *) notification {
  BOOL animate = [[notification object] copy];
  
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.messageList scrollToBottomWith:animate];
  });
}

- (void)awakeFromNib {
  [super awakeFromNib];

}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver: self];
}
@end
