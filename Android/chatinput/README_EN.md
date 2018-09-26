## ChatInput

[中文文档](./README.md)

This is a input component in chatting interface, can combine Aurora IMUI conveniently. Including
features like record voice and video, select photo, take picture etc, supports customize style either.


## Download
Provides several ways to add dependency, you can choose one of them:

- Via Gradle
```groovy

compile 'cn.jiguang.imui:chatinput:0.9.1'

```

- Via Maven

```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>chatinput</artifactId>
  <version>0.9.1</version>
  <type>pom</type>
</dependency>
```

- Via JitPack
> project's build.gradle

```groov
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

> module's build.gradle

```groovy
dependencies {
  compile 'com.github.jpush:imui:0.9.1'
}
```

## Usage
Using ChatInputView only need two steps.

#### Step one: add `ChatInputView` in xml layout

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

#### Step two: init `ChatInputView`

```java
ChatInputView chatInputView = (ChatInputView) findViewById(R.id.chat_input);
chatInputView.setMenuContainerHeight(softInputHeight);
```

**Attention please, for perfect display, MUST set MenuContainer's height after init ChatInputView. If using RN, set `ChatInput`'s  `MenuContainerHeight` property instead.** 

Best suggestion: get soft keyboard height from other activity(Like login Activity, just before chat Activity), then set soft keyboard height via:

```java
ChatInputView chatinput = (ChatInputView) findViewById(R.id.chat_input);
chatinput.setMenuContainerHeight(softKeyboardHeight);
```

As for how to get soft keyboard height, you can listen `onSizeChanged` method.


## Import interface and event
ChatInputView offers all kinds of click listener of button and event's callback, so that user can use
event listener to do their stuff flexibly. Such as send message event etc.

#### OnMenuClickListener
First of all, `OnMenuClickListener` handling click event of menu item. Call `chatInputView.setMenuClickListener`
can set this listener:
```java
chatInput.setMenuClickListener(new OnMenuClickListener() {
    @Override
    public boolean onSendTextMessage(CharSequence input) {
         // After input content and click send button will fire this callback
    }

    @Override
    public void onSendFiles(List<String> list) {
        // chose photo or video files or finished recording video,
        // then click send button fires this event.
    }

    @Override
    public boolean switchToMicrophoneMode() {
        // click mic button in menu item, fires before showing record voice widget
      // return true will use default interface, otherwise you should return false and show your interface
      return true;
    }

    @Override
    public boolean switchToGalleryMode() {
        // click photo button in menu item, fires before showing select photo widget
      // return true will use default interface, otherwise you should return false and show your interface
      return true;
    }

    @Override
    public boolean switchToCameraMode() {
        // click camera button in menu item, fires before showing camera widget
      // return true will use default interface, otherwise you should return false and show your interface
      return true;
    }
});
```

As for how to handle these events and what to do with these events, you can refer sample project for detail.



### OnClickEditTextListener

Callback of click EditText，fires when click EidtText, usage：

```
mChatInput.setOnClickEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
                mAdapter.getLayoutManager().scrollToPosition(0);
            }
        });
```
#### RecordVoiceListener
This is the interface of record voice, the way to use:

```java
mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
mRecordVoiceBtn.setRecordVoiceListener(new RecordVoiceListener() {
    @Override
    public void onStartRecord() {
        // Show record voice interface
        // set directory to save audio files
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
      * In preview record voice layout, fires when click cancel button
      * Add since 0.7.3
      */
    @Override
    public void onPreviewCancel() {
    }

    
    /**
     * In preview record voice layout, fires when click send button
     * Add since chatinput 0.7.3
     */
    @Override
    public void onPreviewSend() {
		
    }
});
```

#### OnCameraCallbackListener
This is interface related to camera, usage like：
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
        // Fires when finished recording video.
        // Pay attention here, when you finished recording video and click send
        // button in screen, will fire onSendFiles() method.
    }

    @Override
    public void onCancelVideoRecord() {

    }
});
```



#### CameraControllerListener

Control camera interface, include full screen event, switch take picture/ record video event, close camera event, etc.

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
                // Judge is take picture mode or record video mode by isRecordVideoMode.
        });
```


#### Set file path and file name that after taken picture(Deprecated since 0.4.5)

setCameraCaptureFile(String path, String fileName)

Since 0.4.5, take picture will return default path.

```java
// The first parameter is file path that saved at, second one is file name
// Suggest calling this method when onCameraClick fires
// Deprecated since 0.4.5
mChatInput.setCameraCaptureFile(path, fileName);
```

## MenuManager(Support since 0.9.0)

Menu management class, used to add a custom menu, freely set the position of the menu item, including the left/right/lower position of the chatinput.

### Get MenuManager

`MenuManager menuManager = mChatInput.getMenuManager();`


### Add custom menu

1. Add custom menu item layout，the root node has to be MenuItem(Extends LinearLayout)
```xml
<cn.jiguang.imui.chatinput.menu.view.MenuItem 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    ...

</cn.jiguang.imui.chatinput.menu.view.MenuItem>
```

2. Add custom menu feature layout，the root node has to be MenuFeature(Extends LinearLayout)
```xml
<cn.jiguang.imui.chatinput.menu.view.MenuFeature 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    ...

</cn.jiguang.imui.chatinput.menu.view.MenuFeature>
```
3. Add custom menu by MenuManager
```java
//First：add view mode, tag must be unique
//If set menuFeature as null means that this menu item does not require a corresponding function.
addCustomMenu(String tag, MenuItem menuItem, MenuFeature menuFeature)

//Second: add layout resources, tag must be unique
//If set menuFeatureResource as -1 means that this menu item does not require a corresponding function.
addCustomMenu(String tag, int menuItemResource, int menuFeatureResource) 
```
4. CustomMenuEventListener
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
### Set the position of the menu item
`setMenu(Menu menu)`

#### Menu

The position is controlled by passing in the tag of the menu item. The default layout tags are:

```Java
Menu.TAG_VOICE 
Menu.TAG_EMOJI
Menu.TAG_GALLERY
Menu.TAG_CAMERA
Menu.TAG_SEND
```
Set position,tag cannot be repeated：

```java
Menu.newBuilder().
        customize(boolean customize).// Whether to customize the position
        setLeft(String ... tag).// Set left menu items
        setRight(String ... tag).// Set right menu items
        setBottom(String ... tag).//Set bottom menu items
        build()
```
#### Sample
```java
menuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE,Menu.TAG_EMOJI,Menu.TAG_GALLERY,Menu.TAG_CAMERA).
                build());
```


