# MessageList
[中文文档](./message_list_usage_zh.md)

MessageList is a message list in chatting interface, use to display all kinds of messages, and it can be fully customize.
If you don't define your style, MessageList will use default style.

## Install

We have support several ways to add dependency. You can choose one of them.

- Gradle:
```groovy
compile 'cn.jiguang.imui:messagelist:0.8.0'
```

-  Maven：
```groovy
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>messagelist</artifactId>
  <version>0.8.0</version>
  <type>pom</type>
</dependency>
```

- JitPack
```groovy
// Add in project's build.gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

// Add in module's build.gradle
dependencies {
    compile 'com.github.jpush:imui:0.7.7'
}
```

## Usage
To use MessageList only need three simple steps, or you can check out our [sample project](./../../sample) to
try it yourself.

### 1. Add MessageList in your xml layout：
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


#### Support Pull To Refresh Layout

If you prefer add pull to refresh feature to `MessageList`, then you should use `PullToRefreshLayout` to wrap `MessageList`, for example:

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

then you should add a header to `PullToRefreshLayout` and implements pull to refresh interface:

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
// If set to true，when pull to refresh, the content will be 
// fixed, only the header view changes
ptrLayout.setPinContent(true);
ptrLayout.setPtrHandler(new PtrHandler() {
  @Override
  public void onRefreshBegin(PullToRefreshLayout layout) {
      Log.i("MessageListActivity", "Loading next page");
      loadNextPage();
      // After load history messages, call refreshComplete.
      ptrLayout.refreshComplete();
  }
});
```

The `PtrDefaultHeader` is Material style, you can use any other style you like, just take a look at [android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) to implement your header.

We have define many kinds of attributes, to support user to adjust their layout, you can see
[attrs.xml](./../../Android/messagelist/src/main/res/values/attrs.xml) in detail, and we support totally customize style either, please look down.

#### MessageList Custom Config

Almost all attributes not only can be set in XML file but also can be set in code. Here is a example showing how to set show display name or not.

```Java
MessageList messageList = (MessageList) findViewById(R.id.msg_list);
```

- To show receiver or sender 's display name，you can set `showReceiverDisplayName` and  `showSenderDisplayName` to true or false in XML file above, or you can also set in code like：

  ```Java
  messageList.setShowSenderDisplayName(true);
  messageList.setShowReceiverDisplayName(true);
  ```

- Forbid pull to refresh（Added since 0.4.8），call `messageList.forbidScrollToRefresh(true)`, then `onLoadMore` would not trigger.

    ```Java
    messageList.forbidScrollToRefresh(true);
    ```

### 2. Construct adapter
Adapter's constructor has three parameters. The first one is `sender id`, the id of sender, the second one is `HoldersConfig object`,
you can use this object to [construct your custom ViewHolder and layout](./custom_layout.md), the third one is implement of `ImageLoader`,
use to display user's avatar, if this value is null, will not display avatar.([Click here to know more about ImageLoader](./image_loader.md))

```java
MsgListAdapter adapter = new MsgListAdapter<MyMessage>("0", holdersConfig, imageLoader);
messageList.setAdapter(adapter);
```

### 3. Construct model
To be add messages, you need to implement IMessage, IUser interface into your existing model and override it's methods:

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
MessageType above is an enum class in IMessage class, you need implement IUser interface, too:

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
That's all! Now you can use your own message model to fill into adapter without type converting of any kind!

## Data management

### Add new messages
To add new message in message list is pretty easy, we support two ways to add new messages:

- Add new message in the bottom of message list： `addToStart(IMESSAGE message, boolean scroll)`

```java
// add a new message in the bottom of message list, the second parameter implys whether to scroll to bottom.
adapter.addToStart(message, true);
```

- Add messages to the top of message list, the parameter list is sorted chronologically: `addToEndChronologically`(**Add since 0.7.2**)

```java
// Messages to be add are sorted chronologically(The last message is the latest)
adapter.addToEndChronologically(messages);
```

- Add messages to the top of message list（Usually use this method to load last page of history messages）: `addToEnd(List<IMessage> messages)`

```java
// Add messages to the top of message list, messages are in descending order(The first message is the latest)
adapter.addToEnd(messages);
```

- Scroll to load history messages(**Attention: If you add `PullToRefreshLayout`, pass this part.**)
  After adding this listener: `OnLoadMoreListener`，when scroll to top will fire `onLoadMore` event，for example：
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

### Delete message
Here are methods to delete message：

- *adapter.deleteById(String id)*: according message id to delete
- *adapter.deleteByIds(String[] ids)*: according message ids' array to delete
- *adapter.delete(IMessage message)*: according message object to delete
- *adapter.delete(List<IMessage> messages)*: according message objects' list to delete
- *adapter.clear()*: delete all messages

### Update message
If message updated, you can invoke these methods to notify adapter to update message:

- *adapter.update(IMessage message)*: message to be updated
- *adapter.update(String oldId, IMessage newMessage)*


## Event handling
- `OnMsgClickListener` fires when click message.
```java
mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
    @Override
    public void onMessageClick(MyMessage message) {
        // do something
    }
});
```

- `OnAvatarClickListener` fires when click avatar.
```java
mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
    @Override
    public void onAvatarClick(MyMessage message) {
        DefaultUser userInfo = (DefaultUser) message.getUserInfo();
        // Do something
    }
});
```

- `OnMsgLongClickListener` fires when long click message.(Add View parameter since 0.6.4)
```java
mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
    /**
     *@param view The view been long clicked.
     */
    @Override
    public void onMessageLongClick(View view, MyMessage message){
        // do something
    }
});
```

- `OnMsgStatusViewClickListener` fires when click message status view button.(previous name was OnMsgResendListener, modified after 0.4.7)
```java
mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
    @Override
    public void onMessageResend(MyMessage message) {
       //message status view click, resend or download here
    }
 });
```



### Progurad

Add the proguard-rule below if you need to obfuscate your code:

```
-keep class cn.jiguang.imui.** { *; }
```

