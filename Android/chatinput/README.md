## ChatInput

[English Document](./README_EN.md)

这是一个聊天界面输入框组件，可以方便地结合 `MessageList` 使用，包含录音，选择图片，拍照等功能，提供了一些丰富的接口和回调供用户使用，
还可以选择自定义样式。

> 该组件依赖了 Glide 3.8.0

## 集成
提供了以下几种方式添加依赖，只需要选择其中一种即可。

- Gradle
```groovy
compile 'cn.jiguang.imui:chatinput:0.9.1'
```

- Maven
```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>chatinput</artifactId>
  <version>0.9.1</version>
  <type>pom</type>
</dependency>
```

- JitPack

  > project 下的 build.gradle

  ```groovy
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  ```

  > module 的 build.gradle

  ```groovy
  dependencies {
    compile 'com.github.jpush:imui:0.9.1'
  }
  ```

## 用法
使用 ChatInput 只需要两个步骤：

1. 在 xml 布局文件中引用 ChatInputView：
```xml
    <cn.jiguang.imui.chatinput.ChatInputView
        android:id="@+id/chat_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:cameraBtnIcon="@drawable/aurora_menuitem_camera"
        app:inputCursorDrawable="@drawable/aurora_edittext_cursor_bg"
        app:inputEditTextBg="@drawable/aurora_edittext_bg"
        app:inputHint="@string/chat_input_hint"
        app:photoBtnIcon="@drawable/aurora_menuitem_photo"
        app:sendBtnIcon="@drawable/aurora_menuitem_send"
        app:voiceBtnIcon="@drawable/aurora_menuitem_mic" />
```

2. 初始化 ChatInputView
```java
ChatInputView chatInputView = (ChatInputView) findViewById(R.id.chat_input);
chatInputView.setMenuContainerHeight(softInputHeight);
```

  **初始化后一定要设置一下 MenuContainer 的高度，最好设置为软键盘的高度，否则会导致第一次打开菜单时高度不正常（此时打开软键盘会导致界面伸缩）。 如果使用 RN，设置 `ChatInput` 的 `menuContainerHeight` 属性即可。 **

  建议在跳转到聊天界面之前使用 onSizeChanged 方法监听软键盘的高度，然后在初始化的时候设置即可。

## 重要接口及事件
ChatInputView 提供了各种按钮及事件的监听回调，所以用户可以灵活地运用监听事件做一些操作，如发送消息等事件。

### OnMenuClickListener
首先是输入框下面的菜单栏事件的监听，调用 `chatInputView.setMenuClickListener` 即可设置监听：
```java
chatInput.setMenuClickListener(new OnMenuClickListener() {
    @Override
    public boolean onSendTextMessage(CharSequence input) {
         // 输入框输入文字后，点击发送按钮事件
    }

    @Override
    public void onSendFiles(List<String> list) {
        // 选中文件或者录制完视频后，点击发送按钮触发此事件
    }

    @Override
    public boolean switchToMicrophoneMode() {
        // 点击语音按钮触发事件，显示录音界面前触发此事件
        // 返回 true 表示使用默认的界面，若返回 false 应该自己实现界面
        return true;
    }

    @Override
    public boolean switchToGalleryMode() {
        // 点击图片按钮触发事件，显示图片选择界面前触发此事件
        // 返回 true 表示使用默认的界面
        return true;
    }

    @Override
    public boolean switchToCameraMode() {
        // 点击拍照按钮触发事件，显示拍照界面前触发此事件
        // 返回 true 表示使用默认的界面
        return true;
    }
});
```
关于上述事件的处理，可以参考 sample 中的 MessageListActivity 对于事件的处理。



### OnClickEditTextListener

监听输入框点击事件，点击输入框后触发此事件。用法：

```
mChatInput.setOnClickEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
                mAdapter.getLayoutManager().scrollToPosition(0);
            }
        });
```




### RecordVoiceListener
这是录音的接口，使用方式：

```java
mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
mRecordVoiceBtn.setRecordVoiceListener(new RecordVoiceListener() {
    @Override
    public void onStartRecord() {
        // Show record voice interface
        // 设置存放录音文件目录
        File rootDir = mContext.getFilesDir();
        String fileDir = rootDir.getAbsolutePath() + "/voice";
        mRecordVoiceBtn.setRecordVoiceFile(fileDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + "");
    }

    @Override
    public void onFinishRecord(File voiceFile, int duration) {
        MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
        message.setMediaFilePath(voiceFile.getPath());
        message.setDuration(duration);
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        mAdapter.addToStart(message, true);
    }

    @Override
    public void onCancelRecord() {

    }
    
    /**
     * 录音试听界面，点击取消按钮触发
     * 0.7.3 后添加此事件
     */
    @Override
    public void onPreviewCancel() {
        
    }

    /**
     * 录音试听界面，点击发送按钮触发
     * 0.7.3 后增加此事件
     */
    @Override
    public void onPreviewSend() {

    }
});
```

