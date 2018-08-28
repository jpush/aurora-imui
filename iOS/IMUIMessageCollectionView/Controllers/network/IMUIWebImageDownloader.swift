//
//  IMUIWebImageDownloader.swift
//  ImageDownload
//
//  Created by oshumini on 2018/8/17.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import Foundation


public typealias downloadCompletionHandler = ((_ data:Data? , _ progressValue:Float, _ urlString: String,_ error: Error?) -> Void)



public class IMUIWebImageDownloader {
  private var _imageData: NSMutableData = NSMutableData()
  
  var callbacks = [downloadCompletionHandler]()
  var task: URLSessionTask?
  
  public func getRequestUrl() -> String {
    return task?.originalRequest?.url?.absoluteString ?? ""
  }
  
  init(callback: @escaping downloadCompletionHandler) {
    self.callbacks.append(callback)
  }
  
  public func addCallback(callback: @escaping downloadCompletionHandler) {
    self.callbacks.append(callback)
  }
  
  public func appendData(with data: Data) {
    self._imageData.append(data)
  }
  
  public func dispatchDownloader(_ session: URLSession, _ urlRequest: URLRequest) -> URLSessionTask? {
    self.task = session.dataTask(with: urlRequest)
    return self.task
  }
  
  public func completionHandler(with error: Error?) {
    for callback in self.callbacks {
      callback(self._imageData as Data, 1.0, (self.task?.currentRequest?.url?.absoluteString) ?? "" ,error)
    }
  }
}

