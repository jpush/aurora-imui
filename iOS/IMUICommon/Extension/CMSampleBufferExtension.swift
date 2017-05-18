//
//  File.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import AVFoundation
import UIKit

public extension CMSampleBuffer {

  
  func imageFromSampleBuffer_Type32BGRA(sampleBuffer: CMSampleBuffer) -> UIImage {
    // Get a CMSampleBuffer's Core Video image buffer for the media data
    let imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer)
    // Lock the base address of the pixel buffer
    CVPixelBufferLockBaseAddress(imageBuffer!, CVPixelBufferLockFlags(rawValue: 0))
    
    // Get the number of bytes per row for the pixel buffer
    let baseAddress = CVPixelBufferGetBaseAddress(imageBuffer!)
    
    // Get the number of bytes per row for the pixel buffer
    let bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer!)
    // Get the pixel buffer width and height
    let width = CVPixelBufferGetWidth(imageBuffer!)
    let height = CVPixelBufferGetHeight(imageBuffer!)
    
    // Create a device-dependent RGB color space
    let colorSpace = CGColorSpaceCreateDeviceRGB()
    
    // Create a bitmap graphics context with the sample buffer data
    //    let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.noneSkipLast.rawValue)
    let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.noneSkipFirst.rawValue | CGBitmapInfo.byteOrder32Little.rawValue)
    let context = CGContext(data: baseAddress, width: width, height: height, bitsPerComponent: 8, bytesPerRow: bytesPerRow, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)
    // Create a Quartz image from the pixel data in the bitmap graphics context
    let quartzImage = context!.makeImage();
    // Unlock the pixel buffer
    CVPixelBufferUnlockBaseAddress(imageBuffer!,CVPixelBufferLockFlags(rawValue: 0));
    
    // Create an image object from the Quartz image
    let image = UIImage(cgImage: quartzImage!)
    
    return image
  }
}
