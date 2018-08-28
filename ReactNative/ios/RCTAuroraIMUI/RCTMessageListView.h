//
//  RCTMessageListView.h
//  imuiDemo
//
//  Created by oshumini on 2017/5/26.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>
#import <UIKit/UIKit.h>

#import <React/RCTComponent.h>

@protocol RCTMessageListDelegate <NSObject>
@optional
- (void)onPullToRefreshMessageList;
@end

@interface RCTMessageListView : UIView
@property(weak, nonatomic) id<RCTMessageListDelegate> delegate;
@property(weak, nonatomic) IBOutlet IMUIMessageCollectionView *messageList;
@property(strong, nonatomic)UIRefreshControl *refreshControl;

@property(nonatomic, copy) RCTBubblingEventBlock onAvatarClick;
@property(nonatomic, copy) RCTBubblingEventBlock onMsgClick;
@property(nonatomic, copy) RCTBubblingEventBlock onMsgLongClick;
@property(nonatomic, copy) RCTBubblingEventBlock onStatusViewClick;

@property(nonatomic, copy) RCTBubblingEventBlock onTouchMsgList;
@property(nonatomic, copy) RCTBubblingEventBlock onBeginDragMessageList;
@property (nonatomic, copy) RCTBubblingEventBlock onPullToRefresh;

// custom layout
//maxBubbleWidth
@property(assign, nonatomic) CGFloat maxBubbleWidth;

@property(copy, nonatomic) NSString *messageListBackgroundColor;

@property(strong, nonatomic) NSDictionary *sendBubble;

@property(strong, nonatomic) NSDictionary *receiveBubble;

@property(copy, nonatomic) NSString *sendBubbleTextColor;

@property(copy, nonatomic) NSString *receiveBubbleTextColor;

@property(assign, nonatomic) NSNumber *sendBubbleTextSize;

@property(assign, nonatomic) NSNumber *receiveBubbleTextSize;

@property(assign, nonatomic) NSNumber *dateTextSize;

@property(copy, nonatomic) NSString *dateTextColor;

@property(strong, nonatomic) NSDictionary *avatarSize;

@property(assign, nonatomic)BOOL isshowDisplayName;

@property(assign, nonatomic)BOOL isShowOutgoingDisplayName;

@property(assign, nonatomic)BOOL isShowIncomingDisplayName;

@property(assign, nonatomic)BOOL isAllowPullToRefresh;

@property(strong, nonatomic) NSDictionary *sendBubblePadding;

@property(strong, nonatomic) NSDictionary *receiveBubblePadding;




// TODO:
@property(strong, nonatomic) NSDictionary *datePadding;
@property(copy, nonatomic) NSString *dateBackgroundColor;
@property(assign, nonatomic) NSNumber *dateCornerRadius;

@property(strong, nonatomic) NSDictionary *eventTextPadding;
@property(copy, nonatomic) NSString *eventBackgroundColor;
@property(assign, nonatomic) NSNumber *eventCornerRadius;
@property(assign, nonatomic) NSNumber *eventTextLineHeight; // TODO:
@property(copy, nonatomic) NSString *eventTextColor;
@property(assign, nonatomic) NSNumber *eventTextSize;

@property(assign, nonatomic) NSNumber *displayNameTextSize;
@property(copy, nonatomic) NSString *displayNameTextColor;
@property(strong, nonatomic) NSDictionary *displayNamePadding;

@property(assign, nonatomic) NSNumber *messageTextLineHeight;// TODO:

@end
