package cn.jiguang.imui.chatinput.emoji.data;

import java.util.ArrayList;

import cn.jiguang.imui.chatinput.emoji.listener.PageViewInstantiateListener;

public class EmoticonPageSetEntity<T> extends PageSetEntity<EmoticonPageEntity> {

    final int mLine;
    final int mRow;
    final EmoticonPageEntity.DelBtnStatus mDelBtnStatus;
    final ArrayList<T> mEmoticonList;

    public EmoticonPageSetEntity(final Builder builder) {
        super(builder);
        this.mLine = builder.line;
        this.mRow = builder.row;
        this.mDelBtnStatus = builder.delBtnStatus;
        this.mEmoticonList = builder.emoticonList;
    }

    public int getLine() {
        return mLine;
    }

    public int getRow() {
        return mRow;
    }

    public EmoticonPageEntity.DelBtnStatus getDelBtnStatus() {
        return mDelBtnStatus;
    }

    public ArrayList<T> getEmoticonList() {
        return mEmoticonList;
    }

    public static class Builder<T> extends PageSetEntity.Builder {

        /**
         * 每页行数
         */
        protected int line;
        /**
         * 每页列数
         */
        protected int row;
        /**
         * 删除按钮
         */
        protected EmoticonPageEntity.DelBtnStatus delBtnStatus = EmoticonPageEntity.DelBtnStatus.GONE;
        /**
         * 表情集数据源
         */
        protected ArrayList<T> emoticonList;

        protected PageViewInstantiateListener pageViewInstantiateListener;

        public Builder() {
        }

        public Builder setLine(int line) {
            this.line = line;
            return this;
        }

        public Builder setRow(int row) {
            this.row = row;
            return this;
        }

        public Builder setShowDelBtn(EmoticonPageEntity.DelBtnStatus showDelBtn) {
            delBtnStatus = showDelBtn;
            return this;
        }

        public Builder setEmoticonList(ArrayList<T> emoticonList) {
            this.emoticonList = emoticonList;
            return this;
        }

        public Builder setIPageViewInstantiateItem(PageViewInstantiateListener pageViewInstantiateListener) {
            this.pageViewInstantiateListener = pageViewInstantiateListener;
            return this;
        }

        public Builder setShowIndicator(boolean showIndicator) {
            isShowIndicator = showIndicator;
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

        public EmoticonPageSetEntity<T> build() {
            int emoticonSetSum = emoticonList.size();
            int del = delBtnStatus.isShow() ? 1 : 0;
            int everyPageMaxSum = row * line - del;
            pageCount = (int) Math.ceil((double) emoticonList.size() / everyPageMaxSum);

            int start = 0;
            int end = everyPageMaxSum > emoticonSetSum ? emoticonSetSum : everyPageMaxSum;

            if (!pageEntityList.isEmpty()) {
                pageEntityList.clear();
            }

            for (int i = 0; i < pageCount; i++) {
                EmoticonPageEntity emoticonPageEntity = new EmoticonPageEntity();
                emoticonPageEntity.setLine(line);
                emoticonPageEntity.setRow(row);
                emoticonPageEntity.setDelBtnStatus(delBtnStatus);
                emoticonPageEntity.setEmoticonList(emoticonList.subList(start, end));
                emoticonPageEntity.setIPageViewInstantiateItem(pageViewInstantiateListener);
                pageEntityList.add(emoticonPageEntity);

                start = everyPageMaxSum + i * everyPageMaxSum;
                end = everyPageMaxSum + (i + 1) * everyPageMaxSum;
                if (end >= emoticonSetSum) {
                    end = emoticonSetSum;
                }
            }
            return new EmoticonPageSetEntity<>(this);
        }
    }
}
