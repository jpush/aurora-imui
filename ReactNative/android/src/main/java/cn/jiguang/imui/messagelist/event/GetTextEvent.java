package cn.jiguang.imui.messagelist.event;

/**
 * Created by caiyaoguan on 2017/11/24.
 */

public class GetTextEvent {

    private String text;
    private String action;

    public GetTextEvent(String text, String action) {
        this.text = text;
        this.action = action;
    }

    public String getText() {
        return this.text;
    }

    public String getAction() {
        return this.action;
    }
}
