IMUIMessageCollectionView is a message list in chatting interface, use to display all kinds of messages, and is can be fully customize. If you don't define your style, IMUIMessageCollectionView will use default style.

## Install
#### Manual
Copy `IMUI` folder to your project. That's it.

- **Note:** Make sure that `Info.plist` include  camera , Microphone, Photo Library permission

## Usage
To use IMUIMessageCollectionView only need three simple steps, or you can check out our [sample project](https://github.com/jpush/imui/tree/master/iOS/IMUIChat) to try it yourself.
- **Setp one:** drag a view to your UIViewController (storyboard or xib), and adjust class to `IMUIMessageCollectionView`

- **Setp two:** implement `IMUIMessageMessageCollectionViewDelegate`

```
  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.messageCollectionView.delegate = self
  }

// MARK - IMUIMessageMessageCollectionViewDelegate 
  func didTapMessageCell(_ model: IMUIMessageModel) {
  }
  
  func didTapMessageBubble(_ model: IMUIMessageModel) {
  }

  func willDisplayMessageCell(_ model: IMUIMessageModel, cell: Any) {
  }

  func didEndDisplaying(_ model: IMUIMessageModel, cell: Any) {
  }
```
- **Setp three:** construct your model

**1.** To be add messages, you need to implement `IMUIMessageModelProtocol` protocol into your existing mode
```
  protocol IMUIMessageModelProtocol {
    @request
    var msgId: String { get }
    var fromUser: IMUIUserProtocol { get }
    var layout: IMUIMessageCellLayoutProtocal { get }

    @optional
  //  文本消息 字符串
    func textMessage() -> String
  
  //  音频数据
    func mediaData() -> Data
  
  // 视频路径
    var videoPath: String? { get }
  
  // 消息泡泡图片
    var resizeBubbleImage: UIImage { get }
  }
```

**2.** construct your user model , to implement `IMUIUserProtocol` protocal

```
public protocol IMUIUserProtocol {
@request
  func userId() -> String 
  func displayName() -> String
  func Avatar() -> UIImage
}
```

## Data management
#### add new message
To add new message in message list is pretty easy, we support some way to add message to `IMUIMessageCollectionView`
- append message to bottom 
```
messageCollectionView.appendMessage(with message: IMUIMessageModel)
``` 

- insert message cell to top
```
messageCollectionViewinsertMessage(with message: IMUIMessageModel)
```
- insert messages cell to top
```
  insertMessages(with messages:[IMUIMessageModel])
```

## Custom  Layout
construct message object need return `IMUIMessageCellLayoutProtocal`  object used to layout MessageCell.
 If you return nil,will use default layout `IMUIMessageCellLayout ` .
If you want to adjust default layout, just change layout static value, like that

```
//  head image size
IMUIMessageCellLayout.avatarSize 

//  head image offset to  message cell
IMUIMessageCellLayout.avatarOffsetToCell

//   name label frame
IMUIMessageCellLayout.nameLabelFrame

// message bubble offset to head image
IMUIMessageCellLayout.bubbleOffsetToAvatar

// message cell width
IMUIMessageCellLayout.cellWidth

// message cell content inset
IMUIMessageCellLayout.cellContentInset
```

If you simply adjust the default layout of the configuration items can not meet their own layout needs.  Just construct you layout object. Inherit from `IMUIMessageCellLayout ` and override `IMUIMessageCellLayoutProtocal` function.

```
class MyMessageCellLayout: IMUIMessageCellLayout {
  
  override init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize) {
    
    super.init(isOutGoingMessage: isOutGoingMessage, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleContentSize)
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

## Future plan
- update message cell status
- custom message 
- message animation
- React Native support

