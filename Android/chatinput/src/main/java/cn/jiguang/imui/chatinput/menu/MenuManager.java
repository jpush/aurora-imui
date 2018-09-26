package cn.jiguang.imui.chatinput.menu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils;
import cn.jiguang.imui.chatinput.listener.CustomMenuEventListener;
import cn.jiguang.imui.chatinput.menu.collection.MenuCollection;
import cn.jiguang.imui.chatinput.menu.collection.MenuFeatureCollection;
import cn.jiguang.imui.chatinput.menu.collection.MenuItemCollection;
import cn.jiguang.imui.chatinput.menu.view.MenuFeature;
import cn.jiguang.imui.chatinput.menu.view.MenuItem;
import cn.jiguang.imui.chatinput.utils.SimpleCommonUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MenuManager {
    public static final String TAG = SimpleCommonUtils.formatTag(MenuManager.class.getSimpleName());

    private ChatInputView mChatInputView;
    private LinearLayout mChatInputContainer;
    private LinearLayout mMenuItemContainer;
    private FrameLayout mMenuContainer;
    private Context mContext;
    private MenuItemCollection mMenuItemCollection;
    private MenuFeatureCollection mMenuFeatureCollection;
    private CustomMenuEventListener mCustomMenuEventListener;

    public MenuManager(ChatInputView chatInputView) {
        mChatInputView = chatInputView;
        mContext = chatInputView.getContext();
        mChatInputContainer = chatInputView.getChatInputContainer();
        mMenuItemContainer = chatInputView.getMenuItemContainer();
        mMenuContainer = chatInputView.getMenuContainer();
        initCollection();

        initDefaultMenu();
    }

    private void initCollection() {
        mMenuItemCollection = new MenuItemCollection(mContext);

        mMenuItemCollection.setMenuCollectionChangedListener(new MenuCollection.MenuCollectionChangedListener() {
            @Override
            public void addMenu(String menuTag, View menu) {
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChatInputView.getInputView().clearFocus();
                        String tag = (String) v.getTag();
                        if (mCustomMenuEventListener != null && mCustomMenuEventListener.onMenuItemClick(tag, (MenuItem) v)) {
                            showMenuFeatureByTag(tag);
                        }
                    }
                });
            }
        });

        mMenuFeatureCollection = new MenuFeatureCollection(mContext);
        mMenuFeatureCollection.setMenuCollectionChangedListener(new MenuCollection.MenuCollectionChangedListener() {
            @Override
            public void addMenu(String menuTag, View menu) {
                menu.setVisibility(View.GONE);
                mMenuContainer.addView(menu);
            }
        });

    }


    private void showMenuFeatureByTag(String tag) {


        View menuFeature = mMenuFeatureCollection.get(tag);
        if (menuFeature == null) {
            Log.i(TAG, "Can't find MenuFeature to show by tag:" + tag);
            return;
        }

        if (menuFeature.getVisibility() == VISIBLE && mMenuContainer.getVisibility() == VISIBLE) {
            mChatInputView.dismissMenuLayout();
            return;
        }

        if (mChatInputView.isKeyboardVisible()) {
            mChatInputView.setPendingShowMenu(true);
            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInputView.getInputView());

        } else {
            mChatInputView.showMenuLayout();

        }

        mChatInputView.hideDefaultMenuLayout();
        hideCustomMenu();
        menuFeature.setVisibility(VISIBLE);
        if (mCustomMenuEventListener != null)
            mCustomMenuEventListener.onMenuFeatureVisibilityChanged(VISIBLE, tag, (MenuFeature) menuFeature);
        lastMenuFeature = menuFeature;

    }

    private View lastMenuFeature;

    public void hideCustomMenu() {
        if (lastMenuFeature != null && lastMenuFeature.getVisibility() != GONE) {
            lastMenuFeature.setVisibility(View.GONE);
            if (mCustomMenuEventListener != null)
                mCustomMenuEventListener.onMenuFeatureVisibilityChanged(GONE, (String) lastMenuFeature.getTag(), (MenuFeature) lastMenuFeature);
        }

    }


    private void initDefaultMenu() {
        addBottomByTag(Menu.TAG_VOICE,
                Menu.TAG_GALLERY,
                Menu.TAG_CAMERA,
                Menu.TAG_EMOJI,
                Menu.TAG_SEND);
    }

    public void setMenu(Menu menu) {
        if (menu.isCustomize()) {
            mMenuItemContainer.removeAllViews();
            addViews(mChatInputContainer, 1, menu.getLeft());
            addViews(mChatInputContainer, mChatInputContainer.getChildCount() - 1, menu.getRight());
            addBottomByTag(menu.getBottom());
        }
    }

    private void addBottomByTag(String... tags) {
        if (tags == null || tags.length == 0) {
            mChatInputView.setShowBottomMenu(false);
            return;
        }
        mChatInputView.setShowBottomMenu(true);
        addViews(mMenuItemContainer, -1, tags);
    }


    private void addViews(LinearLayout parent, int index, String... tags) {
        if (parent == null || tags == null)
            return;
        for (String tag : tags) {
            View child = mMenuItemCollection.get(tag);
            if (child == null) {
                Log.e(TAG, "Can't find view by tag " + tag + ".");
                continue;
            }

            parent.addView(child, index);
        }
    }

    public MenuItemCollection getMenuItemCollection() {
        return mMenuItemCollection;
    }

    public MenuFeatureCollection getMenuFeatureCollection() {
        return mMenuFeatureCollection;
    }

    public void addCustomMenu(String tag, MenuItem menuItem, MenuFeature menuFeature) {

        mMenuItemCollection.addCustomMenuItem(tag, menuItem);
        mMenuFeatureCollection.addMenuFeature(tag, menuFeature);
    }

    public void addCustomMenu(String tag, int menuItemResource, int menuFeatureResource) {

        mMenuItemCollection.addCustomMenuItem(tag, menuItemResource);
        mMenuFeatureCollection.addMenuFeature(tag, menuFeatureResource);
    }


    public void setCustomMenuClickListener(CustomMenuEventListener listener) {
        this.mCustomMenuEventListener = listener;
    }


}
