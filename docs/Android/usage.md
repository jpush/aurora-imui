# MessageList
[English Document](./usageEn.md)

MessageList 是聊天界面的消息列表，用来展示各种类型的消息，可以支持丰富的自定义扩展。如果不使用自定义将会使用默认样式。

## 安装

提供了以下几种方式添加依赖，只需要选择其中一种即可。

- Gradle 方式

```
compile 'cn.jiguang.imui:imui:0.0.1'
```

- 使用 Maven：
```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>imui</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```

- 使用 JitPack

    在项目的 build.gradle 中加入：

   ```
allprojects {
        repositories {
           ...
           maven { url 'https://jitpack.io' }
        }
}
```

    在 Module 的 build.gradle 中加入：
    ```
    dependencies {
        compile 'com.github.jpush:imui:0.0.1'
    }
    ```

## 使用
使用 MessageList 只需要几个简单的步骤，你也可以参考一下我们的 [sample](./../sample)。

#### 第一步：在布局文件中引用 MessageList：
```
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
我们定义了很多样式，供用户调整布局，详细的属性可以参考 attr 文件。当然我们也支持完全自定义布局，下面会说到。

#### 第二步：构造 adapter
Adapter 的构造函数有三个参数。第一个是 sender id，发送方的 id，第二个参数是 HoldersConfig 对象，你可以用这个对象来
[构造自定义的消息的 ViewHolder 及布局界面](./customLayout.md)。第三个参数是 ImageLoader 的实现，用来展示头像，如果为空，将会隐藏头像。
（[点击了解更多关于 ImageLoader 的内容](./imageLoader.md)）
```
MsgListAdapter adapter = new MsgListAdapter<MyMessage>("0", holdersConfig, imageLoader);
messageList.setAdapter(adapter);
```

#### 第三步：构造你的实体类型
包括消息、用户（以及以后的会话），你必须实现我们的 IMessage、IUser 接口类，例如：
```
public class MyMessage implements IMessage {

    private long id;
    private String text;
    private Date createAt;
    private MessageType type;
    private IUser user;
    private String contentFile;
    private int duration;

    public MyMessage(String text, MessageType type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getUserInfo() {
        if (user == null) {
            return new DefaultUser("0", "user1", null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public void setContentFile(String path) {
        this.contentFile = path;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public Date getCreatedAt() {
        return createAt == null ? new Date() : createAt;
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
    public String getContentFile() {
        return contentFile;
    }
}
```
上面的 MessageType 是 IMessage 中的枚举类。同样 IUser 接口也需要实现：
```
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
    public String getAvatar() {
        return avatar;
    }
}
```
以上就完成了所有的准备工作。

## 数据管理

#### 增加新的消息
在消息列表中插入消息非常简单，这里提供了两种方式：

- 在消息列表底部插入最新消息： addToStart(IMESSAGE message, boolean scroll)

```
// 在底部插入一条消息，第二个参数表示是否滚动到底部。
adapter.addToStart(message, true);
```

- 在消息列表的末尾插入消息（通常用来加载上一页消息）: addToEnd(List<IMessage> messages, boolean reverse)

```
// 在消息列表的顶部加入消息，第二个参数表示是否在插入前是否反转列表
adapter.addToEnd(messages, true);
```

- 滚动列表加载历史消息
设置监听 OnLoadMoreListener，当滚动列表时就会触发 onLoadMore 事件，例如：
```
mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
    @Override
    public void onLoadMore(int page, int totalCount) {
        if (totalCount < mData.size()) {
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     mAdapter.addToEnd(mData, true);
                 }
             }, 1000);
        }
    }
});
```

#### 删除消息
有如下删除消息的接口：

- adapter.deleteById(String id) 根据 message id 来删除消息
- adapter.deleteByIds(String[] ids) 根据 message id 批量删除
- adapter.delete(IMessage message) 根据消息对象删除
- adapter.delete(List<IMessage> messages) 根据消息对象列表批量删除
- adapter.clear() 删除所有消息

#### 更新消息
如果消息改变了，可以调用以下接口来更新消息：

- adapter.update(IMessage message)
- adapter.update(String oldId, IMessage newMessage)


## 事件监听
- OnMsgClickListener 点击消息的时候触发

```
mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
    @Override
    public void onMessageClick(MyMessage message) {
        // do something
    }
});
```

- OnAvatarClickListener 点击头像的时候触发

```
mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
    @Override
    public void onAvatarClick(MyMessage message) {
        DefaultUser userInfo = (DefaultUser) message.getUserInfo();
        // Do something
    }
});
```

- OnMsgLongClickListener 长按消息触发

```
mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
    @Override
    public void onMessageLongClick(MyMessage message) {
        // do something
    }
});
```

## Contribute
Please contribute! [Look at the issues](https://github.com/jpush/imui/issues).

## License
MIT © [JiGuang](/LICENSE)
