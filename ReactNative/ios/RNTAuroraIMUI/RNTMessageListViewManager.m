//
//  IMUIMessageListViewManager.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/23.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTViewManager.h>
#import "RNTMessageListView.h"
#import <RNTAuroraIMUI/RNTAuroraIMUI-Swift.h>

@interface RNTMessageListViewManager : RCTViewManager <IMUIMessageMessageCollectionViewDelegate>

//@property (nonatomic, copy) RCTBubblingEventBlock messageListEventCallBack;
@property (strong, nonatomic)RNTMessageListView *messageList;

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

@implementation RNTMessageListViewManager

RCT_EXPORT_VIEW_PROPERTY(onAvatarClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMsgClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStatusViewClick, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onBeginDragMessageList, RCTBubblingEventBlock)

//@property (nonatomic, copy) RCTBubblingEventBlock onTapHeader;
//@property (nonatomic, copy) RCTBubblingEventBlock onTapContentBubbel;
//@property (nonatomic, copy) RCTBubblingEventBlock onTapStatusView;
//@property (nonatomic, copy) RCTBubblingEventBlock onTapMessageCell;
//
//@property (nonatomic, copy) RCTBubblingEventBlock onBeginDragMessageList;
RCT_EXPORT_MODULE()
- (UIView *)view
{
//  let bundle = Bundle.imuiBundle()
//  view = bundle.loadNibNamed("IMUIMessageCollectionView", owner: self, options: nil)?.first as! UIView
  NSBundle *bundle = [NSBundle bundleForClass: [RNTMessageListView class]];
  
  
  _messageList = [[bundle loadNibNamed:@"RNTMessageListView" owner:self options: nil] objectAtIndex:0];
  _messageList.messageList.delegate = self;
  
  
  
  return _messageList;
  
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubbleTextColor, NSString, RNTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    IMUITextMessageCell.outGoingTextColor = color;
  }
  
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubbleTextColor, NSString, RNTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:@"colorString"];
  if (color != nil) {
    IMUITextMessageCell.inComingTextColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubbleTextSize, NSNumber, RNTMessageListView) {
  NSNumber *textSize = [RCTConvert NSNumber: json];
  IMUITextMessageCell.outGoingTextFont = [UIFont systemFontOfSize:[textSize floatValue]];
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubbleTextSize, NSNumber, RNTMessageListView) {
  NSNumber *textSize = [RCTConvert NSNumber: json];
  IMUITextMessageCell.inComingTextFont = [UIFont systemFontOfSize:[textSize floatValue]];
}

RCT_CUSTOM_VIEW_PROPERTY(dateTextSize, NSNumber, RNTMessageListView) {
  NSNumber *dateTextSize = [RCTConvert NSNumber: json];
  IMUIMessageCellLayout.timeStringFont = [UIFont systemFontOfSize: [dateTextSize floatValue]];
  
}

RCT_CUSTOM_VIEW_PROPERTY(dateTextColor, NSString, RNTMessageListView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:@"colorString"];
  if (color != nil) {
    IMUIMessageCellLayout.timeStringColor = color;
  }
}

RCT_CUSTOM_VIEW_PROPERTY(avatarSize, NSDictionary, RNTMessageListView) {
  NSDictionary *avatarSize = [RCTConvert NSDictionary: json];
  NSNumber *width = avatarSize[@"width"];
  NSNumber *height = avatarSize[@"height"];
  IMUIMessageCellLayout.avatarSize = CGSizeMake([width floatValue], [height floatValue]);
}

RCT_CUSTOM_VIEW_PROPERTY(isShowDisplayName, BOOL, RNTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowInComingName = needShowDisPlayName;
  IMUIMessageCellLayout.isNeedShowOutGoingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(isShowOutgoingDisplayName, BOOL, RNTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowOutGoingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(isShowIncommingDisplayName, BOOL, RNTMessageListView) {
  BOOL needShowDisPlayName = [RCTConvert BOOL: json];
  IMUIMessageCellLayout.isNeedShowInComingName = needShowDisPlayName;
}

RCT_CUSTOM_VIEW_PROPERTY(sendBubblePadding, NSDictionary, RNTMessageListView) {
  NSDictionary *bubblePadding = [RCTConvert NSDictionary: json];
  NSNumber *left = bubblePadding[@"left"];
  NSNumber *top = bubblePadding[@"top"];
  NSNumber *right = bubblePadding[@"right"];
  NSNumber *bottom = bubblePadding[@"bottom"];
  MyMessageCellLayout.outgoingPadding = UIEdgeInsetsMake([top floatValue], [left floatValue], [bottom floatValue], [right floatValue]);
}

RCT_CUSTOM_VIEW_PROPERTY(receiveBubblePadding, NSDictionary, RNTMessageListView) {
  NSDictionary *bubblePadding = [RCTConvert NSDictionary: json];
  NSNumber *left = bubblePadding[@"left"];
  NSNumber *top = bubblePadding[@"top"];
  NSNumber *right = bubblePadding[@"right"];
  NSNumber *bottom = bubblePadding[@"bottom"];
  MyMessageCellLayout.incommingPadding = UIEdgeInsetsMake([top floatValue], [left floatValue], [bottom floatValue], [right floatValue]);
}

- (RNTMessageModel *)convertMessageDicToModel:(NSDictionary *)message {
  return [[RNTMessageModel alloc] initWithMessageDic: message];
}

// - MARK: IMUIMessageCollectionViewDelegate

//- (void)sendEventWithType:(NSString *)type model:(id)model {
//  if(!_messageList.onEventCallBack) { return; }
//  
//  NSMutableDictionary *event = @{}.mutableCopy;
//  RNTMessageModel *message = model;
//  NSDictionary *msgDic = message.messageDictionary;
//  event[@"message"] = msgDic;
//  event[@"type"] = type;
//  _messageList.onEventCallBack(event);/Users/HuminiOS/Desktop/myproject/reactNative/testheiheihei/index.ios.js
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
    RNTMessageModel *message = model;
    NSDictionary *messageDic = message.messageDictionary;
    _messageList.onMsgClick((@{@"message": messageDic}));
}

/// Tells the delegate that user tap message cell
//self.delegate?.messageCollectionView(didTapMessageBubbleInCell: self, model: self.message!)
- (void)messageCollectionView:(UICollectionView *)_ forItemAt:(NSIndexPath * _Nonnull)forItemAt model:(id)model {
}

/// Tells the delegate that user tap header image in message cell
- (void)messageCollectionViewWithDidTapHeaderImageInCell:(UICollectionViewCell * _Nonnull)didTapHeaderImageInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model {

  if(!_messageList.onAvatarClick) { return; }
  RNTMessageModel *message = model;
  NSDictionary *messageDic = message.messageDictionary;
  _messageList.onAvatarClick(@{@"message": messageDic});
}
/// Tells the delegate that user tap statusView in message cell
- (void)messageCollectionViewWithDidTapStatusViewInCell:(UICollectionViewCell * _Nonnull)didTapStatusViewInCell model:(id <IMUIMessageModelProtocol> _Nonnull)model {

  if(!_messageList.onStatusViewClick) { return; }
  RNTMessageModel *message = model;
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

@end
