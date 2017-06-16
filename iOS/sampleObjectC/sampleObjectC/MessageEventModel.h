//
//  MessageEventModel.h
//  sampleObjectC
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sampleObjectC-Swift.h"

@interface MessageEventModel : NSObject<IMUIMessageProtocol>
@property (nonatomic, copy) NSString * _Nonnull msgId;
@property(strong, nonatomic)NSString * _Nonnull evenText;

- (instancetype _Nonnull )initWithMsgId:(NSString *_Nonnull)msgId eventText:(NSString *_Nonnull)eventText;
@end
