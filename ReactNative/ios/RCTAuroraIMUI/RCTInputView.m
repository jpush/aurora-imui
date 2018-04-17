//
//  RCTInputView.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTInputView.h"
#import "RCTAuroraIMUIModule.h"


@implementation RCTInputView

- (instancetype)init {
  self = [super init];
  return self;
}

- (instancetype)initWithFrame:(CGRect)frame {
  self = [super initWithFrame: frame];
  return self;
}

- (void)setFrame:(CGRect)frame {
  // override setFrame and do not thing to disable this function, react-native use setBounds to layout.
}

- (id)initWithCoder:(NSCoder *)aDecoder {
  self = [super initWithCoder:aDecoder];
  self.keyBoardHeight = 0.0;
  self.maxKeyBoardHeight = 0.0;
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(hidenFeatureView)
                                                 name:kHidenFeatureView object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(layoutInputView)
                                                 name:kLayoutInputView object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidHide:)
                                                 name:UIKeyboardDidHideNotification object:nil];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidShow:)
                                                 name:UIKeyboardDidShowNotification object:nil];

    [self addObserver:self forKeyPath:@"bounds" options:NSKeyValueObservingOptionNew context:NULL];
  }
  return self;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
  if (object == self && [keyPath isEqualToString:@"bounds"]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      [self.imuiIntputView.featureView.featureCollectionView layoutSubviews];
      [self.imuiIntputView.featureView.featureCollectionView reloadData];
    });
  }
}

- (CGFloat)inputTextHeight {
  if (self.imuiIntputView == nil) {
    return 0.0;
  }
  
  CGSize maxSize = CGSizeMake(self.imuiIntputView.inputTextView.frame.size.width, 78);

  NSMutableParagraphStyle *paragraphStyle= [[NSMutableParagraphStyle alloc] init];
  CGSize realSize = [self.imuiIntputView.inputTextView.text
                     boundingRectWithSize:maxSize
                     options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading
                     attributes:@{NSFontAttributeName:self.imuiIntputView.inputTextView.font,
                                  NSParagraphStyleAttributeName:paragraphStyle}
                     context:nil].size;
  return realSize.height <= self.imuiIntputView.inputTextViewHeightRange.minimum ? self.imuiIntputView.inputTextViewHeightRange.minimum : realSize.height;
  
}
- (void)hidenFeatureView {
  dispatch_async(dispatch_get_main_queue(), ^{
    self.maxKeyBoardHeight = 0.0;
    [self.imuiIntputView hideFeatureView];
    [self.imuiIntputView hideFeatureView];// call twice to fix odd bug
    if(self.onSizeChange) {
      BOOL needShow = [self.imuiIntputView isNeedShowBottomView];
      self.onSizeChange(@{@"height":@(46 + self.inputTextHeight +
                            self.imuiIntputView.inputTextViewPadding.top +
                            self.imuiIntputView.inputTextViewPadding.bottom +
                            (needShow?0:-46)),
                          @"width":@(self.frame.size.width)});
    }
  });
}

- (void)layoutInputView {
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.imuiIntputView layoutInputView];
    
    if(self.onSizeChange) {
      BOOL needShow = [self.imuiIntputView isNeedShowBottomView];
      self.onSizeChange(@{@"height":@(46 + self.inputTextHeight +
                            self.imuiIntputView.inputTextViewPadding.top +
                            self.imuiIntputView.inputTextViewPadding.bottom +
                            (needShow?0:-46)),
                          @"width":@(self.frame.size.width)});
    }
  });
}

- (void)keyboardDidHide:(NSNotification *) notif{
  NSDictionary *dic = notif.userInfo;
  NSValue *keyboardValue = dic[UIKeyboardFrameEndUserInfoKey];
  CGFloat bottomDistance = [UIScreen mainScreen].bounds.size.height - keyboardValue.CGRectValue.origin.y;
  self.keyBoardHeight  = bottomDistance;
}

- (void)keyboardDidShow:(NSNotification *) notif{
  NSDictionary *dic = notif.userInfo;
  NSValue *keyboardValue = dic[UIKeyboardFrameEndUserInfoKey];
  CGFloat bottomDistance = [UIScreen mainScreen].bounds.size.height - keyboardValue.CGRectValue.origin.y;
  if (self.maxKeyBoardHeight < bottomDistance) {
    self.maxKeyBoardHeight = bottomDistance;
  }
  self.keyBoardHeight  = bottomDistance;
  if(self.onSizeChange) {
    BOOL needShow = [self.imuiIntputView isNeedShowBottomView];
    self.onSizeChange(@{@"height":@(46 + self.inputTextHeight + self.keyBoardHeight +
                          self.imuiIntputView.inputTextViewPadding.top +
                          self.imuiIntputView.inputTextViewPadding.bottom +
                          (needShow?0:-46)),
                        @"width":@(self.frame.size.width)});
  }
}

- (void)awakeFromNib {
  [super awakeFromNib];
}

- (void)dealloc {
  [self removeObserver:self forKeyPath:@"bounds"];
  [[NSNotificationCenter defaultCenter] removeObserver: self];
}
                                                                           

@end
