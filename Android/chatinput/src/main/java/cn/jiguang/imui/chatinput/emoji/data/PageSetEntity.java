package cn.jiguang.imui.chatinput.emoji.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class PageSetEntity<T extends PageEntity> implements Serializable {

    protected final String uuid = UUID.randomUUID().toString();
    protected final int mPageCount;
    protected final boolean mIsShowIndicator;
    protected final LinkedList<T> mPageEntityList;
    protected final String mIconUri;
    protected final String mSetName;

    public PageSetEntity(final Builder builder) {
        this.mPageCount = builder.pageCount;
        this.mIsShowIndicator = builder.isShowIndicator;
        this.mPageEntityList = builder.pageEntityList;
        this.mIconUri = builder.iconUri;
        this.mSetName = builder.setName;
    }

    public String getIconUri() {
        return mIconUri;
    }

    public int getPageCount() {
        return mPageEntityList == null ? 0 : mPageEntityList.size();
    }

    public LinkedList<T> getPageEntityList() {
        return mPageEntityList;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isShowIndicator() {
        return mIsShowIndicator;
    }

    public static class Builder<T extends PageEntity> {

        protected int pageCount;
        protected boolean isShowIndicator = true;
        protected LinkedList<T> pageEntityList = new LinkedList<>();
        protected String iconUri;
        protected String setName;

        public Builder setPageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder setShowIndicator(boolean showIndicator) {
            isShowIndicator = showIndicator;
            return this;
        }

        public Builder setPageEntityList(LinkedList<T> pageEntityList) {
            this.pageEntityList = pageEntityList;
            return this;
        }

        public Builder addPageEntity(T pageEntityt) {
            pageEntityList.add(pageEntityt);
            return this;
        }

        public Builder setIconUri(String iconUri) {
            this.iconUri = iconUri;
            return this;
        }

        public Builder setIconUri(int iconUri) {
            this.iconUri = "" + iconUri;
            return this;
        }

        public Builder setSetName(String setName) {
            this.setName = setName;
            return this;
        }

        public Builder() {
        }

        public PageSetEntity<T> build() {
            return new PageSetEntity<>(this);
        }
    }
}