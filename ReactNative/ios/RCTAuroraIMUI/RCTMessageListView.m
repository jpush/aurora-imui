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
#import "RCTAuroraIMUIModule.h"

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
    
    [self addObserver:self forKeyPath:@"bounds" options:NSKeyValueObservingOptionNew context:NULL];
    
    
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
    [refreshControl addTarget:self action:@selector(refresh:) forControlEvents:UIControlEventValueChanged];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
      [_messageList.messageCollectionView addSubview:refreshControl];
      _messageList.messageCollectionView.alwaysBounceVertical = YES;
    });
  }
  return self;
}

- (void)refresh:(UIRefreshControl *)refreshControl
{
  NSLog(@"start refresh");
  [self performSelector:@selector(endRefresh:) withObject:refreshControl afterDelay:0.5f];
}

- (void)endRefresh:(UIRefreshControl *)refreshControl
{
  [refreshControl endRefreshing];
  if (_delegate != nil) {
    [_delegate onPullToRefreshMessageList];
  }
  NSLog(@"end refresh");
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
  if (object == self && [keyPath isEqualToString:@"bounds"]) {
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.01 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
      [self.messageList scrollToBottomWith: NO];
    });
  }
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
  [self removeObserver:self forKeyPath:@"bounds"];
}
@end
