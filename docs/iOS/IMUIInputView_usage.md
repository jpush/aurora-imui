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



# IMUICustomInputView

from [0.10.0 Version] you can use ```IMUICustomInputView ``` to customize you inputView. ```IMUIInputView``` is basic in ```IMUICustomInputView```. You can use ```IMUICustomInputView``` to put anything into inputTextView's left/right/bottom position,and put anything into inputView's FeatureView. and it is easy used, just like ```UICollectionView``` or ```UITableView``` .You need implement ```IMUICustomInputViewDataSource``` and ```IMUICustomInputViewDelegate``` protocol.

### Usage:

**Setp one:** drag a view to your UIViewController (storyboard or xib), and adjust class to ```IMUICustomInputView```,and set customInput's delegate and dataSource.
**Setp two:** implement ```IMUICustomInputViewDataSource```

- return left/right/bottom inputBarItemListView's item number

  ```swift
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             numberForItemAt position: IMUIInputViewItemPosition) -> Int
  ```

- retuen left/right/bottom inputBarItemListView's item size

  ```swift
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             _ position: IMUIInputViewItemPosition,
                             sizeForIndex indexPath: IndexPath) -> CGSize
  ```

- return left/right/bottom inputBarItemListView's item cell

  ```swift
  func imuiInputView(_ inputBarItemListView: UICollectionView,
                             _ position: IMUIInputViewItemPosition,
                             cellForItemAt indexPath: IndexPath) -> UICollectionViewCell
  ```

- return featureView's cell

  ```swift
  func imuiInputView(_ featureView: UICollectionView,
                             cellForItem indexPath: IndexPath) -> UICollectionViewCell
  ```

**Setp three:** implement ```IMUICustomInputViewDelegate```

- Tells the delegate when inputTextview text did change

  ```swift
  optional func textDidChange(text: String)
  ```

- Tells the delegate when keyboard will show

  ```swift
  optional func keyBoardWillShow(height: CGFloat, durationTime: Double)
  ```

### API:

##### register

- register ```UICollectionViewCell``` to specified position, The usage just like ```UICollectionView```. there are two register function:

```swift
// register cell with class
public func register(_ cellClass: AnyClass?, in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {  }

// register cell with nib
public func register(_ nib: UINib?,in position: IMUIInputViewItemPosition, forCellWithReuseIdentifier identifier: String) {  }
```

example:

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

- featureView also need to register ```UICollectionViewCell ``` to featureView, there are two register function:

  ```swift
  // register cell with class
  public func registerForFeatureView(_ cellClass: AnyClass?, 
           forCellWithReuseIdentifier identifier: String) {  }

  // register cell with nib
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

##### update inputView's item(inputBar' cell and FeatureView's cell)

Sometime you need to update inputView's item(ex: if textView's text is empty, you want to set send button's color to gray that mean the button is unable to click), You can use the following methods：

```swift
//update inputBar's item
public func updateInputBarItemCell(_ position: IMUIInputViewItemPosition,
                                     at index: Int)
                                     
//update featureView's content，
public func reloadFeaturnView()
```

##### show featureView

```swift
customInputView.showFeatureView()
```

##### hidden featureView

```swift
customInputView.hideFeatureView()
```

