//
//  MessageLayout.h
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sampleObjectC-Swift.h"
#import <UIKit/UIKit.h>

@interface MessageLayout : NSObject <IMUIMessageCellLayoutProtocol>

@property (nonatomic, readonly) CGFloat cellHeight;

@property (nonatomic, readonly) CGRect avatarFrame;

@property (nonatomic, readonly) CGRect timeLabelFrame;

@property (nonatomic, readonly) CGRect bubbleFrame;

@property (nonatomic, readonly) CGSize bubbleContentSize;

@property (nonatomic, readonly) UIEdgeInsets bubbleContentInset;

@property (nonatomic, readonly) UIEdgeInsets cellContentInset;

@property (nonatomic, readonly, strong) id <IMUIMessageStatusViewProtocol> _Nonnull statusView;

@property (nonatomic, readonly) CGRect statusViewFrame;

@property (nonatomic, readonly) CGRect nameLabelFrame;

@property (nonatomic, readonly, strong) id <IMUIMessageContentViewProtocol> _Nonnull bubbleContentView;

@property (nonatomic, copy) NSString * _Nonnull bubbleContentType;

- (instancetype)initWithIsOutGoingMessage:(BOOL)isOutgoing
                           isNeedShowTime:(BOOL)isNeedShowTime
                        bubbleContentSize:(CGSize)bubbleContentSize
                      bubbleContentInsets:(UIEdgeInsets)contentInset
                              contentType:(NSString *)contentType;
@end
