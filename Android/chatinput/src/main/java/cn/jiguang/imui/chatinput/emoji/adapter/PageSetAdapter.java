package cn.jiguang.imui.chatinput.emoji.adapter;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.jiguang.imui.chatinput.emoji.data.PageEntity;
import cn.jiguang.imui.chatinput.emoji.data.PageSetEntity;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class PageSetAdapter extends PagerAdapter {

    private final ArrayList<PageSetEntity> mPageSetEntityList = new ArrayList<>();

    public ArrayList<PageSetEntity> getPageSetEntityList() {
        return mPageSetEntityList;
    }

    public int getPageSetStartPosition(PageSetEntity pageSetEntity) {
        if (pageSetEntity == null || TextUtils.isEmpty(pageSetEntity.getUuid())) {
            return 0;
        }

        int startPosition = 0;
        for (int i = 0; i < mPageSetEntityList.size(); i++) {
            if (i == mPageSetEntityList.size() - 1 && !pageSetEntity.getUuid().equals(mPageSetEntityList.get(i).getUuid())) {
                return 0;
            }
            if (pageSetEntity.getUuid().equals(mPageSetEntityList.get(i).getUuid())) {
                return startPosition;
            }
            startPosition += mPageSetEntityList.get(i).getPageCount();
        }
        return startPosition;
    }

    public void add(View view) {
        add(mPageSetEntityList.size(), view);
    }

    public void add(int index, View view) {
        PageSetEntity pageSetEntity = new PageSetEntity.Builder()
                .addPageEntity(new PageEntity(view))
                .setShowIndicator(false)
                .build();
        mPageSetEntityList.add(index, pageSetEntity);
    }

    public void add(PageSetEntity pageSetEntity) {
        add(mPageSetEntityList.size(), pageSetEntity);
    }

    public void add(int index, PageSetEntity pageSetEntity) {
        if (pageSetEntity == null) {
            return;
        }
        mPageSetEntityList.add(index, pageSetEntity);
    }

    public PageSetEntity get(int position) {
        return mPageSetEntityList.get(position);
    }

    public void remove(int position) {
        mPageSetEntityList.remove(position);
        notifyData();
    }

    public void notifyData() { }

    public PageEntity getPageEntity(int position) {
        for (PageSetEntity pageSetEntity : mPageSetEntityList) {
            if (pageSetEntity.getPageCount() > position) {
                return (PageEntity) pageSetEntity.getPageEntityList().get(position);
            } else {
                position -= pageSetEntity.getPageCount();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (PageSetEntity pageSetEntity : mPageSetEntityList) {
            count += pageSetEntity.getPageCount();
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getPageEntity(position).instantiateItem(container, position, null);
        if(view == null){
            return null;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}