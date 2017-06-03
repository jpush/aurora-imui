//
//  RNTInputView.m
//  imuiDemo
//
//  Created by oshumini on 2017/5/27.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RNTInputView.h"
#import "RNTAuroraIController.h"

@implementation RNTInputView

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
  }
  return self;
}

- (void)hidenFeatureView {
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.imuiIntputView hideFeatureView];
  });
  
}

- (void)awakeFromNib {
  [super awakeFromNib];
}

                                                                           

@end
