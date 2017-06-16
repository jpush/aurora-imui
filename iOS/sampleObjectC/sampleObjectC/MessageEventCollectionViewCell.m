//
//  MessageEventCollectionViewCell.m
//  sampleObjectC
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "MessageEventCollectionViewCell.h"

@interface MessageEventCollectionViewCell ()
@property(strong, nonatomic)UILabel *evenText;
@end

@implementation MessageEventCollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
  self = [super initWithFrame:frame];
  if (self) {
    _evenText = [[UILabel alloc] initWithFrame: CGRectZero];
    [self.contentView addSubview: _evenText];
    _evenText.textColor = [[UIColor alloc] initWithNetHex: 0x7587A8];
    _evenText.textAlignment = NSTextAlignmentCenter;
    
  }
  return self;
}

- (void)presentCell:(NSString *)eventText {
  _evenText.text = eventText;
  _evenText.frame = CGRectMake(0, 0, 300, 20);
  
  _evenText.center = CGPointMake(self.contentView.frame.size.width/2,
                                 self.contentView.frame.size.height/2);
  
}

@end
