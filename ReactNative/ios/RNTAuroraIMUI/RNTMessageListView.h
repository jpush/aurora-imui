//
//  RNTMessageListView.h
//  imuiDemo
//
//  Created by oshumini on 2017/5/26.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <RNTAuroraIMUI/RNTAuroraIMUI-Swift.h>
#import <UIKit/UIKit.h>

#import <React/RCTComponent.h>


@interface RNTMessageListView : UIView
@property(weak, nonatomic) IBOutlet IMUIMessageCollectionView *messageList;
@property(nonatomic, copy) RCTBubblingEventBlock onAvatarClick;
@property(nonatomic, copy) RCTBubblingEventBlock onMsgClick;
@property(nonatomic, copy) RCTBubblingEventBlock onStatusViewClick;

@property(nonatomic, copy) RCTBubblingEventBlock onBeginDragMessageList;


// custom layout
@property(strong, nonatomic) NSString *sendBubbleTextColor;

@property(strong, nonatomic) NSString *receiveBubbleTextColor;

@property(assign, nonatomic) NSNumber *sendBubbleTextSize;

@property(assign, nonatomic) NSNumber *receiveBubbleTextSize;

@property(assign, nonatomic) NSNumber *dateTextSize;

@property(strong, nonatomic) NSString *dateTextColor;

@property(strong, nonatomic) NSDictionary *avatarSize;

@property(assign, nonatomic)BOOL isshowDisplayName;

@property(assign, nonatomic)BOOL isShowOutgoingDisplayName;

@property(assign, nonatomic)BOOL isShowIncommingDisplayName;

@property(strong, nonatomic) NSDictionary *sendBubblePadding;

@property(strong, nonatomic) NSDictionary *receiveBubblePadding;

@end
