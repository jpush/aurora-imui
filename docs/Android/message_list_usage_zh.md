# 消息列表
[English Document](./message_list_usage.md)

聊天的消息列表，用于展示各种类型消息，支持丰富的自定义扩展。如果不做自定义则使用默认样式。

## 安装
提供了以下几种方式添加依赖，只需要选择其中一种即可。

- Gradle

```groovy
compile 'cn.jiguang.imui:messagelist:0.8.0'
```

- Maven
```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>messagelist</artifactId>
  <version>0.8.0</version>
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
  compile 'com.github.jpush:imui:0.7.7'
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


#### 支持下拉刷新布局

如果需要使用下拉刷新的消息列表，可以使用 `PullToRefreshLayout` 来包裹 `MessageList`，此外还需要实现下拉刷新的接口。用法如下：

```
<cn.jiguang.imui.messages.ptr.PullToRefreshLayout
    android:id="@+id/pull_to_refresh_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:PtrCloseDuration="300"
    app:PtrCloseHeaderDuration="2000"
    app:PtrKeepHeaderWhenRefresh="true"
    app:PtrPullToRefresh="true"
    app:PtrRatioHeightToRefresh="1.2"
    app:PtrResistance="1.2"
    android:layout_above="@+id/chat_input"
    android:layout_below="@+id/title_container">

    <cn.jiguang.imui.messages.MessageList
        android:id="@+id/msg_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:avatarHeight="48dp"
        app:avatarWidth="48dp"
        app:showReceiverDisplayName="true"
        app:showSenderDisplayName="false"
        app:avatarRadius="5dp"
        app:bubbleMaxWidth="0.70"
        app:dateTextSize="14sp"
        app:receiveBubblePaddingLeft="16dp"
        app:receiveBubblePaddingRight="8dp"
        app:receiveTextColor="#ffffff"
        app:receiveTextSize="14sp"
        app:sendBubblePaddingLeft="8dp"
        app:sendBubblePaddingRight="16dp"
        app:sendTextColor="#7587A8"
        app:sendTextSize="14sp" />

</cn.jiguang.imui.messages.ptr.PullToRefreshLayout>
```

定义了 xml 后，添加 Header View，并实现下拉刷新接口：

```
final PullToRefreshLayout ptrLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
PtrDefaultHeader header = new PtrDefaultHeader(getContext());
int[] colors = getResources().getIntArray(R.array.google_colors);
header.setColorSchemeColors(colors);
header.setLayoutParams(new LayoutParams(-1, -2));
header.setPadding(0, DisplayUtil.dp2px(getContext(),15), 0,DisplayUtil.dp2px(getContext(),10));
header.setPtrFrameLayout(ptrLayout);
ptrLayout.setLoadingMinTime(1000);
ptrLayout.setDurationToCloseHeader(1500);
ptrLayout.setHeaderView(header);
ptrLayout.addPtrUIHandler(header);
// 如果设置为 true，下拉刷新时，内容固定，只有 Header 变化
ptrLayout.setPinContent(true);
ptrLayout.setPtrHandler(new PtrHandler() {
  @Override
  public void onRefreshBegin(PullToRefreshLayout layout) {
      Log.i("MessageListActivity", "Loading next page");
      loadNextPage();
      // 加载完历史消息后调用
      ptrLayout.refreshComplete();
  }
});
```

`PullToRefreshLayout` 默认的 Header 是 Material 风格的，你也可以参考 [android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) 来实现各种风格的 Header。

我们定义了很多样式，供用户调整布局，详细的属性可以参考 [attrs.xml](./../../Android/messagelist/src/main/res/values/attrs.xml) 文件，当然也支持完全自定义布局，下面会介绍到。

#### 设置 MessageList 自定义属性

基本上所有的属性都支持在 xml 或在代码中设置，用户可自行选择。所有可以设置的自定义属性可以参考 [attrs.xml](/../../Android/messagelist/src/main/res/values/attrs.xml). 下面展示一下如何设置是否显示昵称。

```Java
MessageList messageList = (MessageList) findViewById(R.id.msg_list);
```

- 设置接收方或者发送方显示昵称，可以在上面的 xml 中设置 `showReceiverDisplayName` 及 `showSenderDisplayName` 为 true 或者 false. true 表示展示昵称，false 为不展示。也可以在代码中设置：

  ```Java
  messageList.setShowSenderDisplayName(true);
  messageList.setShowReceiverDisplayName(true);
  ```

- 禁止下拉刷新（0.4.8 新增接口），调用 `messageList.forbidScrollToRefresh(true)` 即可

    ```Java
    messageList.forbidScrollToRefresh(true);
    ```





### 2. 构造 Adapter
MsgListAdapter 的构造函数有三个参数：

1. Sender Id: 发送方 Id(唯一标识)。
2. HoldersConfig，可以用这个对象来[构造自定义消息的 ViewHolder 及布局界面](./custom_layout_zh.md)。
3. ImageLoader 的实例，用来展示头像。如果为空，将会隐藏头像。（[点击](./image_loader_zh.md)了解更多关于 ImageLoader 的内容）。

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
    private int type;
    private IUser user;
    private String contentFile;
    private long duration;

    public MyMessage(String text, int type) {
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
    public int getType() {
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

- addToEndChronologically(List<IMessage> messages)（**0.7.2 后新增**）

```java
// 在消息列表顶部插入消息，参数列表按照时间顺序排序(最后一条消息是最新的)。
adapter.addToEndChronologically(messages);
```

- addToEnd(List<IMessage> messages)
```java
// 在消息列表的顶部加入消息，消息列表应当按日期降序存放（第一条消息是最新的）。
adapter.addToEnd(messages);
```

- 滚动列表加载历史消息（**注意：如果使用了 `PullToRefreshLayout` 跳过这个部分**）
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

  ​

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

- *OnMsgLongClickListener*: 长按消息触发（0.6.4 后增加参数 View）
```java
mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
    @Override
    public void onMessageLongClick(View view,MyMessage message) {
        // do something
    }
});
```

- *OnMsgStatusViewClickListener*: 点击消息状态按钮触发（原来为 OnMsgResendListener，0.4.7 版本后修改）
```java
mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
    @Override
    public void onStatusViewClick(MyMessage message) {
      //message status view click, resend or download here
    }
 });
```



### 混淆

如果需要混淆代码，需要添加如下配置：

```
-keep class cn.jiguang.imui.** { *; }
```

