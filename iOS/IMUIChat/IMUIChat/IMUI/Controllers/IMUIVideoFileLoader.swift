//
//  IMUIVideoFileLoader.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation


class IMUIVideoFileLoader: NSObject {
 
  typealias ReadFrameCallBack = (UIImage) -> ()
  var readFrameCallback: ReadFrameCallBack?
  
  func loadVideoFile(with url: URL, callback: @escaping ReadFrameCallBack) {
    self.readFrameCallback = callback
    
    let queue = DispatchQueue(label: "imui.video.reader")
    queue.async {
      do {
        let videoAsset = AVAsset(url: url)
        let reader = try AVAssetReader(asset: videoAsset)
        let videoTracks = videoAsset.tracks(withMediaType: AVMediaTypeVideo)
        let videoTrack = videoTracks[0]
        
        let options = [String(kCVPixelBufferPixelFormatTypeKey): kCVPixelFormatType_32BGRA]
        let videoReaderOutput = AVAssetReaderTrackOutput(track: videoTrack, outputSettings: options)
        reader.add(videoReaderOutput)
        reader.startReading()
        
        while reader.status == .reading && videoTrack.nominalFrameRate > 0 {
          let videoBuffer = videoReaderOutput.copyNextSampleBuffer()
          if videoBuffer == nil { return }
          
          let image = self.imageFromSampleBuffer(sampleBuffer: videoBuffer!)
          
          usleep(41666)
          
          self.readFrameCallback?(image)
        }
        
      } catch {
        print("can not load video file")
        return
      }
    }
    
    return
  }
  
  fileprivate func imageFromSampleBuffer(sampleBuffer: CMSampleBuffer) -> UIImage {
    let imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer)
    CVPixelBufferLockBaseAddress(imageBuffer!, CVPixelBufferLockFlags(rawValue: 0))

    let baseAddress = CVPixelBufferGetBaseAddress(imageBuffer!)
    let bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer!)
    let width = CVPixelBufferGetWidth(imageBuffer!)
    let height = CVPixelBufferGetHeight(imageBuffer!)
    
    // Create a device-dependent RGB color space
    let colorSpace = CGColorSpaceCreateDeviceRGB()
    
    let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.noneSkipFirst.rawValue | CGBitmapInfo.byteOrder32Little.rawValue)
    let context = CGContext(data: baseAddress, width: width, height: height, bitsPerComponent: 8, bytesPerRow: bytesPerRow, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)

    let quartzImage = context!.makeImage();
    CVPixelBufferUnlockBaseAddress(imageBuffer!,CVPixelBufferLockFlags(rawValue: 0));
    
    let image = UIImage(cgImage: quartzImage!)
    
    return image
  }
}
