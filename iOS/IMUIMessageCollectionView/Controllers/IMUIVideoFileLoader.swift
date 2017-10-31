//
//  IMUIVideoFileLoader.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import AVFoundation



typealias ReadFrameCallBack = (CGImage) -> ()

class IMUIVideoFileLoader: NSObject {
 
  
  var readFrameCallback: ReadFrameCallBack?
  
  static let queue = OperationQueue()
  var videoReadOperation: IMUIVideFileLoaderOperation?
  
  var isNeedToStopVideo: Bool {
    set {
      self.videoReadOperation?.isNeedToStop = newValue
    }
    
    get {
      return true
    }
  }
  
  func loadVideoFile(with url: URL, callback: @escaping ReadFrameCallBack) {
    self.readFrameCallback = callback
    videoReadOperation?.isNeedToStop = true
    videoReadOperation = IMUIVideFileLoaderOperation(url: url, callback: callback)
    IMUIVideoFileLoader.queue.addOperation(videoReadOperation!)
    return
  }
}

class IMUIVideFileLoaderOperation: Operation {
  
  var isNeedToStop = false

  var previousFrameTime: CMTime?
  
  var url: URL
  var readFrameCallback: ReadFrameCallBack
  
  init(url: URL, callback: @escaping ReadFrameCallBack) {
    self.url = url
    self.readFrameCallback = callback
    super.init()
  }
  
  override func main() {
    self.isNeedToStop = false
    do {
      while !self.isNeedToStop {
        let videoAsset = AVAsset(url: url)
        let reader = try AVAssetReader(asset: videoAsset)
        let videoTracks = videoAsset.tracks(withMediaType: AVMediaType.video)
        let videoTrack = videoTracks[0]
        
        let options = [String(kCVPixelBufferPixelFormatTypeKey): kCVPixelFormatType_32BGRA]
        let videoReaderOutput = AVAssetReaderTrackOutput(track: videoTrack, outputSettings: options)
        reader.add(videoReaderOutput)
        reader.startReading()
        
        while reader.status == .reading && videoTrack.nominalFrameRate > 0 {
          let videoBuffer = videoReaderOutput.copyNextSampleBuffer()
          
          if videoBuffer == nil {
            if reader.status != .cancelled {
              reader.cancelReading()
            }
            break
          }
          
          let image = self.imageFromSampleBuffer(sampleBuffer: videoBuffer!)
          
          if self.isNeedToStop != true {
            self.readFrameCallback(image)
          } else {
            break
          }

          usleep(41666)

          if reader.status == .completed {
            reader.cancelReading()
          }
        }

      }
    } catch {
      print("can not load video file")
      return
    }
  }
  
  fileprivate func imageFromSampleBuffer(sampleBuffer: CMSampleBuffer) -> CGImage {
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
    
    return quartzImage!
  }
  
  fileprivate func ciImageFromSampleBuffer(sampleBuffer: CMSampleBuffer) -> CIImage {
    return CIImage(cvPixelBuffer: CMSampleBufferGetImageBuffer(sampleBuffer)!)
    
  }
  
}
