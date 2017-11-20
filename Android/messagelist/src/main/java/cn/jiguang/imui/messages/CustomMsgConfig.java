package cn.jiguang.imui.messages;


import cn.jiguang.imui.commons.models.IMessage;

/**
 * This class specify custom message's view type, layout resource id, is send outgoing or not
 * and custom message's {@link Class}
 */
public class CustomMsgConfig {

    // View Type should not use 0-12, because we have already used.
    private int mViewType;
    private int mResId;
    private boolean mIsSender;
    private Class<? extends BaseMessageViewHolder<? extends IMessage>> mClass;

    public CustomMsgConfig(int type, int id, boolean isSender, Class<? extends BaseMessageViewHolder<? extends IMessage>> clazz) {
        this.mViewType = type;
        this.mResId = id;
        this.mIsSender = isSender;
        this.mClass = clazz;
    }

    public void setViewType(int type) {
        this.mViewType = type;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setResourceId(int id) {
        this.mResId = id;
    }

    public int getResourceId() {
        return mResId;
    }

    public void setIsSender(boolean isSender) {
        this.mIsSender = isSender;
    }

    public boolean getIsSender() {
        return mIsSender;
    }

    public void setClazz(Class<? extends BaseMessageViewHolder<? extends IMessage>> clazz) {
        this.mClass = clazz;
    }

    public Class<? extends BaseMessageViewHolder<? extends IMessage>> getClazz() {
        return mClass;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int mViewType;
        private int mResId;
        private boolean mIsSender;
        private Class<? extends BaseMessageViewHolder<? extends IMessage>> mClass;

        public Builder setViewType(int type) {
            this.mViewType = type;
            return this;
        }

        public Builder setResourceId(int id) {
            this.mResId = id;
            return this;
        }

        public Builder setIsSender(boolean isSender) {
            this.mIsSender = isSender;
            return this;
        }

        public Builder setClass(Class<? extends BaseMessageViewHolder<? extends IMessage>> clazz) {
            this.mClass = clazz;
            return this;
        }

        public CustomMsgConfig build() {
            return new CustomMsgConfig(mViewType, mResId, mIsSender, mClass);
        }
    }
}
