# IMUIInputView
这是一个聊天界面输入框组件，可以方便地结合 `IMUIMessageCollectionView` 使用，包含录音，选择图片，拍照等功能，提供了一些丰富的接口和回调供用户使用， 还可以选择自定义样式。

## 用法
**第一步：** 拖拽一个 View 到 UIViewController 中 （可以是 storyboard 和  xib），修改 class 为 `IMUIInputView `
**第二步：** 实现 `IMUIInputViewDelegate` 方法

```
  /**
   *  Tells the delegate that user tap send button and text input string is not empty
   */
  func sendTextMessage(_ messageText: String)
  
  /**
   *  Tells the delegate that IMUIInputView will switch to recording voice mode
   */
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
  
  /**
   *  Tells the delegate that start record voice
   */
  func startRecordingVoice()
  
  /**
   *  Tells the delegate when finish record voice
   */
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
  
  /**
   *  Tells the delegate that user cancel record
   */
  func cancelRecordingVoice()
  
  /**
   *  Tells the delegate that IMUIInputView will switch to gallery
   */
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
  
  /**
   *  Tells the delegate that user did selected Photo in gallery
   */
  func didSeletedGallery(AssetArr: [PHAsset])
  
  /**
   *  Tells the delegate that IMUIInputView will switch to camera mode
   */
  func switchIntoCameraMode(cameraBtn: UIButton)
  
  /**
   *  Tells the delegate that user did shoot picture in camera mode
   */
  func didShootPicture(picture: Data)
  
  /**
   *  Tells the delegate that user did shoot video in camera mode
   */
  func didShootVideo(videoPath: String, durationTime: Double)
```