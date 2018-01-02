package cn.jiguang.imui.chatinput.emoji.data;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.jiguang.imui.chatinput.emoji.widget.EmoticonPageView;


public class EmoticonPageEntity<T> extends PageEntity<EmoticonPageEntity> {

    public enum DelBtnStatus {
        // 0,1,2
        GONE, FOLLOW, LAST;

        public boolean isShow() {
            return ! GONE.toString().equals(this.toString());
        }
    }

    /**
     * 表情数据源
     */
    private List<T> mEmoticonList;
    /**
     * 每页行数
     */
    private int mLine;
    /**
     * 每页列数
     */
    private int mRow;
    /**
     * 删除按钮
     */
    private DelBtnStatus mDelBtnStatus;

    public List<T> getEmoticonList() {
        return mEmoticonList;
    }

    public void setEmoticonList(List<T> emoticonList) {
        this.mEmoticonList = emoticonList;
    }

    public int getLine() {
        return mLine;
    }

    public void setLine(int line) {
        this.mLine = line;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }

    public DelBtnStatus getDelBtnStatus() {
        return mDelBtnStatus;
    }

    public void setDelBtnStatus(DelBtnStatus delBtnStatus) {
        this.mDelBtnStatus = delBtnStatus;
    }

    public EmoticonPageEntity() { }

    @Override
    public View instantiateItem(final ViewGroup container, int position, EmoticonPageEntity pageEntity) {
        if(mPageViewInstantiateListener != null){
            return mPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        if (getRootView() == null) {
            EmoticonPageView pageView = new EmoticonPageView(container.getContext());
            pageView.setNumColumns(mRow);
            setRootView(pageView);
        }
        return getRootView();
    }
}