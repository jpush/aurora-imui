package cn.jiguang.imui.chatinput.emoji.listener;

import android.view.ViewGroup;

import cn.jiguang.imui.chatinput.emoji.adapter.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}