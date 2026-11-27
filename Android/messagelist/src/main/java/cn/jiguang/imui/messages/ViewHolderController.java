package cn.jiguang.imui.messages;

import java.lang.ref.WeakReference;

/**
 * @deprecated Voice playback state is owned by each {@link MsgListAdapter}.
 * Use {@link MsgListAdapter#replayVoice()} instead.
 */
@Deprecated
public class ViewHolderController {

    private static final ViewHolderController INSTANCE = new ViewHolderController();

    private WeakReference<MsgListAdapter<?>> mPlayingAdapter = new WeakReference<>(null);

    private ViewHolderController() {
    }

    public static ViewHolderController getInstance() {
        return INSTANCE;
    }

    void onPlaybackStarted(MsgListAdapter<?> adapter) {
        mPlayingAdapter = new WeakReference<MsgListAdapter<?>>(adapter);
    }

    public void replayVoice() {
        MsgListAdapter<?> adapter = mPlayingAdapter.get();
        if (adapter != null) {
            adapter.replayVoice();
        }
    }
}
