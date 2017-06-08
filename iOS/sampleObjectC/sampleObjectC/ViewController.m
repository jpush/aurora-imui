//
//  ViewController.m
//  sampleObjectC
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <Photos/Photos.h>

#import "ViewController.h"
#import "sampleObjectC-Swift.h"
#import "MessageModel.h"
#import "UserModel.h"

@interface ViewController ()<IMUIInputViewDelegate, IMUIMessageMessageCollectionViewDelegate>
@property (weak, nonatomic) IBOutlet IMUIMessageCollectionView *messageList;
@property (weak, nonatomic) IBOutlet IMUIInputView *imuiInputView;

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  self.messageList.delegate = self;
  self.imuiInputView.inputViewDelegate = self;
}


- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}


// - MARK: IMUIInputViewDelegate
- (void)messageCollectionView:(UICollectionView * _Nonnull)willBeginDragging {
  [_imuiInputView hideFeatureView];
}


// - MARK: IMUIInputViewDelegate
/// Tells the delegate that user tap send button and text input string is not empty
- (void)sendTextMessage:(NSString * _Nonnull)messageText {
  MessageModel *message = [MessageModel new];
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  [message setupTextMessage:msgId fromUser:[UserModel new] timeString:@"" text:messageText isOutgoing:YES status:IMUIMessageStatusSuccess];
  [self.messageList appendMessageWith:message];
}
/// Tells the delegate that IMUIInputView will switch to recording voice mode
- (void)switchToMicrophoneModeWithRecordVoiceBtn:(UIButton * _Nonnull)recordVoiceBtn {
  
}
/// Tells the delegate that start record voice
- (void)startRecordVoice {
  
}
/// Tells the delegate when finish record voice
- (void)finishRecordVoice:(NSString * _Nonnull)voicePath durationTime:(double)durationTime {
  MessageModel *message = [MessageModel new];
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];

  [message setupVoiceMessage:msgId fromUser:[UserModel new] timeString:@"" mediaPath:voicePath isOutgoing:true status:IMUIMessageStatusSuccess];
  [_messageList appendMessageWith: message];
}

- (void)cancelRecordVoice {
  
}
/// Tells the delegate that IMUIInputView will switch to gallery
- (void)switchToGalleryModeWithPhotoBtn:(UIButton * _Nonnull)photoBtn {
  
}
/// Tells the delegate that user did selected Photo in gallery
- (void)didSeletedGalleryWithAssetArr:(NSArray<PHAsset *> * _Nonnull)AssetArr {
  
  for (PHAsset *asset in AssetArr) {
    switch (asset.mediaType) {
      case PHAssetMediaTypeImage: {
        
        PHImageRequestOptions *options = [[PHImageRequestOptions alloc]init];
        options.synchronous  = YES;
        [[PHImageManager defaultManager] requestImageForAsset: asset
                               targetSize: CGSizeMake(100.0, 100.0)
                              contentMode:PHImageContentModeAspectFill
                                  options:options resultHandler:^(UIImage * _Nullable result, NSDictionary * _Nullable info) {
                                    NSData *imageData = UIImagePNGRepresentation(result);
                                    NSString *filePath = [self getPath];
                                    if ([imageData writeToFile: filePath atomically: true]) {
                                      MessageModel *message = [MessageModel new];
                                      NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
                                      [message setupImageMessage:msgId fromUser:[UserModel new] timeString:@"" mediaPath:filePath isOutgoing:true status: IMUIMessageStatusSuccess];
                                      [_messageList appendMessageWith: message];
                                    }
                                  }];
        break;
      }
        
      default:
        break;
    }
  }
  
}
/// Tells the delegate that IMUIInputView will switch to camera mode
- (void)switchToCameraModeWithCameraBtn:(UIButton * _Nonnull)cameraBtn {
  
}
/// Tells the delegate that user did shoot picture in camera mode
- (void)didShootPictureWithPicture:(NSData * _Nonnull)picture {
  MessageModel *message = [MessageModel new];
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  NSString *imagePath = [self getPath];
  if ([picture writeToFile: imagePath atomically: true]) {
    [message setupImageMessage:msgId fromUser:[UserModel new] timeString:@"" mediaPath:imagePath isOutgoing:true status: IMUIMessageStatusSuccess];
    [_messageList appendMessageWith: message];
  }

  
  
}
/// Tells the delegate when starting record video
- (void)startRecordVideo {
  
}
/// Tells the delegate when user did shoot video in camera mode
- (void)finishRecordVideoWithVideoPath:(NSString * _Nonnull)videoPath durationTime:(double)durationTime {
  MessageModel *message = [MessageModel new];
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  [message setupVideoMessage:msgId fromUser:[UserModel new] timeString:@"" mediaPath:videoPath isOutgoing:true status:IMUIMessageStatusSuccess];
  [_messageList appendMessageWith: message];
}

- (void)keyBoardWillShowWithHeight:(CGFloat)height durationTime:(double)durationTime {
  
}

- (NSString *)getPath {//"\(NSHomeDirectory())/Documents/"
  NSString *path = [NSString stringWithFormat:@"%@\/Documents\/%f", NSHomeDirectory(), NSDate.timeIntervalSinceReferenceDate];
  return path;
  
}


@end
