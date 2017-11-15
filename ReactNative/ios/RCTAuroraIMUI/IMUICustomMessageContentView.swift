//
//  IMUICustomMessageContentView.swift
//  RCTAuroraIMUI
//
//  Created by oshumini on 2017/11/7.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import WebKit

class IMUICustomMessageContentView: UIView, IMUIMessageContentViewProtocol {

  var customView : WKWebView?
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    let jsStr = "var meta = document.createElement('meta'); meta.setAttribute('name', 'viewport'); meta.setAttribute('content', 'width=device-width'); document.getElementsByTagName('head')[0].appendChild(meta);"
    let script = WKUserScript(source: jsStr, injectionTime: .atDocumentEnd, forMainFrameOnly: true)
    let contentController = WKUserContentController()
    contentController.addUserScript(script)
    
    let config = WKWebViewConfiguration()
    config.userContentController = contentController
    self.customView = WKWebView(frame: CGRect.zero, configuration: config)
    self.addSubview(self.customView!)
    self.customView?.isUserInteractionEnabled = false
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }

  func layoutContentView(message: IMUIMessageModelProtocol) {
    customView!.frame = CGRect(origin: CGPoint.zero, size: message.layout.bubbleContentSize)
    customView!.loadHTMLString(message.text(), baseURL: Bundle.main.bundleURL)
  }
}

