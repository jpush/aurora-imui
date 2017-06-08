//
//  UserModel.m
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "UserModel.h"

@implementation UserModel
- (instancetype)init
{
  self = [super init];
  if (self) {
    
  }
  return self;
}

- (NSString * _Nonnull)userId SWIFT_WARN_UNUSED_RESULT {
  return @"";
}

- (NSString * _Nonnull)displayName SWIFT_WARN_UNUSED_RESULT {
  return @"";
}

- (UIImage * _Nonnull)Avatar SWIFT_WARN_UNUSED_RESULT {
  return [UIImage imageNamed:@"defoult_header"];
  
}

@end
