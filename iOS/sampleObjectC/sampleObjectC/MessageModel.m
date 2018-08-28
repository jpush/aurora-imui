//
//  MessageModel.m
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "MessageModel.h"
#import "MessageLayout.h"
#import <CoreGraphics/CoreGraphics.h>

@interface MessageModel()
@property(nonatomic, copy) NSString *messageText;
@property(nonatomic, copy) NSString *messagemediaPath;
@property (nonatomic, copy) NSString *imageUrl;

@end

@implementation MessageModel
- (instancetype)init
{
  self = [super init];
  if (self) {
    
  }
  return self;
}

- (void)setupTextMessage:(NSString *)msgId
               fromUser:(id <IMUIUserProtocol>)fromUser
             timeString:(NSString *)timeString
                   text:(NSString *) text
             isOutgoing:(BOOL)isOutGoing
                 status:(IMUIMessageStatus) messageStatus {
  _msgId = msgId;
  _fromUser = fromUser;
  _timeString = timeString;
  _messageText = text;
  _isOutGoing = isOutGoing;
  _messageStatus = messageStatus;
  
  UIEdgeInsets contentInset = UIEdgeInsetsZero;
  if (isOutGoing) {
    contentInset = UIEdgeInsetsMake(10, 10, 10, 10);
  } else {
    contentInset = UIEdgeInsetsMake(10, 15, 10, 10);
  }
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                              isNeedShowTime: false
                                           bubbleContentSize: [MessageModel calculateTextContentSizeWithText: text]
                                         bubbleContentInsets: contentInset
                                        timeLabelContentSize: CGSizeZero
                                                 contentType: @"Text"];
  _type = @"Text";
}

- (NSString *)getText {
  return _messageText;
}

- (NSString *)text {
  return _messageText;
}

- (NSString *)getMediaPath {
  return _messagemediaPath;
}

- (NSString *)mediaFilePath {
  return _messagemediaPath;
}

- (NSString *)webImageUrl {
  return _imageUrl;
}

- (UIImage *)resizableBubbleImage {
  UIImage *bubbleImg = nil;
  
  if (_isOutGoing) {
    bubbleImg = [UIImage imageNamed:@"outGoing_bubble"];
    bubbleImg = [bubbleImg resizableImageWithCapInsets:UIEdgeInsetsMake(24, 10, 9, 15) resizingMode:UIImageResizingModeTile];
  } else {
    bubbleImg = [UIImage imageNamed:@"inComing_bubble"];
    bubbleImg = [bubbleImg resizableImageWithCapInsets:UIEdgeInsetsMake(24, 15, 9, 10) resizingMode:UIImageResizingModeTile];
  }
  return bubbleImg;
}

- (void)setupVoiceMessage:(NSString *)msgId
               fromUser:(id <IMUIUserProtocol>)fromUser
             timeString:(NSString *)timeString
                   mediaPath:(NSString *) mediaPath
             isOutgoing:(BOOL)isOutGoing
                 status:(IMUIMessageStatus) messageStatus {
  _msgId = msgId;
  _fromUser = fromUser;
  _timeString = timeString;
  _messagemediaPath = mediaPath;
  _isOutGoing = isOutGoing;
  _messageStatus = messageStatus;
  
  BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
  CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 28) : CGSizeZero;
  
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                              isNeedShowTime: isNeedShowTime
                                           bubbleContentSize: CGSizeMake(80, 37)
                                         bubbleContentInsets: UIEdgeInsetsZero
                                        timeLabelContentSize: timeLabelSize
                                                 contentType: @"Voice"];
  _type = @"Voice";
}

- (void)setupImageMessage:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
                mediaPath:(NSString *) mediaPath
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus {
  _msgId = msgId;
  _fromUser = fromUser;
  _timeString = timeString;
  _messagemediaPath = mediaPath;
  _isOutGoing = isOutGoing;
  _messageStatus = messageStatus;
  
  BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
  CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 28) : CGSizeZero;
  
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                              isNeedShowTime: isNeedShowTime
                                           bubbleContentSize: CGSizeMake(120, 160)
                                         bubbleContentInsets: UIEdgeInsetsZero
                                        timeLabelContentSize: timeLabelSize
                                                 contentType: @"Image"];
  
  _type = @"Image";
}



- (void)setupVideoMessage:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
                mediaPath:(NSString *) mediaPath
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus {
  _msgId = msgId;
  _fromUser = fromUser;
  _timeString = timeString;
  _messagemediaPath = mediaPath;
  
  _isOutGoing = isOutGoing;
  _messageStatus = messageStatus;
  
  BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
  CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 28) : CGSizeZero;
  
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                              isNeedShowTime: isNeedShowTime
                                           bubbleContentSize: CGSizeMake(120, 160)
                                         bubbleContentInsets: UIEdgeInsetsZero
                                        timeLabelContentSize: timeLabelSize
                                                 contentType: @"Video"];
  _type = @"Video";
}

