# 自定义界面样式

[English Docment](./customLayoutEn.md)

如果你需要对界面样式做更多定制化的需求，例如消息状态，设置已读标签等等，你可以使用自定义 ViewHolder 及布局。

## 用法
自定义界面的样式只需要三个步骤：

### 1. 创建自定义布局界面
在 layout 中创建你自己的布局文件。

### 2. 构建 ViewHolder 继承自 BaseMessageViewHolder 并实现 DefaultMessageViewHolder
自定义的 ViewHolder 类构造函数的两个参数类型必须为 View 以及 boolean。可以参考一下 messages 文件夹下的 ViewHolder 类。 例如：

```java
public class TxtViewHolder<MESSAGE extends IMessage>
        extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    public TxtViewHolder(View itemView, boolean isSender) {
        super(itemView);
        ...
    }

    @Override
    public void onBind(final MESSAGE message) {
        ...
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        ...
    }
```

### 3. 使用 HoldersConfig 对象设置 ViewHolder 及布局

```java
MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
// 第一个参数是自定义的 ViewHolder 类，第二个是自定义布局文件的资源 id
holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
```

这样就完成了自定义界面。



### 新增自定义类型的消息接口

新增 `SEND_CUSTOM`  和 `RECEIVE_CUSTOM` 消息类型。可以用 `HoldersConfig` 对象设置这两种自定义消息的布局和适配器：

```
MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
holdersConfig.setSendCustomMsg(CustomMessageViewHolder.class, layoutRes);
holdersConfig.setReceiveCustomMsg(CustomMessageViewHolder.class, layoutRes);
```



### 新增支持多种自定义消息接口（0.5.2 新增）

支持多种自定义消息。使用方式如下：

- 构造自定义 Adapter 继承自 `BaseMessageViewHolder` 并实现`DefaultMessageViewHolder`,  比如参考 `TxtViewHolder`:

```java
public class TxtViewHolder<MESSAGE extends IMessage>
        extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {
        ...
        }
```



- 构造 `CustomMsgConfig`, 调用 `MsgListAdapter.addCustomMsgType`

```java
MsgListAdapter adapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
// 第一个参数为 ViewType，不能设置为 0-12 的整形
// 第二个参数为 resource id
// 第三个参数为是否为发送方
// 第四个为自定义 ViewHolder 的 Class 对象 
CustomMsgConfig config1 = new CustomMsgConfig(13, R.layout.item.send_custom, true, DefaultCustomViewHolder.class);
// 第一个参数为 ViewType，同上
adapter.addCustomMsgType(13, config1);
```

- 在你的 `Message` 实体中（实现了 `IMessage` 的类），需要设置对应的自定义消息的类型的 type 值。例如：

```java
public class MyMessage implements IMessage {
 	private int type;
	// 本例中 viewType 为 13
	public void setType(int viewType) {
      this.type = viewType;
	}
  
    @Override
    public int getType() {
      return this.type;
    }
}
```

