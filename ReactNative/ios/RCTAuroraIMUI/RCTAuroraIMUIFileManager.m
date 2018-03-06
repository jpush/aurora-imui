//
//  RCTAuroraIMUIFileManager.m
//  RCTAuroraIMUI
//
//  Created by oshumini on 2018/3/6.
//  Copyright © 2018年 HXHG. All rights reserved.
//

#import "RCTAuroraIMUIFileManager.h"

@implementation RCTAuroraIMUIFileManager
+ (NSString *)getPath {//"\(NSHomeDirectory())/Documents/RCTAuroraIMUI"
  
  
  NSString *dirPath = [NSString stringWithFormat:@"%@\/Documents\/RCTAuroraIMUI", NSHomeDirectory()];
  CFUUIDRef udid = CFUUIDCreate(NULL);
  NSString *udidString = (NSString *) CFBridgingRelease(CFUUIDCreateString(NULL, udid));
  
  NSString *fileName = [udidString stringByAppendingString:@".jpg"];
  NSString *path = [dirPath stringByAppendingPathComponent:fileName];
  return path;
}

+ (void)createDirectory:(NSString *)directoryName atFilePath:(NSString *)filePath
{
  NSString *filePathAndDirectory = [filePath stringByAppendingPathComponent:directoryName];
  NSError *error;
  
  if (![[NSFileManager defaultManager] createDirectoryAtPath:filePathAndDirectory
                                 withIntermediateDirectories:NO
                                                  attributes:nil
                                                       error:&error])
  {
    NSLog(@"Create directory error: %@", error);
  }
}
@end
