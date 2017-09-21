package cn.jiguang.imui.messagelist.event;


public class LoadedEvent {

    private String action;

    public LoadedEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
