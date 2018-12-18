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

@property (assign, nonatomic) NSString * _Nonnull type;



- (instancetype _Nonnull )initWithText:(NSString *_Nonnull)text
                             messageId:(NSString *_Nonnull)msgId
                              fromUser:(id <IMUIUserProtocol>_Nonnull)fromUser
                            timeString:(NSString *_Nullable)timeString
                            isOutgoing:(BOOL)isOutGoing
                                status:(IMUIMessageStatus)messageStatus;

- (instancetype _Nonnull )initWithImagePath:(NSString *_Nonnull)mediaPath
                                  messageId:(NSString *_Nonnull)msgId
                                   fromUser:(id <IMUIUserProtocol>_Nonnull)fromUser
                                 timeString:(NSString *_Nullable)timeString
                                 isOutgoing:(BOOL)isOutGoing
                                     status:(IMUIMessageStatus)messageStatus;

- (instancetype _Nonnull )initWithImageUrl:(NSString *_Nonnull)imgUrl
                                 messageId:(NSString *_Nonnull)msgId
                                  fromUser:(id <IMUIUserProtocol>_Nonnull)fromUser
                                timeString:(NSString *_Nullable)timeString
                                isOutgoing:(BOOL)isOutGoing
                                    status:(IMUIMessageStatus)messageStatus;
  
- (instancetype _Nonnull )initWithVoicePath:(NSString *_Nonnull)mediaPath
                                   duration:(CGFloat)duration
                                  messageId:(NSString *_Nonnull)msgId
                                   fromUser:(id <IMUIUserProtocol>_Nonnull)fromUser
                                 timeString:(NSString *_Nullable)timeString
                                 isOutgoing:(BOOL)isOutGoing
                                     status:(IMUIMessageStatus)messageStatus;

- (instancetype _Nonnull )initWithVideoPath:(NSString *_Nonnull)mediaPath
                                  messageId:(NSString *_Nonnull)msgId
                                   fromUser:(id <IMUIUserProtocol>_Nonnull)romUser
                                 timeString:(NSString *_Nullable)timeString
                                 isOutgoing:(BOOL)isOutGoing
                                     status:(IMUIMessageStatus)messageStatus;


@end


