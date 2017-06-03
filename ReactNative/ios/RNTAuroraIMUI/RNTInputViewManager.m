//
//  RNTInputViewManager.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import "RNTInputView.h"
#import <RNTAuroraIMUI/RNTAuroraIMUI-Swift.h>
#import <Photos/Photos.h>

@interface RNTInputViewManager : RCTViewManager <IMUIInputViewDelegate>

@property (strong, nonatomic)RNTInputView *rntInputView;
@end

@implementation RNTInputViewManager

//RCT_EXPORT_VIEW_PROPERTY(onEventCallBack, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onSendText, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTakePicture, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStartRecordVoice, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onCancelRecordVoice, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onFinishRecordVoice, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onStartRecordVideo, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onFinishRecordVideo, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSendGalleryFiles, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onSwitchToMicrophoneMode, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSwitchToGalleryMode, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSwitchToCameraMode, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onShowKeyboard, RCTBubblingEventBlock)

RCT_EXPORT_MODULE()
- (UIView *)view
{
  NSBundle *bundle = [NSBundle bundleForClass: [RNTInputView class]];
  _rntInputView = [[bundle loadNibNamed:@"RNTInputView" owner:self options: nil] objectAtIndex:0];
  _rntInputView.imuiIntputView.inputViewDelegate = self;
  
  return _rntInputView;
}

/// Tells the delegate that user tap send button and text input string is not empty
- (void)sendTextMessage:(NSString * _Nonnull)messageText {
  if(!_rntInputView.onSendText) { return; }
  
  _rntInputView.onSendText(@{@"text": messageText});
}

/// Tells the delegate that IMUIInputView will switch to recording voice mode
- (void)switchToMicrophoneModeWithRecordVoiceBtn:(UIButton * _Nonnull)recordVoiceBtn {

  if(!_rntInputView.onSwitchToMicrophoneMode) { return; }
  _rntInputView.onSwitchToMicrophoneMode(@{});
}

/// Tells the delegate that start record voice
- (void)startRecordVoice {
  if(!_rntInputView.onStartRecordVoice) { return; }
  _rntInputView.onStartRecordVoice(@{});
}

/// Tells the delegate when finish record voice
- (void)finishRecordVoice:(NSString * _Nonnull)voicePath durationTime:(double)durationTime {
  if(!_rntInputView.onFinishRecordVoice) { return; }
  _rntInputView.onFinishRecordVoice(@{@"mediaPath": voicePath, @"durationTime": @(durationTime)});
}

/// Tells the delegate that user cancel record
- (void)cancelRecordVoice {
  if(!_rntInputView.onCancelRecordVoice) { return; }
  _rntInputView.onCancelRecordVoice(@{});
}

/// Tells the delegate that IMUIInputView will switch to gallery
- (void)switchToGalleryModeWithPhotoBtn:(UIButton * _Nonnull)photoBtn {
  if(!_rntInputView.onSwitchToGalleryMode) { return; }
  _rntInputView.onSwitchToGalleryMode(@{});
}

/// Tells the delegate that user did selected Photo in gallery
- (void)didSeletedGalleryWithAssetArr:(NSArray<PHAsset *> * _Nonnull)AssetArr {
  
  if(!_rntInputView.onSendGalleryFiles) { return; }
  __block NSMutableArray *imagePathArr = @[].mutableCopy;
  
  for (PHAsset *asset in AssetArr) {
    switch (asset.mediaType) {
      case PHAssetMediaTypeImage: {
        
        PHImageRequestOptions *options = [[PHImageRequestOptions alloc]init];
        options.synchronous  = YES;
        PHCachingImageManager *imageManage = [[PHCachingImageManager alloc] init];
        [imageManage requestImageForAsset: asset
                               targetSize: CGSizeMake(100.0, 100.0)
                              contentMode:PHImageContentModeDefault
                                  options:options resultHandler:^(UIImage * _Nullable result, NSDictionary * _Nullable info) {
                                    NSData *imageData = UIImagePNGRepresentation(result);
                                    NSString *filePath = [self getPath];
                                    if ([imageData writeToFile: filePath atomically: true]) {
                                      [imagePathArr addObject: @{@"mediaPath": filePath, @"mediaType": @"image"}];
                                    }
                                  }];
        break;
      }
        
      default:
        break;
    }
  }
  
//  dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//    sleep(1);
      _rntInputView.onSendGalleryFiles(@{@"mediaFiles": imagePathArr});
//  });
  
  
}

/// Tells the delegate that IMUIInputView will switch to camera mode
- (void)switchToCameraModeWithCameraBtn:(UIButton * _Nonnull)cameraBtn {
  if(!_rntInputView.onSwitchToCameraMode) { return; }
  _rntInputView.onSwitchToCameraMode(@{});
}

/// Tells the delegate that user did shoot picture in camera mode
- (void)didShootPictureWithPicture:(NSData * _Nonnull)picture {
  
  if(!_rntInputView.onTakePicture) { return; }
  // TODO: save to file
  NSString *filePath = [self getPath];
  
  [picture writeToFile: filePath atomically: false];
  _rntInputView.onTakePicture(@{@"mediaPath": filePath});
}

/// Tells the delegate when starting record video
- (void)startRecordVideo {
  if(!_rntInputView.onStartRecordVideo) { return; }
  _rntInputView.onStartRecordVideo(@{});
}

/// Tells the delegate when user did shoot video in camera mode
- (void)finishRecordVideoWithVideoPath:(NSString * _Nonnull)videoPath durationTime:(double)durationTime {
  
  if(!_rntInputView.onFinishRecordVideo) { return; }
  _rntInputView.onFinishRecordVideo(@{@"mediaPath": videoPath, @"durationTime": @(durationTime)});
}

- (void)keyBoardWillShowWithHeight:(CGFloat)height durationTime:(double)durationTime {
  if(!_rntInputView.onShowKeyboard) { return; }
  _rntInputView.onShowKeyboard(@{@"keyboard_height": @(height), @"durationTime": @(durationTime)});
}

- (NSString *)getPath {//"\(NSHomeDirectory())/Documents/"
  NSString *path = [NSString stringWithFormat:@"%@\/Documents\/%f", NSHomeDirectory(), NSDate.timeIntervalSinceReferenceDate];
  return path;

}
@end
