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

@property (nonatomic, readonly, strong) id <IMUIMessageCellLayoutProtocal> _Nonnull layout;

@property (nonatomic, readonly, strong) UIImage * _Nonnull resizableBubbleImage;

@property (nonatomic, readonly, copy) NSString * _Nonnull timeString;

- (NSString * _Nonnull)text SWIFT_WARN_UNUSED_RESULT;

- (NSString * _Nonnull)mediaFilePath SWIFT_WARN_UNUSED_RESULT;

@property (nonatomic, readonly) CGFloat duration;

@property (nonatomic, readonly) BOOL isOutGoing;

@property (nonatomic, readonly) enum IMUIMessageStatus messageStatus;

@property (assign, nonatomic) enum IMUIMessageType type;

- (void)setupTextMessage:(NSString *)msgId
                fromUser:(id <IMUIUserProtocol>)fromUser
              timeString:(NSString *)timeString
                    text:(NSString *) text
              isOutgoing:(BOOL)isOutGoing
                  status:(IMUIMessageStatus) messageStatus;

- (void)setupVoiceMessage:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
                mediaPath:(NSString *) mediaPath
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus;

- (void)setupImageMessage:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
                mediaPath:(NSString *) mediaPath
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus;

- (void)setupVideoMessage:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
                mediaPath:(NSString *) mediaPath
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatu;
@end


