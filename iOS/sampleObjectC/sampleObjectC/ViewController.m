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
#import "MessageEventCollectionViewCell.h"
#import "MessageEventModel.h"

@interface ViewController ()<IMUIInputViewDelegate, IMUIMessageMessageCollectionViewDelegate>
@property (weak, nonatomic) IBOutlet IMUIMessageCollectionView *messageList;
@property (weak, nonatomic) IBOutlet IMUIInputView *imuiInputView;

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  self.messageList.delegate = self;
  self.imuiInputView.delegate = self;
  self.imuiInputView.inputViewDelegate = self;
  
  [self.messageList.messageCollectionView registerClass:[MessageEventCollectionViewCell class] forCellWithReuseIdentifier:[[MessageEventCollectionViewCell class] description]];
  
}

- (void)viewDidAppear:(BOOL)animated {
  [super viewDidAppear: animated];
  NSArray *imgArr = @[
                       
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926548887&di=f107f4f8bd50fada6c5770ef27535277&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F67%2F23%2F69i58PICP37.jpg",//1
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926367926&di=ac707ee3e73241daaa5598730d28909d&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fapp%2Ficon%2F20160220%2F1455956985275086.jpg",//2
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926419519&di=c545a5d3310e88454d222623532e06b7&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fyouxi%2Fimages%2F2015%2F0701%2F20150701085247270.jpg",//3
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926596720&di=001e99492a3e684a63c07b204ff1c641&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01567057a188f70000018c1bc79411.jpg%40900w_1l_2o_100sh.jpg",//4
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926617378&di=01ade16186d4f0b6ef4fead945d142c4&imgtype=0&src=http%3A%2F%2Fimg1.tplm123.com%2F2008%2F04%2F04%2F3421%2F2309912507054.jpg",//5
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926710881&di=83ecd418f598bcadb9d74e5075397fc2&imgtype=0&src=http%3A%2F%2Fwww.missku.com%2Fd%2Ffile%2Fimport%2F2015%2F1211%2Fthumb_20151211142740226.jpg",//6
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926731796&di=e431578738f709fd75f17799a91ac4a9&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fbaike%2Fw%253D268%2Fsign%3D4c99e09935d3d539c13d08c50286e927%2F8c1001e93901213f3d7d8ebb57e736d12f2e950f.jpg",//7
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926752612&di=7a8d887ece70f73517b32803a2e048cd&imgtype=0&src=http%3A%2F%2Fimg10.360buyimg.com%2FpopWaterMark%2Fg15%2FM01%2F03%2F13%2FrBEhWVLh4JEIAAAAAAB99-puGocAAIKSwLztRsAAH4P213.jpg",//8
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926795383&di=1ce1c07257fa6918c4fbbeb3ee4e1eef&imgtype=0&src=http%3A%2F%2Fd.ifengimg.com%2Fw600%2Fp0.ifengimg.com%2Fpmop%2F2018%2F0322%2FB02F8FEE6DF6ECD3358F1EB877ECABC93268790E_size31_w643_h643.jpeg",//9
                       @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926793631&di=76964940e9b139ec8960ebf3dc360c8c&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20170828%2F84c750b9293744549a169ae3d80a0dab.jpeg"//10
                       ];
  for (NSString *urlString in imgArr) {
    NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
    MessageModel *msg = [[MessageModel alloc] initWithImageUrl:urlString messageId:msgId fromUser:[UserModel new] timeString:@"" isOutgoing:true status: IMUIMessageStatusSending];
    [_messageList appendMessageWith: msg];
    
  }
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

  NSString *eventMsgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  MessageEventModel *event = [[MessageEventModel alloc] initWithMsgId:eventMsgId eventText:messageText];
  [self.messageList appendMessageWith: event];
  
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  MessageModel *message = [[MessageModel alloc] initWithText:messageText
                                                   messageId:msgId
                                                    fromUser:[UserModel new]
                                                  timeString:@"fawefaewf"
                                                  isOutgoing:true
                                                      status:IMUIMessageStatusSuccess];
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
  
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  MessageModel *message = [[MessageModel alloc] initWithVoicePath:voicePath
                                                         duration:durationTime
                                                        messageId:msgId
                                                         fromUser:[UserModel new]
                                                       timeString:@"fasdfsadf"
                                                       isOutgoing:true
                                                           status:IMUIMessageStatusSuccess];
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
        [[PHImageManager defaultManager]
           requestImageForAsset: asset
                     targetSize: CGSizeMake(100.0, 100.0)
                    contentMode:PHImageContentModeAspectFill
                        options:options resultHandler:^(UIImage * _Nullable result, NSDictionary * _Nullable info) {
                          NSData *imageData = UIImagePNGRepresentation(result);
                          NSString *filePath = [self getPath];
                          if ([imageData writeToFile: filePath atomically: true]) {

                            NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
                            MessageModel *message = [[MessageModel alloc]
                                                       initWithImagePath:filePath
                                                                messageId:msgId
                                                                 fromUser:[UserModel new]
                                                               timeString:@""
                                                               isOutgoing:true
                                                                   status:IMUIMessageStatusSuccess];

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
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];
  NSString *imagePath = [self getPath];
  if ([picture writeToFile: imagePath atomically: true]) {
    MessageModel *message = [[MessageModel alloc] initWithImagePath:imagePath
                                                          messageId:msgId
                                                           fromUser:[UserModel new]
                                                         timeString:@""
                                                         isOutgoing:true
                                                             status:IMUIMessageStatusSuccess];
    [_messageList appendMessageWith: message];
  }

  
  
}
/// Tells the delegate when starting record video
- (void)startRecordVideo {
  
}
/// Tells the delegate when user did shoot video in camera mode
- (void)finishRecordVideoWithVideoPath:(NSString * _Nonnull)videoPath durationTime:(double)durationTime {
  NSString *msgId = [NSString stringWithFormat:@"%f",[[NSDate new] timeIntervalSince1970] * 1000];

  MessageModel *message = [[MessageModel alloc] initWithVideoPath:videoPath
                                                        messageId:msgId
                                                         fromUser:[UserModel new]
                                                       timeString:@""
                                                       isOutgoing:true
                                                           status:IMUIMessageStatusSuccess];
  [_messageList appendMessageWith: message];
}

- (void)keyBoardWillShowWithHeight:(CGFloat)height durationTime:(double)durationTime {
  [_messageList scrollToBottomWith: YES];
}

- (NSString *)getPath {//"\(NSHomeDirectory())/Documents/"
  NSString *path = [NSString stringWithFormat:@"%@\/Documents\/%f", NSHomeDirectory(), NSDate.timeIntervalSinceReferenceDate];
  return path;
  
}


- (UICollectionViewCell * _Nullable)messageCollectionViewWithMessageCollectionView:(UICollectionView * _Nonnull)messageCollectionView forItemAt:(NSIndexPath * _Nonnull)forItemAt messageModel:(id <IMUIMessageProtocol> _Nonnull)messageModel SWIFT_WARN_UNUSED_RESULT {
  if ([messageModel isKindOfClass: [MessageEventModel class]]) {
    MessageEventCollectionViewCell *cell = [messageCollectionView dequeueReusableCellWithReuseIdentifier:[[MessageEventCollectionViewCell class] description] forIndexPath:forItemAt];
    MessageEventModel *event = (MessageEventModel *)messageModel;
    [cell presentCell: event.evenText];
    return cell;
  } else {
    return nil;
  }
}

- (NSNumber * _Nullable)messageCollectionViewWithMessageCollectionView:(UICollectionView * _Nonnull)messageCollectionView heightForItemAtIndexPath:(NSIndexPath * _Nonnull)forItemAt messageModel:(id <IMUIMessageProtocol> _Nonnull)messageModel SWIFT_WARN_UNUSED_RESULT {
  if ([messageModel isKindOfClass: [MessageEventModel class]]) {
    return @(20.0);
  } else {
    return nil;
  }
}

@end
