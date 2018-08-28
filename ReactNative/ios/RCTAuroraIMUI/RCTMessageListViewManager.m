//
//  IMUIMessageListViewManager.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/23.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTViewManager.h>
#import "RCTMessageListView.h"
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>

@interface RCTMessageListViewManager : RCTViewManager <IMUIMessageMessageCollectionViewDelegate>

//@property (nonatomic, copy) RCTBubblingEventBlock messageListEventCallBack;
@property (strong, nonatomic)RCTMessageListView *messageList;

/// Tells the delegate that user tap message cell
- (void)messageCollectionView:(UICollectionView * _Nonnull)_ forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate that user tap message bubble
- (void)messageCollectionViewWithDidTapMessageBubbleInCell:(UICollectionViewCell * _Nonnull)didTapMessageBubbleInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate that user tap header image in message cell
- (void)messageCollectionViewWithDidTapHeaderImageInCell:(UICollectionViewCell * _Nonnull)didTapHeaderImageInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate that user tap statusView in message cell
- (void)messageCollectionViewWithDidTapStatusViewInCell:(UICollectionViewCell * _Nonnull)didTapStatusViewInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate that the message cell will show in screen
- (void)messageCollectionView:(UICollectionView * _Nonnull)_ willDisplayMessageCell:(UICollectionViewCell * _Nonnull)willDisplayMessageCell forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate that message cell end displaying
- (void)messageCollectionView:(UICollectionView * _Nonnull)_ didEndDisplaying:(UICollectionViewCell * _Nonnull)didEndDisplaying forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id <IMUIMessageModelProtocol> _Nonnull)model;
/// Tells the delegate when messageCollection beginDragging
- (void)messageCollectionView:(UICollectionView * _Nonnull)willBeginDragging;

@end

@implementation RCTMessageListViewManager

RCT_EXPORT_VIEW_PROPERTY(onAvatarClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMsgClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMsgLongClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStatusViewClick, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onTouchMsgList, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBeginDragMessageList, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPullToRefresh, RCTBubblingEventBlock)

static NSString *cellIdentify = nil;
RCT_EXPORT_MODULE()
- (UIView *)view
{
  NSBundle *bundle = [NSBundle bundleForClass: [IMUIMessageCollectionView class]];
  
  _messageList = [[bundle loadNibNamed:@"RCTMessageListView" owner:self options: nil] objectAtIndex:0];
  _messageList.messageList.delegate = self;
  _messageList.delegate = self;
  
  cellIdentify = nil;
  return _messageList;
  
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubble, NSDictionary, RCTMessageListView) {
  NSDictionary *bubbleDic = [RCTConvert NSDictionary: json];
  NSString *bubbleName = bubbleDic[@"imageName"];
  
  if (bubbleName == nil) { return; }
  if (bubbleDic[@"padding"] == nil) { return; }
  UIEdgeInsets padding = [self edgeInsetsWith:bubbleDic[@"padding"]];
  
  UIImage *bubbleImg = [UIImage imageNamed:bubbleName];
  bubbleImg = [bubbleImg resizableImageWithCapInsets: padding
                                        resizingMode: UIImageResizingModeTile];
  RCTMessageModel.outgoingBubbleImage = bubbleImg;
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubble, NSDictionary, RCTMessageListView) {
  NSDictionary *bubbleDic = [RCTConvert NSDictionary: json];
  NSString *bubbleName = bubbleDic[@"imageName"];
  
  if (bubbleName == nil) { return; }
  if (bubbleDic[@"padding"] == nil) { return; }
  
  UIEdgeInsets padding = [self edgeInsetsWith:bubbleDic[@"padding"]];
  UIImage *bubbleImg = [UIImage imageNamed:bubbleName];
  bubbleImg = [bubbleImg resizableImageWithCapInsets: padding
                                        resizingMode: UIImageResizingModeTile];
  RCTMessageModel.incommingBubbleImage = bubbleImg;
  
}

RCT_CUSTOM_VIEW_PROPERTY(messageListBackgroundColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    _messageList.messageList.messageCollectionView.backgroundColor = color;
  }
  
}

