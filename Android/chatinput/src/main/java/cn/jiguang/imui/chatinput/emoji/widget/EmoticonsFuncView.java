package cn.jiguang.imui.chatinput.emoji.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import cn.jiguang.imui.chatinput.emoji.adapter.PageSetAdapter;
import cn.jiguang.imui.chatinput.emoji.data.PageSetEntity;


/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class EmoticonsFuncView extends ViewPager {

    protected PageSetAdapter mPageSetAdapter;
    protected int mCurrentPagePosition;

    public EmoticonsFuncView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(PageSetAdapter adapter) {
        super.setAdapter(adapter);
        this.mPageSetAdapter = adapter;

        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                checkPageChange(position);
                mCurrentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mOnEmoticonsPageViewListener == null
                || mPageSetAdapter.getPageSetEntityList().isEmpty()) {
            return;
        }
        PageSetEntity pageSetEntity = mPageSetAdapter.getPageSetEntityList().get(0);
        mOnEmoticonsPageViewListener.playTo(0, pageSetEntity);
        mOnEmoticonsPageViewListener.emoticonSetChanged(pageSetEntity);
    }

    public void setCurrentPageSet(PageSetEntity pageSetEntity) {
        if (mPageSetAdapter == null || mPageSetAdapter.getCount() <= 0) {
            return;
        }
        setCurrentItem(mPageSetAdapter.getPageSetStartPosition(pageSetEntity));
    }

    public void checkPageChange(int position) {
        if (mPageSetAdapter == null) {
            return;
        }
        int end = 0;
        for (PageSetEntity pageSetEntity : mPageSetAdapter.getPageSetEntityList()) {

            int size = pageSetEntity.getPageCount();

            if (end + size > position) {

                boolean isEmoticonSetChanged = true;
                // 上一表情集
                if (mCurrentPagePosition - end >= size) {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playTo(position - end, pageSetEntity);
                    }
                }
                // 下一表情集
                else if (mCurrentPagePosition - end < 0) {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playTo(0, pageSetEntity);
                    }
                }
                // 当前表情集
                else {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playBy(mCurrentPagePosition - end, position - end, pageSetEntity);
                    }
                    isEmoticonSetChanged = false;
                }

                if (isEmoticonSetChanged && mOnEmoticonsPageViewListener != null) {
                    mOnEmoticonsPageViewListener.emoticonSetChanged(pageSetEntity);
                }
                return;
            }
            end += size;
        }
    }

    private OnEmoticonsPageViewListener mOnEmoticonsPageViewListener;

    public void setOnIndicatorListener(OnEmoticonsPageViewListener listener) {
        mOnEmoticonsPageViewListener = listener;
    }

    public interface OnEmoticonsPageViewListener {

        void emoticonSetChanged(PageSetEntity pageSetEntity);

        /**
         * @param position 相对于当前表情集的位置
         */
        void playTo(int position, PageSetEntity pageSetEntity);

        /**
         * @param oldPosition 相对于当前表情集的始点位置
         * @param newPosition 相对于当前表情集的终点位置
         */
        void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity);
    }
}
