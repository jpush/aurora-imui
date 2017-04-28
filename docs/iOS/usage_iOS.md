# IMUIMessageCollectionView
[English document](./usage_english.md)

IMUIMessageCollectionView 是聊天界面的消息列表，用来展示各种类型的消息，可以支持丰富的自定义扩展。如果不使用自定义将会使用默认样式。 


## 安装
#### 手动集成
拷贝 `IMUI` 目录到自己工程中。

**注意：** 确保自己工程中 `Info.plist` 包含 camera , Microphone 和 Photo Library 权限。

## 使用
使用 IMUIMessageCollectionView 只需要几个简单的步骤。

第一步： 拖拽一个 View 到 UIViewController 中 （可以是 storyboard 和  xib），修改 class 为 `IMUIMessageCollectionView`。

第二步： 实现 `IMUIMessageMessageCollectionViewDelegate` 方法。

```
  @IBOutlet weak var messageCollectionView: IMUIMessageCollectionView!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.messageCollectionView.delegate = self
  }

// MARK - IMUIMessageMessageCollectionViewDelegate 

  func messageCollectionView(_: UICollectionView, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  
  func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell, model: IMUIMessageModelProtocol)
  
  func messageCollectionView(_: UICollectionView, willDisplayMessageCell: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(_ willBeginDragging: UICollectionView){}
```

第三步： 构造实体类型
- 构建消息类型， 实现 `IMUIMessageModelProtocol` 协议：

```
protocol IMUIMessageModelProtocol {
  @request
  var msgId: String { get }
  var fromUser: IMUIUserProtocol { get }
  var layout: IMUIMessageCellLayoutProtocal { get }
  // 消息为发出的消息
  var isOutGoing: Bool { get }
  
  @optional
  
  // 消息的时间
  var timeString: String { get }
  
  //  文本消息字符串
  func text() -> String

  // 媒体文件路径
  func mediaFilePath() -> String

  // 语音和视频的时长
  var duration: CGFloat { get }

  // 消息泡泡图片
  var resizeBubbleImage: UIImage { get }
}
```

- 构建用户类型， 实现 `IMUIUserProtocol` 协议：
```
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
```
  messageCollectionView.appendMessage(with message: IMUIMessageModel)
```
- 插入消息到指定位置：
```
  messageCollectionView.insertMessage(with message: IMUIMessageModel)
```
- 插入一串消息到指定位置：
```  
  messageCollectionView.insertMessages(with messages:[IMUIMessageModel])
```

### 自定义布局
创建 Message 对象的时候需要指定布局信息，如果不指定则会使用默认布局 `IMUIMessageCellLayout`。
如果需要在默认布局的基础上简单调整 message cell 内的元素，这里提供了简单的配置项。
```
// 设置头像的尺寸
IMUIMessageCellLayout.avatarSize

// 设置头像在 Cell 里面的偏移量
IMUIMessageCellLayout.avatarOffsetToCell

// 设置头像在 Cell 里面的偏移量
IMUIMessageCellLayout.nameLabelFrame

// 设置消息 Bubble 相对于 头像的偏移量
IMUIMessageCellLayout.bubbleOffsetToAvatar

// 设置 Message Cell 的的宽度
IMUIMessageCellLayout.cellWidth

// 设置 Message Cell 的内边距
IMUIMessageCellLayout.cellContentInset
```
如果简单调整 默认布局 的配置项满足局需求，需要不了自己的布在构造方法中指定 自定义的布局, 自定义布局需要实现`IMUIMessageCellLayoutProtocal `。也可以继承 `IMUIMessageCellLayout` 类，根据自己的需求 override 方法。例如：
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

## 未来计划
- 消息状态更新
- 自定义消息支持
- 消息动画
- React Native 版本
