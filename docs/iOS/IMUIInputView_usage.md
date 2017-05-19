# IMUIInputView
[中文文档](./IMUIInputView_usage_zh.md)

This is a input component in chatting interface, can combine Aurora IMUIMessageCollection. Including features like record voice and video, select photo, take picture etc, supports customize style either.

## Install

### [CocoaPods](https://cocoapods.org/)  (recommended)

```ruby
# For latest release in cocoapods
pod 'AuroraIMUI'
```
### Manual
Copy `IMUICommon`  and `IMUIInputView` folder to your project. That's it.

> **Note:** Make sure that `Info.plist` include camera, Microphone, Photo Library permission.

## Usage

To use IMUIInputView only need two simple steps, or you can check out our sample project to try it yourself.

**Setp one:** drag a view to your UIViewController (storyboard or xib), and adjust class to `IMUIInputView `

**Setp two:** implement `IMUIInputViewDelegate`

- Tells the delegate that user tap send button and text input string is not empty（ **Note:** if selected photo in gallery mode,will send photo frist）:
```
  func sendTextMessage(_ messageText: String)
```

- Tells the delegate that IMUIInputView will switch to recording voice mode:
```
  func switchToMicrophoneMode(recordVoiceBtn: UIButton)
```

- Tells the delegate that start record voice:
```
  func startRecordVoice()
```

- Tells the delegate when finish record voice:
```
  func finishRecordVoice(_ voicePath: String, durationTime: Double)
```

- Tells the delegate that user cancel record:
```
  func cancelRecordVoice()
```

- Tells the delegate that IMUIInputView will switch to gallery:
```
  func switchToGalleryMode(photoBtn: UIButton)
```

- Tells the delegate that user did selected Photo in gallery:
```
  func didSeletedGallery(AssetArr: [PHAsset])
```

- Tells the delegate that IMUIInputView will switch to camera mode:
```
  func switchToCameraMode(cameraBtn: UIButton)
```

- Tells the delegate that user did shoot picture in camera mode:
```
  func didShootPicture(picture: Data)
```

- Tells the delegate when starting record video:

```
  func startRecordVideo()
```

- Tells the delegate that user did shoot video in camera mode:
```
  func finishRecordVideo(videoPath: String, durationTime: Double)
```

