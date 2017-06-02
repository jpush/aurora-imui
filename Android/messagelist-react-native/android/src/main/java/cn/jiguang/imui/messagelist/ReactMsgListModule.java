package cn.jiguang.imui.messagelist;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by caiyaoguan on 2017/6/2.
 */

public class ReactMsgListModule extends ReactContextBaseJavaModule {

    private final String REACT_MSG_LIST_MODULE = "RCTMsgListModule";

    public ReactMsgListModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_MSG_LIST_MODULE;
    }

    @Override
    public void initialize() {
        super.initialize();

    }


}