- (instancetype)initWithText:(NSString *)text
           messageId:(NSString *)msgId
            fromUser:(id <IMUIUserProtocol>)fromUser
          timeString:(NSString *)timeString
          isOutgoing:(BOOL)isOutGoing
              status:(IMUIMessageStatus) messageStatus {

  self = [super init];
  if (self) {
    _msgId = msgId;
    _fromUser = fromUser;
    _timeString = timeString;
    _messageText = text;
    _isOutGoing = isOutGoing;
    _messageStatus = messageStatus;
    
    UIEdgeInsets contentInset = UIEdgeInsetsZero;
    if (isOutGoing) {
      contentInset = UIEdgeInsetsMake(10, 10, 10, 10);
    } else {
      contentInset = UIEdgeInsetsMake(10, 15, 10, 10);
    }
    BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
    CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 14) : CGSizeZero;
    
    _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                                isNeedShowTime: isNeedShowTime
                                             bubbleContentSize: [MessageModel calculateTextContentSizeWithText: text]
                                           bubbleContentInsets: contentInset
                                          timeLabelContentSize: timeLabelSize
                                                   contentType: @"Text"];
    _type = @"Text";
  }
  return self;
}

- (instancetype)initWithImagePath:(NSString *) mediaPath
                messageId:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus {

  self = [super init];
  if (self) {
    _msgId = msgId;
    _fromUser = fromUser;
    _timeString = timeString;
    _messagemediaPath = mediaPath;
    _isOutGoing = isOutGoing;
    _messageStatus = messageStatus;
    
    BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
    CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 14) : CGSizeZero;
    
    _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                                isNeedShowTime: isNeedShowTime
                                             bubbleContentSize: CGSizeMake(120, 160)
                                           bubbleContentInsets: UIEdgeInsetsZero
                                          timeLabelContentSize: timeLabelSize
                                                   contentType: @"Image"];
    
    _type = @"Image";
  }
  return self;
}



- (instancetype)initWithImageUrl: (NSString *) imgUrl
                       messageId: (NSString *)msgId
                        fromUser: (id <IMUIUserProtocol>)fromUser
                      timeString: (NSString *)timeString
                      isOutgoing: (BOOL)isOutGoing
                          status: (IMUIMessageStatus) messageStatus {
  
  self = [super init];
  if (self) {
    _msgId = msgId;
    _fromUser = fromUser;
    _timeString = timeString;
    _isOutGoing = isOutGoing;
    _messageStatus = messageStatus;
    _imageUrl = imgUrl;
    
    BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
    CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 14) : CGSizeZero;
    
    _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                                isNeedShowTime: isNeedShowTime
                                             bubbleContentSize: CGSizeMake(120, 160)
                                           bubbleContentInsets: UIEdgeInsetsZero
                                          timeLabelContentSize: timeLabelSize
                                                   contentType: @"Image"];
    
    _type = @"Image";
  }
  return self;
}

- (instancetype)initWithVoicePath:(NSString *) mediaPath
                         duration:(CGFloat)duration
                        messageId:(NSString *)msgId
                         fromUser:(id <IMUIUserProtocol>)fromUser
                       timeString:(NSString *)timeString
                       isOutgoing:(BOOL)isOutGoing
                           status:(IMUIMessageStatus) messageStatus {

  self = [super init];
  if (self) {
    _msgId = msgId;
    _fromUser = fromUser;
    _timeString = timeString;
    _messagemediaPath = mediaPath;
    _isOutGoing = isOutGoing;
    _messageStatus = messageStatus;
    _duration = duration;
    
    BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
    CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 14) : CGSizeZero;
    
    _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                                isNeedShowTime: isNeedShowTime
                                             bubbleContentSize: CGSizeMake(80, 37)
                                           bubbleContentInsets: UIEdgeInsetsZero
                                          timeLabelContentSize: timeLabelSize
                                                   contentType: @"Voice"];
    _type = @"Voice";
  }
  return self;
}

- (instancetype)initWithVideoPath:(NSString *) mediaPath
                messageId:(NSString *)msgId
                 fromUser:(id <IMUIUserProtocol>)fromUser
               timeString:(NSString *)timeString
               isOutgoing:(BOOL)isOutGoing
                   status:(IMUIMessageStatus) messageStatus {

  self = [super init];
  if (self) {
    _msgId = msgId;
    _fromUser = fromUser;
    _timeString = timeString;
    _messagemediaPath = mediaPath;
    _isOutGoing = isOutGoing;
    _messageStatus = messageStatus;
    
    BOOL isNeedShowTime = timeString != nil && ![timeString isEqualToString:@""];
    CGSize timeLabelSize = isNeedShowTime ? CGSizeMake(200, 14) : CGSizeZero;
    
    _layout = [[MessageLayout alloc] initWithIsOutGoingMessage: isOutGoing
                                                isNeedShowTime: isNeedShowTime
                                             bubbleContentSize: CGSizeMake(120, 160)
                                           bubbleContentInsets: UIEdgeInsetsZero
                                          timeLabelContentSize: timeLabelSize
                                                   contentType: @"Video"];
    _type = @"Video";
  }
  return self;
}


+ (CGSize)calculateTextContentSizeWithText:(NSString *)text {
  return [MessageModel getTextSizeWithString:text maxWidth: IMUIMessageCellLayout.bubbleMaxWidth];
}

+ (CGSize)getTextSizeWithString:(NSString *)string maxWidth:(CGFloat)maxWidth {
  CGSize maxSize = CGSizeMake(maxWidth, 2000);
  UIFont *font =[UIFont systemFontOfSize:18];
  NSMutableParagraphStyle *paragraphStyle= [[NSMutableParagraphStyle alloc] init];
  CGSize realSize = [string boundingRectWithSize:maxSize options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:font,NSParagraphStyleAttributeName: paragraphStyle} context: nil].size;
  
  return realSize;
}

@end
