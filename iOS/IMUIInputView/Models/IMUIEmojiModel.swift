//
//  IMUIEmojiModel.swift
//  sample
//
//  Created by oshumini on 2017/9/26.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public enum IMUIEmojiType {
  case emoji
  case image
  case gif
}

public class IMUIEmojiModel: NSObject {
  var emojiType: IMUIEmojiType
  
  var emoji: String?
  var mediaPath: String?
  
  public init(_ emojiType: IMUIEmojiType,
       _ emoji: String?,
       _ mediaPath: String?) {
    self.emojiType = emojiType
    self.emoji = emoji
    self.mediaPath = mediaPath
    super.init()
  }
  
  convenience public init(emojiType: IMUIEmojiType,
                          emoji: String) {
    self.init(emojiType, emoji, nil)
  }
  
  convenience public init(emojiType: IMUIEmojiType,
                          mediaPath: String) {
    self.init(emojiType, nil, mediaPath)
  }
}
