//
//  IMUIPermissionDenyedView.swift
//  sample
//
//  Created by oshumini on 2018/1/5.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import UIKit

class IMUIPermissionDenyedView: UIView {

  var view: UIView!
  
  @IBOutlet weak var permissionTitle: UILabel!
  @IBOutlet weak var permissionBody: UILabel!
  
  var _type = ""
  
  var type: String {
    get {
      return _type
    }
    
    set {
      _type = newValue
      self.permissionTitle.text = "授予\(_type)访问权限"
      self.permissionBody.text = "请打开“设置”>“隐私”>“\(_type)”，勾选应用权限。"
    }
  }
  
  open override func awakeFromNib() {
    super.awakeFromNib()
  }
  
  required public init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    
    let bundle = Bundle.imuiInputViewBundle()
    view = bundle.loadNibNamed("IMUIPermissionDenyedView", owner: self, options: nil)?.first as! UIView
    
    self.addSubview(view)
    view.frame = self.bounds
  }
  
  @IBAction func gotoSettingPage(_ sender: Any) {
    if #available(iOS 10.0, *) {
      UIApplication.shared.open(URL(string: UIApplicationOpenSettingsURLString)!, completionHandler: nil)
    } else {
      UIApplication.shared.openURL(URL(string: UIApplicationOpenSettingsURLString)!)
    }
  }
  
}
