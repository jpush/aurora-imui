//
//  RNTMessageListView.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/26.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RNTMessageListView.h"
#import <CoreGraphics/CoreGraphics.h>
#import "RNTInputView.h"
#import <RNTAuroraIMUI/RNTAuroraIMUI-Swift.h>
#import "RNTAuroraIController.h"

@implementation RNTMessageListView

- (instancetype)init {
  self = [super init];
  return self;
}


- (RNTMessageModel *)convertMessageDicToModel:(NSDictionary *)message {
  return [[RNTMessageModel alloc] initWithMessageDic: message];
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
    RNTMessageModel * messageModel = [self convertMessageDicToModel:message];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      [self.messageList appendMessageWith: messageModel];
    });
  }
}

- (void)insertMessagesToTop:(NSNotification *) notification {
  NSArray *messages = [[notification object] copy];
  
  NSMutableArray *messageModels = @[].mutableCopy;
  for (NSDictionary *message in messages) {
    RNTMessageModel * messageModel = [self convertMessageDicToModel: message];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      [self.messageList appendMessageWith: messageModel];
    });
  }
}

- (void)updateMessage:(NSNotification *) notification {
  NSDictionary *message = [notification object];
  RNTMessageModel * messageModel = [self convertMessageDicToModel: message];
  
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
