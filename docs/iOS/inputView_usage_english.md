# IMUIInputView
[中文文档](./inputView_usage.md)

This is a input component in chatting interface, can combine Aurora IMUIMessageCollection. Including features like record voice and video, select photo, take picture etc, supports customize style either.

## Usage
To use IMUIInputView only need two simple steps, or you can check out our sample project to try it yourself.
- **Setp one:** drag a view to your UIViewController (storyboard or xib), and adjust class to `IMUIInputView `

- **Setp two:** implement `IMUIInputViewDelegate`

- Tells the delegate that user tap send button and text input string is not empty（ **Note:** if selected photo in gallery mode,will send photo frist）
```
  func sendTextMessage(_ messageText: String)
```

- Tells the delegate that IMUIInputView will switch to recording voice mode
```
  func switchIntoRecordingVoiceMode(recordVoiceBtn: UIButton)
```

- Tells the delegate that start record voice
```
  func startRecordingVoice()
```

- Tells the delegate when finish record voice
```
  func finishRecordingVoice(_ voicePath: String, durationTime: Double)
```

- Tells the delegate that user cancel record
```
  func cancelRecordingVoice()
```

- Tells the delegate that IMUIInputView will switch to gallery
```
  func switchIntoSelectPhotoMode(photoBtn: UIButton)
```

- Tells the delegate that user did selected Photo in gallery
```
  func didSeletedGallery(AssetArr: [PHAsset])
```

- Tells the delegate that IMUIInputView will switch to camera mode
```
  func switchIntoCameraMode(cameraBtn: UIButton)
```

- Tells the delegate that user did shoot picture in camera mode
```
  func didShootPicture(picture: Data)
```

- Tells the delegate that user did shoot video in camera mode
```
  func didShootVideo(videoPath: String, durationTime: Double)
```
