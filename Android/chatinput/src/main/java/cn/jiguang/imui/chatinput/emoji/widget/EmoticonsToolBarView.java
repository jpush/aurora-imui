package cn.jiguang.imui.chatinput.emoji.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.io.IOException;
import java.util.ArrayList;

import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.emoji.ImageLoader;
import cn.jiguang.imui.chatinput.emoji.data.PageSetEntity;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class EmoticonsToolBarView extends RelativeLayout {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected ArrayList<View> mToolBtnList = new ArrayList<>();
    protected int mBtnWidth;

    protected HorizontalScrollView hsv_toolbar;
    protected LinearLayout ly_tool;

    public EmoticonsToolBarView(Context context) {
        this(context, null);
    }

    public EmoticonsToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_emoticonstoolbar, this);
        this.mContext = context;
        mBtnWidth = (int) context.getResources().getDimension(R.dimen.bar_tool_btn_width);
        hsv_toolbar = (HorizontalScrollView) findViewById(R.id.hsv_toolbar);
        ly_tool = (LinearLayout) findViewById(R.id.ly_tool);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() > 3) {
            throw new IllegalArgumentException("can host only two direct child");
        }
    }

    public void addFixedToolItemView(View view, boolean isRight) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams hsvParams = (LayoutParams) hsv_toolbar.getLayoutParams();
        if (view.getId() <= 0) {
            view.setId(isRight ? R.id.id_toolbar_right : R.id.id_toolbar_left);
        }
        if (isRight) {
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, view.getId());
        } else {
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF, view.getId());
        }
        addView(view, params);
        hsv_toolbar.setLayoutParams(hsvParams);
    }

    protected View getCommonItemToolBtn() {
        return mInflater == null ? null : mInflater.inflate(R.layout.item_toolbtn, null);
    }

    protected void initItemToolBtn(View toolBtnView, int rec, final PageSetEntity pageSetEntity, OnClickListener onClickListener){
        ImageView iv_icon = (ImageView) toolBtnView.findViewById(R.id.iv_icon);
        if (rec > 0) {
            iv_icon.setImageResource(rec);
        }
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(mBtnWidth, LayoutParams.MATCH_PARENT);
        iv_icon.setLayoutParams(imgParams);
        if (pageSetEntity != null) {
            iv_icon.setTag(R.id.id_tag_pageset, pageSetEntity);
            try {
                ImageLoader.getInstance(mContext).displayImage(pageSetEntity.getIconUri(), iv_icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        toolBtnView.setOnClickListener(onClickListener != null ? onClickListener : new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListeners != null && pageSetEntity != null) {
                    mItemClickListeners.onToolBarItemClick(pageSetEntity);
                }
            }
        });
    }

    protected View getToolBgBtn(View parentView) {
        return  parentView.findViewById(R.id.iv_icon);
    }

    public void addFixedToolItemView(boolean isRight, int rec, final PageSetEntity pageSetEntity, OnClickListener onClickListener) {
        View toolBtnView = getCommonItemToolBtn();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams hsvParams = (LayoutParams) hsv_toolbar.getLayoutParams();
        if (toolBtnView.getId() <= 0) {
            toolBtnView.setId(isRight ? R.id.id_toolbar_right : R.id.id_toolbar_left);
        }
        if (isRight) {
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, toolBtnView.getId());
        } else {
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF, toolBtnView.getId());
        }
        addView(toolBtnView, params);
        hsv_toolbar.setLayoutParams(hsvParams);
        initItemToolBtn(toolBtnView, rec, pageSetEntity, onClickListener);
    }

    public void addToolItemView(PageSetEntity pageSetEntity) {
        addToolItemView(0, pageSetEntity, null);
    }

    public void addToolItemView(int rec, OnClickListener onClickListener) {
        addToolItemView(rec, null, onClickListener);
    }

    public void addToolItemView(int rec, final PageSetEntity pageSetEntity, OnClickListener onClickListener) {
        View toolBtnView = getCommonItemToolBtn();
        initItemToolBtn(toolBtnView, rec, pageSetEntity, onClickListener);
        ly_tool.addView(toolBtnView);
        mToolBtnList.add(getToolBgBtn(toolBtnView));
    }

    public void setToolBtnSelect(String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return;
        }
        int select = 0;
        for (int i = 0; i < mToolBtnList.size(); i++) {
            Object object = mToolBtnList.get(i).getTag(R.id.id_tag_pageset);
            if (object != null && object instanceof PageSetEntity && uuid.equals(((PageSetEntity) object).getUuid())) {
                mToolBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.toolbar_btn_select));
                select = i;
            } else {
                mToolBtnList.get(i).setBackgroundResource(R.drawable.btn_toolbtn_bg);
            }
        }
        scrollToBtnPosition(select);
    }

    protected void scrollToBtnPosition(final int position) {
        int childCount = ly_tool.getChildCount();
        if (position < childCount) {
            hsv_toolbar.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = hsv_toolbar.getScrollX();

                    int childX = ly_tool.getChildAt(position).getLeft();

                    if (childX < mScrollX) {
                        hsv_toolbar.scrollTo(childX, 0);
                        return;
                    }

                    int childWidth = ly_tool.getChildAt(position).getWidth();
                    int hsvWidth = hsv_toolbar.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if (childRight > scrollRight) {
                        hsv_toolbar.scrollTo(childRight - scrollRight, 0);
                        return;
                    }
                }
            });
        }
    }

    public void setBtnWidth(int width) {
        mBtnWidth = width;
    }

    protected OnToolBarItemClickListener mItemClickListeners;

    public interface OnToolBarItemClickListener {
        void onToolBarItemClick(PageSetEntity pageSetEntity);
    }

    public void setOnToolBarItemClickListener(OnToolBarItemClickListener listener) {
        this.mItemClickListeners = listener;
    }
}
