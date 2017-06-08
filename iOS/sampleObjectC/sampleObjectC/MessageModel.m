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
@property(strong, nonatomic)NSString *messageText;
@property(strong, nonatomic)NSString *messagemediaPath;
@end

@implementation MessageModel
- (instancetype)init
{
  self = [super init];
  if (self) {
    
  }
  return self;
}

//- (instancetype)initWithMsgId:(NSString *)msgId
//                     fromUser:(id <IMUIUserProtocol>)fromUser
//                   timeString:(NSString *)timeString
//                         text:(NSString *) text
//                   isOutgoing:(BOOL)isOutGoing
//                       status:(IMUIMessageStatus) messageStatus{
//  self = [super init];
//  if (self) {
//    _msgId = msgId;
//    self.fromUser = fromUser;
//    self.timeString = timeString;
//    self.text = text;
//    self.isOutGoing = isOutGoing;
//    self.messageStatus = messageStatus;
//    self.messageType = IMUIMessageTypeText;
//  }
//  return self;
//}

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
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage:isOutGoing isNeedShowTime:false bubbleContentSize:[MessageModel calculateTextContentSizeWithText: text] bubbleContentInsets:contentInset];
  _type = IMUIMessageTypeText;
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
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage:isOutGoing isNeedShowTime:false bubbleContentSize: CGSizeMake(80, 37) bubbleContentInsets: UIEdgeInsetsZero];
  _type = IMUIMessageTypeVoice;
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
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage:isOutGoing isNeedShowTime:false bubbleContentSize: CGSizeMake(120, 160)bubbleContentInsets: UIEdgeInsetsZero];
  
  _type = IMUIMessageTypeImage;
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
  _layout = [[MessageLayout alloc] initWithIsOutGoingMessage:isOutGoing isNeedShowTime:false bubbleContentSize: CGSizeMake(120, 160) bubbleContentInsets: UIEdgeInsetsZero];
  _type = IMUIMessageTypeVideo;
}

+ (CGSize)calculateTextContentSizeWithText:(NSString *)text {
  return [MessageModel getTextSizeWithString:text maxWidth: IMUIMessageCellLayout.bubbleMaxWidth];
}

+ (CGSize)getTextSizeWithString:(NSString *)string maxWidth:(CGFloat)maxWidth {
  CGSize maxSize = CGSizeMake(maxWidth, 2000);
  UIFont *font =[UIFont systemFontOfSize:18];
  NSMutableParagraphStyle *paragraphStyle= [[NSMutableParagraphStyle alloc] init];
  CGSize realSize = [string boundingRectWithSize:maxSize options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:font,NSParagraphStyleAttributeName: paragraphStyle} context: nil].size;
  CGSize imgSize =realSize;

  return imgSize;
}

@end
