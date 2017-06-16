# Customize widget style
[中文文档](./customLayout.md)

If you want to totally customize your widget, you can define custom ViewHolder and layout.
This document will show you how to do it.

## Step
Customize your widget style only need three steps.

### Step one: create custom message item layout
Create xml layout file, you can refer MessageList's message item layout.


### Step two:Create ViewHolder extends BaseMessageViewHolder and implements DefaultMessageViewHolder
The custom ViewHolder's constructor **must** have two types of parameter: View type and boolean type. For example:


```java
public class TxtViewHolder<MESSAGE extends IMessage>
        extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    // Notice here
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

### Step three: Use HoldersConfig object to set custom ViewHolder and layout
Remember there several kinds of message: text, photo, voice, video, so you need in place all of them,
otherwise, will use default style.

```java
MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
// First parameter is custom ViewHolder class，second one is resource id of custom layout.
holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
holdersConfig.setSendPhotoMsg(CustomViewHolder.class, layoutRes);
...
```

That's all! Please try it by yourself!



### Add custom message Interface

Add `SEND_CUSTOM`  and `RECEIVE_CUSTOM` message type. You can use `HoldersConfig` set these kinds of message's ViewHolder and layout：

```
MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
holdersConfig.setSendCustomMsg(CustomMessageViewHolder.class, layoutRes);
holdersConfig.setReceiveCustomMsg(CustomMessageViewHolder.class, layoutRes);
```
