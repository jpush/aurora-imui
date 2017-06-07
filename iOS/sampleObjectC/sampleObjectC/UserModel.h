//
//  UserModel.h
//  JMessage-AuroraIMUI-OC-Demo
//
//  Created by oshumini on 2017/6/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sampleObjectC-Swift.h"
#import <UIKit/UIKit.h>

@interface UserModel : NSObject <IMUIUserProtocol>
- (NSString * _Nonnull)userId SWIFT_WARN_UNUSED_RESULT;

- (NSString * _Nonnull)displayName SWIFT_WARN_UNUSED_RESULT;

- (UIImage * _Nonnull)Avatar SWIFT_WARN_UNUSED_RESULT;
@end