RCT_CUSTOM_VIEW_PROPERTY(maxBubbleWidth, NSNumber, RCTMessageListView) {
  NSNumber *widthPercent = [RCTConvert NSNumber: json];
  
  IMUIMessageCellLayout.bubbleMaxWidth = UIScreen.mainScreen.bounds.size.width * widthPercent.floatValue;
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubbleTextColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUITextMessageContentView.outGoingTextColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubbleTextColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUITextMessageContentView.inComingTextColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubbleTextSize, NSNumber, RCTMessageListView) {
  NSNumber *textSize = [RCTConvert NSNumber: json];
  IMUITextMessageContentView.outGoingTextFont = [UIFont systemFontOfSize:[textSize floatValue]];
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubbleTextSize, NSNumber, RCTMessageListView) {
  NSNumber *textSize = [RCTConvert NSNumber: json];
  IMUITextMessageContentView.inComingTextFont = [UIFont systemFontOfSize:[textSize floatValue]];
}

RCT_CUSTOM_VIEW_PROPERTY(dateTextSize, NSNumber, RCTMessageListView) {
  NSNumber *dateTextSize = [RCTConvert NSNumber: json];
  IMUIMessageCellLayout.timeStringFont = [UIFont systemFontOfSize: [dateTextSize floatValue]];
  
}

