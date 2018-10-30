//
//  IMUIWebImageOperation.swift
//  ImageDownload
//
//  Created by oshumini on 2018/7/9.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import UIKit
import Foundation

public class IMUIWebImageTaskCache: NSObject {
  
  public override init() {
    super.init()
  }
  
  var cache = [URLSessionTask: IMUIWebImageDownloader]()
  var urlCache = [String: URLSessionTask]()
  
  func addTask(_ urlString: String, _ task: URLSessionTask?, _ downloader: IMUIWebImageDownloader) {
    if let _ = task {
      cache[task!] = downloader
      urlCache[urlString] = task
    }
  }
  
  func removeTask(_ urlString: String) {
    if let task = urlCache[urlString] {
      cache[task] = nil
    }
    urlCache[urlString] = nil
  }
  
  func removeTask(_ task: URLSessionTask) {
    if let downloader = cache[task] {
      cache[task] = nil
      let urlString = downloader.getRequestUrl()
      urlCache[urlString] = nil
    }
  }
  
  func removeAllTask() {
    for (task, _) in cache {
      task.cancel()
    }
    
    cache.removeAll()
    urlCache.removeAll()
  }
  
  func checkInCache(_ urlString: String) -> Bool {
    if let task = urlCache[urlString], let _ = cache[task] {
      return true
    } else {
      return false
    }
  }
  
  func getDownloader(with task: URLSessionTask) -> IMUIWebImageDownloader? {
    return cache[task]
  }
  
  func getTask(_ urlString: String) -> URLSessionTask? {
    return urlCache[urlString]
  }
}

class IMUIWebImageTaskManager: NSObject {
  let taskCach = IMUIWebImageTaskCache()
  var _session: URLSession?
  
  static let shared = IMUIWebImageTaskManager()
  
  public override init() {
    
    super.init()
    
    let config = URLSessionConfiguration.default
    config.allowsCellularAccess = true
    config.urlCache = URLCache(memoryCapacity: 5 * 1024 * 1024, diskCapacity: 50 * 1024 * 1024, diskPath: "IMUIImageCache")
    _session = URLSession(configuration: config, delegate: self, delegateQueue: OperationQueue.main)
    
    NotificationCenter.default.addObserver(self, selector: #selector(receiveMemoryWarnning), name: UIApplication.didReceiveMemoryWarningNotification, object: nil)
  }

  @objc func receiveMemoryWarnning() {
    taskCach.removeAllTask()
  }
  
  func downloadImage(_ urlString: String, callback: @escaping downloadCompletionHandler) -> URLSessionTask? {
    
    guard let url = URL(string: urlString) else {
      print("Error: cannot create URL")
      return nil
    }
    
    var downloader: IMUIWebImageDownloader
    if self.taskCach.checkInCache(urlString) {
      let task = self.taskCach.getTask(urlString)
      let downloader = self.taskCach.getDownloader(with: task!)
      downloader!.addCallback(callback: callback)
      
      task!.resume()
      return task
    }
    
    downloader = IMUIWebImageDownloader(callback: callback)
    
    let urlRequest = URLRequest(url: url)
    let task = downloader.dispatchDownloader(_session!, urlRequest)
    taskCach.addTask(urlString, task, downloader)
    task?.resume()
    return task
  }
}

extension IMUIWebImageTaskManager: URLSessionDataDelegate {
  @available(iOS 7.0, *)
  public func urlSession(_ session: URLSession, dataTask: URLSessionDataTask, didReceive response: URLResponse, completionHandler: @escaping (URLSession.ResponseDisposition) -> Swift.Void) {
    
    print("didReceive")
    completionHandler(.allow)
  }
  
  @available(iOS 7.0, *)
  public func urlSession(_ session: URLSession, dataTask: URLSessionDataTask, didReceive data: Data) {
    
    if let downloader = taskCach.getDownloader(with: dataTask) {
      downloader.appendData(with: data)
    }
  }
  
  @available(iOS 7.0, *)
  public func urlSession(_ session: URLSession, dataTask: URLSessionDataTask, willCacheResponse proposedResponse: CachedURLResponse, completionHandler: @escaping (CachedURLResponse?) -> Swift.Void) {
    print("willCacheResponse")
    completionHandler(proposedResponse)
    if let downloader = taskCach.getDownloader(with: dataTask) {
      self.taskCach.removeTask(dataTask)
      downloader.completionHandler(with: nil)
    }
  }
  
}


extension IMUIWebImageTaskManager: URLSessionTaskDelegate {
  
  @available(iOS 7.0, *)
  public func urlSession(_ session: URLSession, task: URLSessionTask, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Swift.Void) {
    completionHandler(.performDefaultHandling, nil)
  }
  
  @available(iOS 7.0, *)
  public func urlSession(_ session: URLSession, task: URLSessionTask, didCompleteWithError error: Error?) {
    
    if let downloader = taskCach.getDownloader(with: task) {
      downloader.completionHandler(with: error)
    }
    self.taskCach.removeTask(task)
  }
}


