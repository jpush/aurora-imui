//
//  RNTInputView.h
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <RNTAuroraIMUI/RNTAuroraIMUI-Swift.h>

#import <React/RCTComponent.h>

@interface RNTInputView : UIView
@property (weak, nonatomic) IBOutlet IMUIInputView *imuiIntputView;

@property (nonatomic, copy) RCTBubblingEventBlock onEventCallBack;

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

@property (nonatomic, copy) RCTBubblingEventBlock onShowKeyboard;
@end
