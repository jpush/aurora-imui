//
//  IMUIChatViewController.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


class IMUIChatViewController: UIViewController {

  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  @IBOutlet weak var myInputView: IMUIInputView!
  
  
  let dataManager = IMUIChatDataManager()
  let chatLayout = IMUIChatLayout()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.myInputView.inputViewDelegate = self
  }
  
  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    
  }

}

extension IMUIChatViewController: IMUIInputViewDelegate {
  
  func sendTextMessage(_ messageText: String) {
    let message = MyMessageModel(with: messageText)
    messageCollectionView.appendMessage(with: message)
    messageCollectionView.reloadData()
  }
}
