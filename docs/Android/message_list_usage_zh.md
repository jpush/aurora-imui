# 消息列表
[English Document](./message_list_usage.md)

聊天的消息列表，用于展示各种类型消息，支持丰富的自定义扩展。如果不做自定义则使用默认样式。

## 安装
提供了以下几种方式添加依赖，只需要选择其中一种即可。

- Gradle

```groovy
compile 'cn.jiguang.imui:messagelist:0.2.0'
```

- Maven
```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>messagelist</artifactId>
  <version>0.2.0</version>
  <type>pom</type>
</dependency>
```

- JitPack
```groovy
// project/build.gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

// module/build.gradle
dependencies {
  compile 'com.github.jpush:imui:0.2.0'
}
```

## 使用
使用消息列表只需几个简单的步骤，可以参考一下 [demo](./../../Android/sample)。

### 1. 在布局文件中引用 MessageList：
```xml
<cn.jiguang.imui.messages.MessageList
    android:id="@+id/msg_list"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:avatarHeight="50dp"
    app:avatarWidth="50dp"
    app:bubbleMaxWidth="0.70"
    app:dateTextSize="14sp"
    app:receiveBubblePaddingLeft="20dp"
    app:receiveBubblePaddingRight="10dp"
    app:receiveTextColor="#ffffff"
    app:receiveTextSize="18sp"
    app:sendBubblePaddingLeft="10dp"
    app:sendBubblePaddingRight="20dp"
    app:sendTextColor="#7587A8"
    app:sendTextSize="18sp" />
```
我们定义了很多样式，供用户调整布局，详细的属性可以参考 [attrs.xml](./../src/main/res/values/attrs.xml) 文件，当然也支持完全自定义布局，下面会介绍到。

### 2. 构造 Adapter
MsgListAdapter 的构造函数有三个参数：

1. Sender Id: 发送方 Id(唯一标识)。
2. HoldersConfig，可以用这个对象来[构造自定义消息的 ViewHolder 及布局界面](./customLayout.md)。
3. ImageLoader 的实例，用来展示头像。如果为空，将会隐藏头像。（[点击](./imageLoader.md)了解更多关于 ImageLoader 的内容）。

```java
MsgListAdapter adapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
messageList.setAdapter(adapter);
```

### 3.构造实体类
包括消息、用户（以及以后的会话），需要实现 IMessage、IUser 接口。例如：
```java
public class MyMessage implements IMessage {

    private long id;
    private String text;
    private String timeString;
    private MessageType type;
    private IUser user;
    private String contentFile;
    private long duration;

    public MyMessage(String text, MessageType type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    @Override
    public String getMsgId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getFromUser() {
        if (user == null) {
            return new DefaultUser("0", "user1", null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public void setMediaFilePath(String path) {
        this.contentFile = path;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String getTimeString() {
        return timeString;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return contentFile;
    }
}
```
上面的 MessageType 是 IMessage 中的枚举类。同样 IUser 接口也需要实现：
```java
public class DefaultUser implements IUser {

    private String id;
    private String displayName;
    private String avatar;

    public DefaultUser(String id, String displayName, String avatar) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }
}
```
以上就完成了所有的准备工作。

## 数据管理
### 新增消息
在消息列表中插入消息非常简单，这里提供了两种方式：

- *addToStart(IMESSAGE message, boolean scroll)*
```java
// 在底部插入一条消息，第二个参数表示是否滚动到底部。
adapter.addToStart(message, true);
```

- *addToEnd(List<IMessage> messages)*
```java
// 在消息列表的顶部加入消息，消息列表应当按日期增序存放。
adapter.addToEnd(messages);
```

- 滚动列表加载历史消息
设置监听 `OnLoadMoreListener`，当滚动列表时就会触发 `onLoadMore` 事件，例如：
```java
mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
    @Override
    public void onLoadMore(int page, int totalCount) {
        if (totalCount < mData.size()) {
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     mAdapter.addToEnd(mData);
                 }
             }, 1000);
        }
    }
});
```

### 删除消息
有如下删除消息的接口：

- *adapter.deleteById(String id)*: 根据 message id 来删除消息
- *adapter.deleteByIds(String[] ids)*: 根据 message id 批量删除
- *adapter.delete(IMessage message)*: 根据消息对象删除
- *adapter.delete(List<IMessage> messages)*: 批量删除
- *adapter.clear()*: 清空消息

### 更新消息
如果消息改变了，可以调用以下接口来更新消息：

- *adapter.update(IMessage message)*
- *adapter.update(String oldId, IMessage newMessage)*

### 事件监听
- *OnMsgClickListener*: 点击消息触发
```java
mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
    @Override
    public void onMessageClick(MyMessage message) {
        // do something
    }
});
```

- *OnAvatarClickListener*: 点击头像触发
```java
mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
    @Override
    public void onAvatarClick(MyMessage message) {
        DefaultUser userInfo = (DefaultUser) message.getUserInfo();
        // do something
    }
});
```

- *OnMsgLongClickListener*: 长按消息触发
```java
mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
    @Override
    public void onMessageLongClick(MyMessage message) {
        // do something
    }
});
```

- *OnMsgResendListener*: 点击重新发送按钮触发
```java
mAdapter.setMsgResendListener(new MsgListAdapter.OnMsgResendListener<MyMessage>() {
    @Override
    public void onMessageResend(MyMessage message) {
       // resend message here
    }
 });
```
