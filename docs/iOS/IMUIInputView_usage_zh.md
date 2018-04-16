# IMUIInputView
[English document](./IMUIInputView_usage.md)

这是一个聊天界面输入框组件，可以方便地结合 `IMUIMessageCollectionView` 使用，包含录音，选择图片，拍照等功能，提供了一些丰富的接口和回调供用户使用， 还可以选择自定义样式。

## 安装
### [CocoaPods](https://cocoapods.org/)  (推荐)
```ruby
# For latest release in cocoapods
pod 'AuroraIMUI'
```

### 手动集成
拷贝 `IMUICommon/` `IMUIInputView/`  这两个目录到自己工程中。

>**注意**：确保自己工程中 `Info.plist` 包含 camera , Microphone 和 Photo Library 权限。

## 用法

**第一步：** 拖拽一个 View 到 UIViewController 中 （可以是 storyboard 和  xib），修改 class 为 `IMUIInputView `。

**第二步：** 实现 `IMUIInputViewDelegate` 方法。

- 当用户点击发送按钮，并且输入框不为空，调用这个代理方法：

```
    func sendTextMessage(_ messageText: String)
```

- 当用户切换到录音模式时，调用这个代理方法：

```
    func switchToMicrophoneMode(recordVoiceBtn: UIButton)
```

- 开始录音是调用这个代理方法：

```
    func startRecordVoice()
```

- 完成录音后调用这个代理方法：

```
    func finishRecordVoice(_ voicePath: String, durationTime: Double)
```

- 取消录音调用这个代理方法：

```
    func cancelRecordVoice()
```

- 用户切换到相册模式时，调用这个代理方法：

```
    func switchToGalleryMode(photoBtn: UIButton)
```

- 在相册模式下选择了图片，用户点击发送按钮， 调用这个代理方法：

```
    func didSeletedGallery(AssetArr: [PHAsset])
```

- 用户切换到相机模式时调用这个代理方法：

```
    func switchToCameraMode(cameraBtn: UIButton)
```

- 相机模式下，用户完成照片拍摄，调用这个方法：

```
    func didShootPicture(picture: Data)
```

- 开始录制视频调用这个方法：
```
    func startRecordVideo()
```
- 相机模式下，用户完成视频拍摄，调用这个方法：

```
    func finishRecordVideo(videoPath: String, durationTime: Double)
```





# IMUICustomInputView
0.10.0 版本开始支持自定义 InputView，使用 ```IMUICustomInputView``` 这个类，```IMUIInputView``` 就是基于 ```IMUICustomInputView``` 类实现的。```IMUICustomInputView``` 所提供的功能是在 inputview 的 TextView 左边/右边/下边 的位置自定义 item，用法和 ```UICollectionview```/```UITableView``` 很像需要实现 ```IMUICustomInputViewDataSource``` 和```IMUICustomInputViewDelegate``` 这两个协议。

### 用法

**第一步：** 拖拽一个 View 到 UIViewController 中 （可以是 storyboard 和  xib），修改 class 为 `IMUICustomInputView `。

```swift
custom.inputViewDelegate = self
customInputview.dataSource = self
```



**第二步：** 实现 ```IMUICustomInputViewDataSource```

- 返回 TextView left/right/bottom items 的数量

  ```swift
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             numberForItemAt position: IMUIInputViewItemPosition) -> Int
  ```

- 返回 CumstomInputView left/right/bottom items 的的尺寸

  ```
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             _ position: IMUIInputViewItemPosition,
                             sizeForIndex indexPath: IndexPath) -> CGSize
  ```

- 返回 left/right/bottom items 的具体内容

  ```swift
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             _ position: IMUIInputViewItemPosition,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell
  ```

- 返回 FeaturenView 的内容

  ```swift
  func imuiInputView(_ featureView: UICollectionView,
                             cellForItem indexPath: IndexPath) -> UICollectionViewCell
  ```

  ​

**第三步：** 实现 ```IMUICustomInputViewDelegate```

- TextView 文本改变的事件回调：

  ```swift
  optional func textDidChange(text: String)
  ```

- 软键盘弹出的事件回调：

  ```swift
  optional func keyBoardWillShow(height: CGFloat, durationTime: Double)
  ```



### API：

##### register

- 注册 ```UICollectionviewCell ``` 到指定位置，用法和 ```UICollectionView``` 一样，在 ```imuiInputView(inputBarItemListView, position, cellForItemAt)``` 中使用 ```inputBarItemListView.dequeueReusableCell(withReuseIdentifier: CellIdentifier, for: indexPath)``` 前需要使用 ```register``` 方法，注册 inputBar item 提供两种注册方法如下。

```swift
  public func register(_ cellClass: AnyClass?, in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {  }
  
  public func register(_ nib: UINib?,in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {  }
```

example：

```swift
  customIntputView.self.register(
          UINib(nibName: "IMUIFeatureListIconCell", bundle: bundle), 
          in: .bottom, 
          forCellWithReuseIdentifier: "IMUIFeatureListIconCell"
  		)
  
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                     _ position: IMUIInputViewItemPosition,
                     cellForItemAt indexPath: IndexPath) -> UICollectionViewCell 
  {
       switch position {
        case .bottom:
          let cell = inputBarItemListView.dequeueReusableCell(
          withReuseIdentifier: cellIdentifier, 											
          for: indexPath)
        default:
        break
        }
  }
```

- featureView 也需要先 register，提供如下两种方法：

  ```swift
  public func registerForFeatureView(_ cellClass: AnyClass?, 
           forCellWithReuseIdentifier identifier: String) {  }

  public func registerForFeatureView(_ nib: UINib?, 
     forCellWithReuseIdentifier identifier: String) {  }
  ```

  example:

  ```swift
  customInputView.registerForFeatureView(
  		UINib(nibName: "IMUIRecordVoiceCell", bundle: bundle),
           forCellWithReuseIdentifier: "IMUIRecordVoiceCell"
         )
         
  func imuiInputView(_ featureView: UICollectionView,
               cellForItem indexPath: IndexPath) -> UICollectionViewCell {
    let cell = featureView.dequeueReusableCell(
            withReuseIdentifier: CellIdentifier, 
            for: indexPath
    		)
  }
  ```

##### update inputView 内的元素

当需要更新 inputBar's item 和 FeatureView 的内容时（比如没有选择图片，输入框没有文本时，我们需要将 发送按钮设为不可点状态），可以使用如下两个接口来更新。

```
//更新 inputBar's 上的 item
public func updateInputBarItemCell(_ position: IMUIInputViewItemPosition,
                                     at index: Int)
                                     
//更新 featureView 的内容，会重新请求 featureView            
public func reloadFeaturnView()
```

##### 弹出FeatureView

```
customInputView.showFeatureView()
```

##### 隐藏 FeatureView

```
customInputView.hideFeatureView()
```