### OnCameraCallbackListener
这是相机相关的接口，使用方式：
```java
mChatInput.setOnCameraCallbackListener(new OnCameraCallbackListener() {
    @Override
    public void onTakePictureCompleted(String photoPath) {
        MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        message.setMediaFilePath(photoPath);
        message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
        MessageListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToStart(message, true);
            }
        });
    }

    @Override
    public void onStartVideoRecord() {

    }

    @Override
    public void onFinishVideoRecord(String videoPath) {
        // 请注意，点击发送视频的事件会回调给 onSendFiles，这个是在录制完视频后触发的                               
    }

    @Override
    public void onCancelVideoRecord() {

    }
    
});
```



#### CameraControllerListener

相机控制相关接口，包括全屏、拍照与录制视频切换、关闭相机等事件。

```
mChatInput.setCameraControllerListener(new CameraControllerListener() {
            @Override
            public void onFullScreenClick() {
               
            }

            @Override
            public void onRecoverScreenClick() {
               
            }

            @Override
            public void onCloseCameraClick() {
                
            }

            @Override
            public void onSwitchCameraModeClick(boolean isRecordVideoMode) {
                // 切换拍照与否，通过 isRecordVideoMode 判断
        });
```





### 设置拍照后保存的文件(0.4.5 版本后弃用)

`setCameraCaptureFile(String path, String fileName)`

0.4.5 版本后拍照会返回默认路径。

```java
// 参数分别是路径及文件名，建议在上面的 onCameraClick 触发时调用
// 0.4.5 后弃用
mChatInput.setCameraCaptureFile(path, fileName);
```

## MenuManager(从0.9.0版本开始支持)
菜单功能管理类，用于添加自定义菜单，自由设置菜单项的位置，包括输入框的左边/右边/下边位置。

### 获取MenuManager

`MenuManager menuManager = mChatInput.getMenuManager();`


### 添加自定义菜单

1. 添加菜单项布局，根节点为MenuItem（继承自LinearLayout）
```xml
<cn.jiguang.imui.chatinput.menu.view.MenuItem 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    ...

</cn.jiguang.imui.chatinput.menu.view.MenuItem>
```

2. 添加对应菜单功能布局，根节点为MenuFeature（继承自LinearLayout）
```xml
<cn.jiguang.imui.chatinput.menu.view.MenuFeature 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    ...

</cn.jiguang.imui.chatinput.menu.view.MenuFeature>
```
3. 通过MenuManager添加自定义菜单，
```java
//第一种：添加view方式，tag唯一
//menuFeature 传 null 即表示此菜单项不需要对应功能
addCustomMenu(String tag, MenuItem menuItem, MenuFeature menuFeature)

//第二种：添加布局资源，tag唯一
//menuFeatureResource 传 -1 即表示此菜单项不需要对应功能
addCustomMenu(String tag, int menuItemResource, int menuFeatureResource) 
```
4. 设置自定义菜单事件监听，CustomMenuEventListener
```java
menuManager.setCustomMenuClickListener(new CustomMenuEventListener() {
            @Override
            public boolean onMenuItemClick(String tag, MenuItem menuItem) {
                //Menu feature will not be shown if return false；
                return true;
            }

            @Override
            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {
                if(visibility == View.VISIBLE){
                    // Menu feature is visible.
                }else {
                    // Menu feature is gone.
                }
            }
        });
```
### 设置菜单项的位置
`setMenu(Menu menu)`

#### Menu
通过传入菜单项的tag来控制布局,默认可使用的布局tag有：
```Java
Menu.TAG_VOICE 
Menu.TAG_EMOJI
Menu.TAG_GALLERY
Menu.TAG_CAMERA
Menu.TAG_SEND
```
设置位置,tag不能重复：

```java
Menu.newBuilder().
        customize(boolean customize).//是否自定义位置
        setLeft(String ... tag).//输入框左侧菜单项
        setRight(String ... tag).//输入框右侧菜单项
        setBottom(String ... tag).//输入框下侧菜单项
        build()
```
#### 示例
```java
menuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE,Menu.TAG_EMOJI,Menu.TAG_GALLERY,Menu.TAG_CAMERA).
                build());
```








