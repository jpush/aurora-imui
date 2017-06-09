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

- (id)initWithCoder:(NSCoder *)aDecoder {
  self = [super initWithCoder:aDecoder];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(hidenFeatureView)
                                                 name:kHidenFeatureView object:nil];
    [self addObserver:self forKeyPath:@"bounds" options:NSKeyValueObservingOptionNew context:NULL];
  }
  return self;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
  if (object == self && [keyPath isEqualToString:@"bounds"]) {
    [self.imuiIntputView.featureView.featureCollectionView reloadData];
  }
}

- (void)hidenFeatureView {
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.imuiIntputView hideFeatureView];
  });
  
}

- (void)awakeFromNib {
  [super awakeFromNib];
}

- (void)dealloc {
  [self removeObserver:self forKeyPath:@"bounds"];
}
                                                                           

@end
