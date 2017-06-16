//
//  MessageEventModel.m
//  sampleObjectC
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "MessageEventModel.h"

@interface MessageEventModel ()
@end

@implementation MessageEventModel

- (instancetype)initWithMsgId:(NSString *)msgId eventText:(NSString *)eventText
{
  self = [super init];
  if (self) {
    _msgId = msgId;
    _evenText = eventText;
  }
  return self;
}


@end
