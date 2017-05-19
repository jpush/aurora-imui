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

