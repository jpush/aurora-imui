//
//  MessageModel.h
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sampleObjectC-Swift.h"
#import <UIKit/UIKit.h>


@interface MessageModel : NSObject <IMUIMessageModelProtocol>

@property (nonatomic, readonly, copy) NSString * _Nonnull msgId;

@property (nonatomic, readonly, strong) id <IMUIUserProtocol> _Nonnull fromUser;

@property (nonatomic, readonly, strong) id <IMUIMessageCellLayoutProtocol> _Nonnull layout;

@property (nonatomic, readonly, strong) UIImage * _Nonnull resizableBubbleImage;

@property (nonatomic, readonly, copy) NSString * _Nonnull timeString;

- (NSString * _Nonnull)text SWIFT_WARN_UNUSED_RESULT;

- (NSString * _Nonnull)mediaFilePath SWIFT_WARN_UNUSED_RESULT;

@property (nonatomic, readonly) CGFloat duration;

@property (nonatomic, readonly) BOOL isOutGoing;

@property (nonatomic, readonly) enum IMUIMessageStatus messageStatus;

@property (assign, nonatomic) NSString *type;


//- (instancetype)initWithIsOutGoingMessage:(BOOL)isOutgoing
//                           isNeedShowTime:(BOOL)isNeedShowTime
//                        bubbleContentSize:(CGSize)bubbleContentSize
//                      bubbleContentInsets:(UIEdgeInsets)contentInset
//                              contentType:(NSString *)contentType;


- (instancetype)initWithText:(NSString *)text
           messageId:(NSString *)msgId
            fromUser:(id <IMUIUserProtocol>)fromUser
            timeString:(NSString *)timeString
            isOutgoing:(BOOL)isOutGoing
                status:(IMUIMessageStatus) messageStatus;

- (instancetype)initWithImagePath:(NSString *) mediaPath
           messageId:(NSString *)msgId
            fromUser:(id <IMUIUserProtocol>)fromUser
          timeString:(NSString *)timeString
          isOutgoing:(BOOL)isOutGoing
              status:(IMUIMessageStatus) messageStatus;

- (instancetype)initWithVoicePath:(NSString *) mediaPath
                messageId:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus;

- (instancetype)initWithVideoPath:(NSString *) mediaPath
                messageId:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus;


@end