RCT_CUSTOM_VIEW_PROPERTY(dateTextColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUIMessageCellLayout.timeStringColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(avatarSize, NSDictionary, RCTMessageListView) {
  NSDictionary *avatarSize = [RCTConvert NSDictionary: json];
  NSNumber *width = avatarSize[@"width"];
  NSNumber *height = avatarSize[@"height"];
  IMUIMessageCellLayout.avatarSize = CGSizeMake([width floatValue], [height floatValue]);
}

RCT_CUSTOM_VIEW_PROPERTY(isShowDisplayName, BOOL, RCTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowInComingName = needShowDisPlayName;
  IMUIMessageCellLayout.isNeedShowOutGoingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(isShowOutgoingDisplayName, BOOL, RCTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowOutGoingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(isShowIncomingDisplayName, BOOL, RCTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowInComingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(isAllowPullToRefresh, BOOL, RCTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  _messageList.isAllowPullToRefresh = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubblePadding, NSDictionary, RCTMessageListView) {
  NSDictionary *paddingDic = [RCTConvert NSDictionary: json];
  UIEdgeInsets padding = [self edgeInsetsWith:paddingDic];
  MyMessageCellLayout.outgoingPadding = padding;
}

RCT_CUSTOM_VIEW_PROPERTY(avatarCornerRadius, NSDictionary, RCTMessageListView) {
  NSNumber *cornerRadius = [RCTConvert NSNumber: json];
  IMUIBaseMessageCell.avatarCornerRadius = [cornerRadius floatValue];
  
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubblePadding, NSDictionary, RCTMessageListView) {
  NSDictionary *paddingDic = [RCTConvert NSDictionary: json];
  UIEdgeInsets padding = [self edgeInsetsWith:paddingDic];
  MyMessageCellLayout.incommingPadding = padding;
}

RCT_CUSTOM_VIEW_PROPERTY(eventTextColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    MessageEventCollectionViewCell.eventTextColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(eventTextSize, NSNumber, RCTMessageListView) {
  NSNumber *eventTextSize = [RCTConvert NSNumber: json];
  MessageEventCollectionViewCell.eventFont = [UIFont systemFontOfSize:eventTextSize.floatValue];
}

RCT_CUSTOM_VIEW_PROPERTY(eventTextPadding, NSDictionary, RCTMessageListView) {
  NSDictionary *paddingDic = [RCTConvert NSDictionary: json];
  UIEdgeInsets padding = [self edgeInsetsWith:paddingDic];
  MessageEventCollectionViewCell.contentInset = padding;
}

RCT_CUSTOM_VIEW_PROPERTY(displayNamePadding, NSDictionary, RCTMessageListView) {
  NSDictionary *paddingDic = [RCTConvert NSDictionary: json];
  UIEdgeInsets padding = [self edgeInsetsWith:paddingDic];
  IMUIMessageCellLayout.nameLabelPadding = padding;
}


RCT_CUSTOM_VIEW_PROPERTY(dateBackgroundColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUIMessageCellLayout.timeStringBackgroundColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(eventBackgroundColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    MessageEventCollectionViewCell.eventBackgroundColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(displayNameTextColor, NSString, RCTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUIMessageCellLayout.nameLabelTextColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(dateCornerRadius, NSNumber, RCTMessageListView) {
  NSNumber *cornerRadius = [RCTConvert NSNumber: json];
  IMUIMessageCellLayout.timeStringCornerRadius = cornerRadius.floatValue;
}

RCT_CUSTOM_VIEW_PROPERTY(eventCornerRadius, NSNumber, RCTMessageListView) {
  NSNumber *cornerRadius = [RCTConvert NSNumber: json];
  MessageEventCollectionViewCell.eventCornerRadius = cornerRadius.floatValue;
}

RCT_CUSTOM_VIEW_PROPERTY(displayNameTextSize, NSNumber, RCTMessageListView) {
  NSNumber *textSize = [RCTConvert NSNumber: json];
  IMUIMessageCellLayout.nameLabelTextFont = [UIFont systemFontOfSize:[textSize floatValue]];
  IMUIMessageCellLayout.nameLabelSize = CGSizeMake(IMUIMessageCellLayout.nameLabelSize.width, textSize.floatValue);
}

RCT_CUSTOM_VIEW_PROPERTY(datePadding, NSDictionary, RCTMessageListView) {
  NSDictionary *paddingDic = [RCTConvert NSDictionary: json];
  UIEdgeInsets padding = [self edgeInsetsWith:paddingDic];
  IMUIMessageCellLayout.timeLabelPadding = padding;
  // TODO:
}

RCT_CUSTOM_VIEW_PROPERTY(eventTextLineHeight, NSNumber, RCTMessageListView) {
  NSNumber *lineHeight = [RCTConvert NSNumber: json];
  // TODO:
}

RCT_CUSTOM_VIEW_PROPERTY(messageTextLineHeight, NSNumber, RCTMessageListView) {
  NSNumber *lineHeight = [RCTConvert NSNumber: json];
  // TODO:
}

///////////////////=============

- (RCTMessageModel *)convertMessageDicToModel:(NSDictionary *)message {
  return [[RCTMessageModel alloc] initWithMessageDic: message];
}

- (UIEdgeInsets)edgeInsetsWith:(NSDictionary *)dic {
  NSNumber *left = dic[@"left"];
  NSNumber *top = dic[@"top"];
  NSNumber *right = dic[@"right"];
  NSNumber *bottom = dic[@"bottom"];
  return UIEdgeInsetsMake([top floatValue], [left floatValue], [bottom floatValue], [right floatValue]);
}


// - MARK: IMUIMessageCollectionViewDelegate

//- (void)sendEventWithType:(NSString *)type model:(id)model {
//  if(!_messageList.onEventCallBack) { return; }
//  
//  NSMutableDictionary *event = @{}.mutableCopy;
//  RCTMessageModel *message = model;
//  NSDictionary *msgDic = message.messageDictionary;
//  event[@"message"] = msgDic;
//  event[@"type"] = type;
//  _messageList.onEventCallBack(event);
//}

//- (void)sendEventWithType:(NSString *)type {
//  if(!_messageList.onEventCallBack) { return; }
//  
//  NSMutableDictionary *event = @{}.mutableCopy;
//  event[@"type"] = type;
//  _messageList.onEventCallBack(event);
//}

/// Tells the delegate that user tap message bubble
- (void)messageCollectionViewWithDidTapMessageBubbleInCell:(UICollectionViewCell *)didTapMessageBubbleInCell model:(id)model {

    if(!_messageList.onMsgClick) { return; }
    RCTMessageModel *message = model;
    NSDictionary *messageDic = message.messageDictionary;
    _messageList.onMsgClick((@{@"message": messageDic}));
}

/// Tells the delegate that user tap message bubble
- (void)messageCollectionViewWithBeganLongTapMessageBubbleInCell:(UICollectionViewCell * _Nonnull)beganLongTapMessageBubbleInCell model:(id <IMUIMessageProtocol> _Nonnull)model {
  if(!_messageList.onMsgLongClick) { return; }
  RCTMessageModel *message = model;
  NSDictionary *messageDic = message.messageDictionary;
  _messageList.onMsgLongClick((@{@"message": messageDic}));
}

/// Tells the delegate that user tap message cell
//self.delegate?.messageCollectionView(didTapMessageBubbleInCell: self, model: self.message!)
- (void)messageCollectionView:(UICollectionView *)_ forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id)model {
  if(!_messageList.onTouchMsgList) { return; }
  _messageList.onTouchMsgList(@{});
}

/// Tells the delegate that user tap header image in message cell
- (void)messageCollectionViewWithDidTapHeaderImageInCell:(UICollectionViewCell * _Nonnull)didTapHeaderImageInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model {

  if(!_messageList.onAvatarClick) { return; }
  RCTMessageModel *message = model;
  NSDictionary *messageDic = message.messageDictionary;
  _messageList.onAvatarClick(@{@"message": messageDic});
}
/// Tells the delegate that user tap statusView in message cell
- (void)messageCollectionViewWithDidTapStatusViewInCell:(UICollectionViewCell * _Nonnull)didTapStatusViewInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model {

  if(!_messageList.onStatusViewClick) { return; }
  RCTMessageModel *message = model;
  NSDictionary *messageDic = message.messageDictionary;
  _messageList.onStatusViewClick((@{@"message": messageDic}));
}

/// Tells the delegate that the message cell will show in screen
- (void)messageCollectionView:(UICollectionView * _Nonnull)aaa willDisplayMessageCell:(UICollectionViewCell * _Nonnull)willDisplayMessageCell forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id <IMUIMessageModelProtocol> _Nonnull)model {
  
}
/// Tells the delegate that message cell end displaying
- (void)messageCollectionView:(UICollectionView * _Nonnull)_ didEndDisplaying:(UICollectionViewCell * _Nonnull)didEndDisplaying forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id <IMUIMessageModelProtocol> _Nonnull)model {
  
}
/// Tells the delegate when messageCollection beginDragging
- (void)messageCollectionView:(UICollectionView * _Nonnull)willBeginDragging {
  if(!_messageList.onBeginDragMessageList) { return; }
  
  _messageList.onBeginDragMessageList(@{});
}


/// return a messageCell, it will show in messageList. Can use it to show message event or anything.
/// @optional function
/// (NOTE:  1. You need append a model in IMUIMessageMessageCollectionView frist.
/// 2. If it is not a custom message, you should return nil)
- (UICollectionViewCell * _Nullable)messageCollectionViewWithMessageCollectionView:(UICollectionView * _Nonnull)messageCollectionView forItemAt:(NSIndexPath * _Nonnull)forItemAt messageModel:(id <IMUIMessageProtocol> _Nonnull)messageModel SWIFT_WARN_UNUSED_RESULT {
  
  if ([messageModel isKindOfClass: MessageEventModel.class]) {
    cellIdentify = [[MessageEventCollectionViewCell class] description];
    [messageCollectionView registerClass:[MessageEventCollectionViewCell class] forCellWithReuseIdentifier:cellIdentify];
    
    MessageEventCollectionViewCell *cell = [messageCollectionView  dequeueReusableCellWithReuseIdentifier: cellIdentify forIndexPath: forItemAt];
    MessageEventModel *event = messageModel;
    [cell presentCellWithEvent: event];
    return cell;
  } else {
    return nil;
  }
}

- (NSNumber * _Nullable)messageCollectionViewWithMessageCollectionView:(UICollectionView * _Nonnull)messageCollectionView heightForItemAtIndexPath:(NSIndexPath * _Nonnull)forItemAt messageModel:(id <IMUIMessageProtocol> _Nonnull)messageModel SWIFT_WARN_UNUSED_RESULT {
  
  if ([messageModel isKindOfClass: MessageEventModel.class]) {
    MessageEventModel *event = messageModel;
    return @([event cellHeight]);
  } else {
    return nil;
  }
}


- (void)onPullToRefreshMessageList {
  if(!_messageList.onPullToRefresh) { return; }
  
  _messageList.onPullToRefresh(@{});
}


@end
