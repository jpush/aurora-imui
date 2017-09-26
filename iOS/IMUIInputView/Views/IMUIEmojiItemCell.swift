//
//  IMUIEmojiItemCell.swift
//  sample
//
//  Created by oshumini on 2017/9/26.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIEmojiItemCell: UICollectionViewCell {

  @IBOutlet weak var emojiLabel: UILabel!
  override func awakeFromNib() {
    super.awakeFromNib()
  }
  
  public func layout(with emoji: IMUIEmojiModel) {
    emojiLabel.text = emoji.emoji
  }
}
