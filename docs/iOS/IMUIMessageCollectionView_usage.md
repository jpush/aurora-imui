# IMUIMessageCollectionView
[中文文档](./IMUIMessageCollectionView_usage_iOS_zh.md)

IMUIMessageCollectionView is a message list in chatting interface, use to display all kinds of messages, and it can be fully customize. If you don't define your style, IMUIMessageCollectionView will use default style.

## Install
### [CocoaPods](https://cocoapods.org/)  (recommended)

```ruby
# For latest release in cocoapods
pod 'AuroraIMUI'
```

### Manual
Copy `IMUICommon`  and `IMUIMessageCollectionView` folder to your project. That's it.

>**Note:** Make sure that `Info.plist` include camera, Microphone, Photo Library permission.

## Usage
To use IMUIMessageCollectionView only need three simple steps, or you can check out our [sample project](./../../iOS/sample) to try it yourself.
- **Step one:** drag a view to your UIViewController (storyboard or xib), and adjust class to `IMUIMessageCollectionView`.

- **Step two:** implement `IMUIMessageMessageCollectionViewDelegate`.

  ```swift
  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!

  override func viewDidLoad() {
    super.viewDidLoad()
    self.messageCollectionView.delegate = self
  }

  // MARK - IMUIMessageMessageCollectionViewDelegate
  func messageCollectionView(_: UICollectionView,
                     forItemAt: IndexPath,
                         model: IMUIMessageModelProtocol) {}

  func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell,
                                                 model: IMUIMessageModelProtocol) {}

  func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell,
                                               model: IMUIMessageModelProtocol)

  func messageCollectionView(didTapStatusViewInCell: UICollectionViewCell, 
  									     model: IMUIMessageModelProtocol)

  func messageCollectionView(_: UICollectionView,
        willDisplayMessageCell: UICollectionViewCell,
                     forItemAt: IndexPath,
                         model: IMUIMessageModelProtocol) {}

  func messageCollectionView(_: UICollectionView,
              didEndDisplaying: UICollectionViewCell,
                     forItemAt: IndexPath,
                         model: IMUIMessageModelProtocol) {}

  func messageCollectionView(_ willBeginDragging: UICollectionView){}
  ```
- **Step three:** construct your model

  1. To add messages, you need to implement `IMUIMessageModelProtocol` protocol into your existing mode.
  ```swift
  protocol IMUIMessageModelProtocol {
      @request
      var msgId: String { get }
      var fromUser: IMUIUserProtocol { get }
      var layout: IMUIMessageCellLayoutProtocal { get }
      var isOutGoing: Bool { get }

      @optional
      // return time lable string
      var timeString: String { get }

      // return text message's string
      func text() -> String

      // return media(image, voice, video ) file path
      func mediaFilePath() -> String

      // return duration of audio or video
      var duration: CGFloat { get }

      // the bubble background image
      // @warning the image must be resizable just like this:
      // bubbleImg.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
      var resizeBubbleImage: UIImage { get }
  }
  ```

  2. Construct your user model, to implement `IMUIUserProtocol` protocal.
  ```swift
  public protocol IMUIUserProtocol {
      @request
      func userId() -> String
      func displayName() -> String
      func Avatar() -> UIImage
  }
  ```

### Data management
#### Add new message
To add new message in message list is pretty easy, we support some way to add message to `IMUIMessageCollectionView`.
- append message to bottom:
  ```swift
  messageCollectionView.appendMessage(with message: IMUIMessageModel)
  ```

- insert message cell to top:
  ```swift
  messageCollectionViewinsertMessage(with message: IMUIMessageModel)
  ```

- insert messages cell to top:
  ```swift
  insertMessages(with messages:[IMUIMessageModel])
  ```

- update message cell (you can use this function to update message's status or ):

  ```
  updateMessage(with message:IMUIMessageModel)
  ```

- remove message cell:

  ```
  removeMessage(with messageId: String)
  ```

  ​

### Custom  Layout

Create `MessageModel` object need to specify layout infomation, if not, will use default layout `IMUIMessageCellLayout`. Base on default layout, here offers simple configuration to adjust elements in `MessageCell`:

```swift
// head image size
IMUIMessageCellLayout.avatarSize

// head image offset to message cell
IMUIMessageCellLayout.avatarOffsetToCell

// name label frame
IMUIMessageCellLayout.nameLabelFrame

// message bubble offset to head image
IMUIMessageCellLayout.bubbleOffsetToAvatar

// message cell width
IMUIMessageCellLayout.cellWidth

// message cell content insert
IMUIMessageCellLayout.cellContentInset
```

If you simply adjust the default layout of the configuration items can not meet their own layout needs.  Just construct you layout object. Inherit from `IMUIMessageCellLayout` and override `IMUIMessageCellLayoutProtocal` function.

```swift
class MyMessageCellLayout: IMUIMessageCellLayout {

  override init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize) {
    super.init(isOutGoingMessage: isOutGoingMessage, isNeedShowTime: isNeedShowTime,
       bubbleContentSize: bubbleContentSize)
  }

  override var bubbleContentInset: UIEdgeInsets {
    if isOutGoingMessage {
      return UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 15)
    } else {
      return UIEdgeInsets(top: 10, left: 15, bottom: 10, right: 10)
    }
  }
}
```

#### Custom Bubble Content

if you want to show your view in message's bubble, you should implement two functions defined in `IMUIMessageCellLayoutProtocal` , just like following:

```swift
// return bubble content's View, this view will should in messageBubble. (NOTE: bubbleContentView must be a subclass of uiview, and you shouldn't store bubbleContentView yourself)
var bubbleContentView: IMUIMessageContentViewProtocol { get }
  
// return bubble content's type,
var bubbleContentType: String { get }
```

#### Custom Status View

if you want to use your custom UIView as statusView, you can implement two functions defined in `IMUIMessageCellLayoutProtocal` , just like following:

```swift
// return status View ,(NOTE: statusView must be a subclass of uiview, and you shouldn't store statusView yourself)
var statusView: IMUIMessageStatusViewProtocol { get }

// return statusView's frame in message cell
var statusViewFrame: CGRect { get }
```

### Fully custom messages

If the above method does not meet your needs (such as event messages only need a label for the display of the message), you need to use a fully custom layout, in this way you can show `UICollectionviewCell` in `IMUIMessageCollectionView`  . To use fully customize the message, you need the following steps:

- **Step one:** construct your message model, you model need comfort `IMUIMessageProtocol`

 ```swift
  var msgId: String { get }
 ```
- **Step two:** implement two functions defined in `IMUIMessageMessageCollectionViewDelegate`,  just like following:

  ```swift
  // return your UICollectionViewCell
  func messageCollectionView(messageCollectionView: UICollectionView, forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> UICollectionViewCell?

  // return your UIcollectionViewCell's height
  func messageCollectionView(messageCollectionView: UICollectionView, heightForItemAtIndexPath forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> NSNumber?
  ```

- **Step three:** send you message instance to message list：

    ```
    // messageCollectionView is an instance of IMUIMessageCollectionView
    messageCollectionView.appendMessage(with message: IMUIMessageModel)
    ```

    ​

