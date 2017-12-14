//
//  RCTInputViewManager.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import "RCTInputView.h"
#import <RCTAuroraIMUI/RCTAuroraIMUI-Swift.h>
#import <Photos/Photos.h>

@interface RCTInputViewManager : RCTViewManager <IMUIInputViewDelegate>

@property (strong, nonatomic)RCTInputView *rctInputView;
@end

@implementation RCTInputViewManager

//RCT_EXPORT_VIEW_PROPERTY(onEventCallBack, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)

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
RCT_EXPORT_VIEW_PROPERTY(onSwitchToEmojiMode, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onShowKeyboard, RCTBubblingEventBlock)

RCT_EXPORT_MODULE()
- (UIView *)view
{
  NSBundle *bundle = [NSBundle bundleForClass: [RCTInputView class]];
  _rctInputView = [[bundle loadNibNamed:@"RCTInputView" owner:self options: nil] objectAtIndex:0];
  _rctInputView.imuiIntputView.inputViewDelegate = self;
  return _rctInputView;
}

RCT_CUSTOM_VIEW_PROPERTY(chatInputBackgroupColor, NSString, RCTInputView) {
  NSString *colorString = [RCTConvert NSString: json];
  UIColor *color = [UIColor hexStringToUIColorWithHex:colorString];
  if (color != nil) {
    for (UIView *view in _rctInputView.imuiIntputView.subviews) {
      view.backgroundColor = color;
    }
  }
  _rctInputView.imuiIntputView.featureSelectorView.featureListCollectionView.backgroundColor = color;
}


/// Tells the delegate that user tap send button and text input string is not empty
- (void)sendTextMessage:(NSString * _Nonnull)messageText {
  if(!_rctInputView.onSendText) { return; }
  
  _rctInputView.onSendText(@{@"text": messageText});
}

/// Tells the delegate that IMUIInputView will switch to recording voice mode
- (void)switchToMicrophoneModeWithRecordVoiceBtn:(UIButton * _Nonnull)recordVoiceBtn {
  // TODO:
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(298 + _rctInputView.inputTextHeight),@"width":@(_rctInputView.frame.size.width)});
  }
  
  if(!_rctInputView.onSwitchToMicrophoneMode) { return; }
  _rctInputView.onSwitchToMicrophoneMode(@{});
}

/// Tells the delegate that start record voice
- (void)startRecordVoice {
  if(!_rctInputView.onStartRecordVoice) { return; }
  _rctInputView.onStartRecordVoice(@{});
}

/// Tells the delegate when finish record voice
- (void)finishRecordVoice:(NSString * _Nonnull)voicePath durationTime:(double)durationTime {
  if(!_rctInputView.onFinishRecordVoice) { return; }
  _rctInputView.onFinishRecordVoice(@{@"mediaPath": voicePath, @"duration": @(durationTime)});
}

/// Tells the delegate that user cancel record
- (void)cancelRecordVoice {
  if(!_rctInputView.onCancelRecordVoice) { return; }
  _rctInputView.onCancelRecordVoice(@{});
}

/// Tells the delegate that IMUIInputView will switch to gallery
- (void)switchToGalleryModeWithPhotoBtn:(UIButton * _Nonnull)photoBtn {
  
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(298 + _rctInputView.inputTextHeight),@"width":@(_rctInputView.frame.size.width)});
  }
  
  if(!_rctInputView.onSwitchToGalleryMode) { return; }
  _rctInputView.onSwitchToGalleryMode(@{});
}

/// Tells the delegate that IMUIInputView will switch to emoji
- (void)switchToEmojiModeWithCameraBtn:(UIButton * _Nonnull)cameraBtn {
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(298 + _rctInputView.inputTextHeight),@"width":@(_rctInputView.frame.size.width)});
  }
  
  if(!_rctInputView.onSwitchToEmojiMode) { return; }
  _rctInputView.onSwitchToEmojiMode(@{});
}

/// Tells the delegate that user did selected Photo in gallery
- (void)didSeletedGalleryWithAssetArr:(NSArray<PHAsset *> * _Nonnull)AssetArr {
  dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void){
    if(!_rctInputView.onSendGalleryFiles) { return; }
    __block NSMutableArray *imagePathArr = @[].mutableCopy;
    
    for (PHAsset *asset in AssetArr) {
      switch (asset.mediaType) {
        case PHAssetMediaTypeImage: {
          
          PHImageRequestOptions *options = [[PHImageRequestOptions alloc]init];
          options.synchronous  = YES;
          options.networkAccessAllowed = YES;
          PHCachingImageManager *imageManage = [[PHCachingImageManager alloc] init];
          
          [imageManage requestImageForAsset: asset
                                 targetSize: CGSizeMake(asset.pixelWidth, asset.pixelHeight)
                                contentMode: PHImageContentModeAspectFill
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
    
    dispatch_async(dispatch_get_main_queue(), ^(void){
      _rctInputView.onSendGalleryFiles(@{@"mediaFiles": imagePathArr});
    });
  });
}

- (void)textDidChangeWithText:(NSString * _Nonnull)text {
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(46 + _rctInputView.inputTextHeight + _rctInputView.keyBoardHeight),@"width":@(_rctInputView.frame.size.width)});
  }
}

/// Tells the delegate that IMUIInputView will switch to camera mode
- (void)switchToCameraModeWithCameraBtn:(UIButton * _Nonnull)cameraBtn {
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(298 + _rctInputView.inputTextHeight),@"width":@(_rctInputView.frame.size.width)});
  }
  
  if(!_rctInputView.onSwitchToCameraMode) { return; }
  _rctInputView.onSwitchToCameraMode(@{});
}

/// Tells the delegate that user did shoot picture in camera mode
- (void)didShootPictureWithPicture:(NSData * _Nonnull)picture {
  
  if(!_rctInputView.onTakePicture) { return; }
  // TODO: save to file
  NSString *filePath = [self getPath];
  
  [picture writeToFile: filePath atomically: false];
  _rctInputView.onTakePicture(@{@"mediaPath": filePath});
}

/// Tells the delegate when starting record video
- (void)startRecordVideo {
  if(!_rctInputView.onStartRecordVideo) { return; }
  _rctInputView.onStartRecordVideo(@{});
}

/// Tells the delegate when user did shoot video in camera mode
- (void)finishRecordVideoWithVideoPath:(NSString * _Nonnull)videoPath durationTime:(double)durationTime {
  
  if(!_rctInputView.onFinishRecordVideo) { return; }
  _rctInputView.onFinishRecordVideo(@{@"mediaPath": videoPath, @"durationTime": @(durationTime)});
}

- (void)keyBoardWillShowWithHeight:(CGFloat)height durationTime:(double)durationTime {
  if(_rctInputView.onSizeChange) {
    _rctInputView.onSizeChange(@{@"height":@(height + 46 + _rctInputView.inputTextHeight),@"width":@(_rctInputView.frame.size.width)});
  }
  
  if(!_rctInputView.onShowKeyboard) { return; }
  _rctInputView.onShowKeyboard(@{@"keyboard_height": @(height), @"durationTime": @(durationTime)});
}

- (NSString *)getPath {//"\(NSHomeDirectory())/Documents/"
  CFUUIDRef udid = CFUUIDCreate(NULL);
  NSString *udidString = (NSString *) CFBridgingRelease(CFUUIDCreateString(NULL, udid));
  
  NSString *path = [NSString stringWithFormat:@"%@\/Documents\/%@.jpg", NSHomeDirectory(), udidString];
  return path;

}
@end
