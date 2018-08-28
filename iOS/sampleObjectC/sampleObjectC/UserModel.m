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

- (NSString * _Nullable)avatarUrlString SWIFT_WARN_UNUSED_RESULT; {
  return @"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926548887&di=f107f4f8bd50fada6c5770ef27535277&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F67%2F23%2F69i58PICP37.jpg";
}
@end
