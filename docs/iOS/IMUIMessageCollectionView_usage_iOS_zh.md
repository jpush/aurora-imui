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

