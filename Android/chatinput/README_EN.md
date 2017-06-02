## ChatInput

[中文文档](./README.md)

This is a input component in chatting interface, can combine Aurora IMUI conveniently. Including
features like record voice and video, select photo, take picture etc, supports customize style either.


## Download
Provides several ways to add dependency, you can choose one of them:

- Via Gradle
```groovy
compile 'cn.jiguang.imui:chatinput:0.2.0'
```

- Via Maven

```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>chatinput</artifactId>
  <version>0.2.0</version>
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
  compile 'com.github.jpush:imui:0.2.0'
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

Attention please, **MUST** set MenuContainer's height after init ChatInputView. Best suggestion: get
soft keyboard height from other activity(Like login Activity), then set soft keyboard height via:
```java
ChatInputView chatinput = (ChatInputView) findViewById(R.id.chat_input);
chatinput.setMenuContainerHeight(softKeyboardHeight);
```

As for how to get soft keyboard height, you can listen `onSizeChanged` method.
Please [refer onSizeChanged in sample's MessageListActivity](./../sample/exampleui/src/main/java/imui/jiguang/cn/imuisample/messages/MessageListActivity.java#L340),
and [onSizedChanged in sample's ChatView](./../sample/exampleui/src/main/java/imui/jiguang/cn/imuisample/views/ChatView.java#L102).


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
    public void switchToMicrophoneMode() {
        // click mic button in menu item, fires before showing record voice widget
    }

    @Override
    public void switchToGalleryMode() {
        // click photo button in menu item, fires before showing select photo widget
    }

    @Override
    public void switchToCameraMode() {
        // click camera button in menu item, fires before showing camera widget
    }
});
```

As for how to handle these events and what to do with these events, you can refer sample project for detail.

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

#### Set file path and file name that after taken picture
setCameraCaptureFile(String path, String fileName)

```java
// The first parameter is file path that saved at, second one is file name
// Suggest calling this method when onCameraClick fires
mChatInput.setCameraCaptureFile(path, fileName);
```
