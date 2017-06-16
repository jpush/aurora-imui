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

