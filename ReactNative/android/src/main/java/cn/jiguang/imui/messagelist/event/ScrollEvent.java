package cn.jiguang.imui.messagelist.event;

public class ScrollEvent {

    private boolean scrollToBottom;

    public ScrollEvent(boolean scrollToBottom) {
        this.scrollToBottom = scrollToBottom;
    }

    public boolean getFlag() {
        return this.scrollToBottom;
    }
}
