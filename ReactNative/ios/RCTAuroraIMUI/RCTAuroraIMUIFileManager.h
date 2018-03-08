//
//  RCTAuroraIMUIFileManager.h
//  RCTAuroraIMUI
//
//  Created by oshumini on 2018/3/6.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RCTAuroraIMUIFileManager : NSObject
+ (NSString *)getPath;
+ (void)createDirectory:(NSString *)directoryName atFilePath:(NSString *)filePath;
+ (int)getFileSize:(NSString *)path;
@end
