# IMUIInputView
[English document](./inputView_usage_english.md)

这是一个聊天界面输入框组件，可以方便地结合 `IMUIMessageCollectionView` 使用，包含录音，选择图片，拍照等功能，提供了一些丰富的接口和回调供用户使用， 还可以选择自定义样式。

## 用法
**第一步：** 拖拽一个 View 到 UIViewController 中 （可以是 storyboard 和  xib），修改 class 为 `IMUIInputView `

**第二步：** 实现 `IMUIInputViewDelegate` 方法

- 当用户点击发送按钮，并且输入框不为空，调用这个代理方法

```
  func sendTextMessage(_ messageText: String)
```

- 当用户切换到录音模式时，调用这个代理方法

```
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
```

- 开始录音是调用这个代理方法

```
  func startRecordingVoice()
```

- 完成录音后调用这个代理方法

```
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
```

- 取消录音调用这个代理方法

```
  func cancelRecordingVoice()
```

- 用户切换到相册模式时，调用这个代理方法

```
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
```

- 在相册模式下选择了图片，用户点击发送按钮， 调用这个代理方法

```
  func didSeletedGallery(AssetArr: [PHAsset])
```

- 用户切换到相机模式时调用这个代理方法

```
  func switchIntoCameraMode(cameraBtn: UIButton)
```

- 相机模式下，用户完成照片拍摄，调用这个方法

```
  func didShootPicture(picture: Data)
```

- 相机模式下，用户完成视频拍摄，调用这个方法

```
  func didShootVideo(videoPath: String, durationTime: Double)
```
