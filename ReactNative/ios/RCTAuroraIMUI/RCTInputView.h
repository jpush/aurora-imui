//
//  RCTInputView.h
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>

#import <React/RCTComponent.h>

@interface RCTInputView : UIView
@property (weak, nonatomic) IBOutlet IMUIInputView *imuiIntputView;
@property (nonatomic, assign)CGFloat inputTextHeight;
@property (nonatomic, assign)CGFloat keyBoardHeight;

@property (nonatomic, assign)CGFloat maxKeyBoardHeight;

@property(strong, nonatomic) NSString *chatInputBackgroupColor;

@property(strong, nonatomic) NSNumber *galleryScale;
@property(strong, nonatomic) NSNumber *compressionQuality;

@property(strong, nonatomic) NSDictionary *customLayoutItems;

@property (nonatomic, copy) RCTBubblingEventBlock onEventCallBack;

@property (nonatomic, copy) RCTBubblingEventBlock onSizeChange;

@property (nonatomic, copy) RCTBubblingEventBlock onSendText;
@property (nonatomic, copy) RCTBubblingEventBlock onTakePicture;
@property (nonatomic, copy) RCTBubblingEventBlock onStartRecordVoice;
@property (nonatomic, copy) RCTBubblingEventBlock onFinishRecordVoice;
@property (nonatomic, copy) RCTBubblingEventBlock onCancelRecordVoice;

@property (nonatomic, copy) RCTBubblingEventBlock onStartRecordVideo;
@property (nonatomic, copy) RCTBubblingEventBlock onFinishRecordVideo;
@property (nonatomic, copy) RCTBubblingEventBlock onSendGalleryFiles;

@property (nonatomic, copy) RCTBubblingEventBlock onSwitchToMicrophoneMode;
@property (nonatomic, copy) RCTBubblingEventBlock onSwitchToGalleryMode;
@property (nonatomic, copy) RCTBubblingEventBlock onSwitchToCameraMode;
@property (nonatomic, copy) RCTBubblingEventBlock onSwitchToEmojiMode;

@property (nonatomic, copy) RCTBubblingEventBlock onShowKeyboard;

@property (nonatomic, copy) RCTBubblingEventBlock onFullScreen;

@property (nonatomic, copy) RCTBubblingEventBlock onRecoverScreen;

@property(strong, nonatomic) NSDictionary *inputPadding;
@property(strong, nonatomic) NSString *inputTextColor;
@property(strong, nonatomic) NSNumber *inputTextSize;
@end
