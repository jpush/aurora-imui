# IMUIMessageCollectionView
[English document](./IMUIMessageCollectionView_usage.md)

IMUIMessageCollectionView 是聊天界面的消息列表，用来展示各种类型的消息，可以支持丰富的自定义扩展。若不自定义，则使用默认样式。

## 安装
### [CocoaPods](https://cocoapods.org/)  (推荐)
```ruby
# For latest release in cocoapods
pod 'AuroraIMUI'
```

### 手动集成
拷贝 `IMUICommon/` `IMUIMessageCollectionView/`  这两个目录到自己工程中。

>**注意**：确保自己工程中 `Info.plist` 包含 camera , Microphone 和 Photo Library 权限。

## 使用
使用 IMUIMessageCollectionView 只需要几个简单的步骤，可以参考 [sample 工程](./../../iOS/sample)：

第一步：拖拽一个 View 到 UIViewController 中（可以是 storyboard 和 xib），修改 class 为 `IMUIMessageCollectionView`。

第二步：实现 `IMUIMessageMessageCollectionViewDelegate` 方法：

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
                                             model: IMUIMessageModelProtocol) {}

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

第三步： 构造实体类型
1. 构建消息类型，实现 `IMUIMessageModelProtocol` 协议：

  ```swift
  protocol IMUIMessageModelProtocol {
    @request
    var msgId: String { get }
    var fromUser: IMUIUserProtocol { get }
    var layout: IMUIMessageCellLayoutProtocal { get }
    var isOutGoing: Bool { get }  // 是否是发出的消息

    @optional
    // 消息的时间
    var timeString: String { get }

    // 文本消息字符串
    func text() -> String

    // 媒体文件路径
    func mediaFilePath() -> String

    // 语音和视频的时长
    var duration: CGFloat { get }

    // 消息气泡图片
    var resizeBubbleImage: UIImage { get }
  }
  ```

2. 构建用户类型，实现 `IMUIUserProtocol` 协议：
  ```swift
  public protocol IMUIUserProtocol {
    @request
    func userId() -> String
    func displayName() -> String
    func Avatar() -> UIImage
  }
  ```

### 数据管理
增加新的消息
- 插入消息到底部：
```swift
messageCollectionView.appendMessage(with message: IMUIMessageModel)
```

- 插入消息到指定位置：
```swift
messageCollectionView.insertMessage(with message: IMUIMessageModel)
```

- 插入一串消息到指定位置：
```swift
messageCollectionView.insertMessages(with messages:[IMUIMessageModel])
```

- 消息更新 （也可用于消息状态更新）：

```
updateMessage(with message:IMUIMessageModel)
```

- 删除消息：

```
removeMessage(with messageId: String)
```

### 自定义布局

创建 Message 对象的时候需要指定布局信息，如果不指定则会使用默认布局 `IMUIMessageCellLayout`。
如果需要在默认布局的基础上简单调整 message cell 内的元素，这里提供了简单的配置项：

```swift
// 设置头像的尺寸
IMUIMessageCellLayout.avatarSize

// 设置头像在 Cell 里面的偏移量
IMUIMessageCellLayout.avatarOffsetToCell

// 设置头像在 Cell 里面的偏移量
IMUIMessageCellLayout.nameLabelFrame

// 设置消息 Bubble 相对于头像的偏移量
IMUIMessageCellLayout.bubbleOffsetToAvatar

// 设置 Message Cell 的宽度
IMUIMessageCellLayout.cellWidth

// 设置 Message Cell 的内边距
IMUIMessageCellLayout.cellContentInset
```

如果上面的配置项无法满足需求，可以自己在构造方法中指定自定义布局，自定义布局需要实现 `IMUIMessageCellLayoutProtocal`，
也可以继承 `IMUIMessageCellLayout` 类，根据自己的需求实现方法。
例如：
```swift
class MyMessageCellLayout: IMUIMessageCellLayout {

  override init(isOutGoingMessage: Bool, isNeedShowTime: Bool, bubbleContentSize: CGSize) {
    super.init(isOutGoingMessage: isOutGoingMessage,
                  isNeedShowTime: isNeedShowTime,
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
## 自定义消息

#### 定义消息内容
如果想要定义消息泡泡中的内容，需要实现 `IMUIMessageCellLayoutProtocol` 的如下两个方法：
```swift
// 自定义的 message BubbleView 必须是 UIView 的子类, 对 bubbleContentView 的事件交给开发者那边自行处理 (NOTE：使用的时候无需对 bubbleContentView 做缓存，内部会做缓存)
var bubbleContentView: IMUIMessageContentViewProtocol { get }

// 用于标识 bubbleContentView， （NOTE：不同类型的消息 bubbleContentType 不能一样）
var bubbleContentType: String { get }
```

#### 自定义 statusView

如果想要修改 statusView 需要实现 `IMUIMessageCellLayoutProtocol` 的如下两个方法：

```swift
// statusView 必须是 UIView 的子类，(NOTE：使用的时候无需对 statusView 做缓存，内部会做缓存)
var statusView: IMUIMessageStatusViewProtocol { get }

// 返回 statusView 的位置信息
var statusViewFrame: CGRect { get }
```

#### 完全自定义消息

如果上述的方式无法满足你的需求（比如事件消息只需要一个 label 用于显示消息），需要使用完全自定义的布局方式，这种方式可以在 `IMUIMessageCollectionView` 中显示自己定义的 `UICollectionviewCell`。下面是完全自定义消息的步骤：

- **第一步：**构建 Message Model 需要遵从 `IMUIMessageProtocol` 协议
   ```
    var msgId: String { get }
   ```

- **第二步：**实现 `IMUIMessageMessageCollectionViewDelegate` 中的如下两个方法。

  ```swift
  // 用于返回自定定义的 UICollectionViewCell 的高度
  func messageCollectionView(messageCollectionView: UICollectionView, forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> UICollectionViewCell?

  // 用于返回自己定义的 UICollectionviewCell 的高度
  func messageCollectionView(messageCollectionView: UICollectionView, heightForItemAtIndexPath forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> NSNumber?
  ```

- **第三步：**把构建好的消息发往 `IMUIMessageCollectionView`

   ```
   // messageCollectionView 为 IMUIMessageCollectionView 的实例
   messageCollectionView.appendMessage(with message: IMUIMessageModel)
   ```

   ​