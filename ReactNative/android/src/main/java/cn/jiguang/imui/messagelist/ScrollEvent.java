package cn.jiguang.imui.messagelist;

public class ScrollEvent {

    private boolean scrollToBottom;

    public ScrollEvent(boolean scrollToBottom) {
        this.scrollToBottom = scrollToBottom;
    }

    public boolean getFlag() {
        return this.scrollToBottom;
    }
}
