##### 新增react-native安卓端，消息长按时，获取当前消息的偏移距离(x,y)和当前消息框的width和height。
###### 修改方式：主要增加Android端原生方法view.getLocationOnScreen()用于获取当前view的位置和width,height;
###### Android studio打开/aurora-imui/ReactNative/sample/android项目，在依赖库‘aurora-imui-react-native’中修改相关方法：
* 在ReactMsgListManager.java中setMsgLongClickListener方法里面添加： 
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                int x = location[0];//获取当前位置的横坐标
                int y = location[1];//获取当前位置的纵坐标
                message.setView_x(px2dp(mContext, x));
                message.setView_y(px2dp(mContext, y));
                message.setMsg_width(px2dp(mContext,view.getWidth()));//当前消息的的宽和高
                message.setMsg_height(px2dp(mContext,view.getHeight()));

                此处还有一个px转dp的方法：
                public static int px2dp(Context context, float pxValue) {
                    final float scale = context.getResources().getDisplayMetrics().density;
                    return (int) (pxValue / scale + 0.5f);
    }


* 在RCTMessage.java这个实体类中，添加对应的字段：
    public static final String VIEW_X = "viewx";
    public static final String VIEW_Y = "viewy";
    public static final String MSG_WIDTH="msg_width";
    public static final String MSG_HEIGHT="msg_height";
     private int view_x;
    private int view_y;
    private int msg_width;
    private int msg_height;

    public int getMsg_width() {
        return msg_width;
    }

    public void setMsg_width(int msg_width) {
        this.msg_width = msg_width;
    }

    public int getMsg_height() {
        return msg_height;
    }

    public void setMsg_height(int msg_height) {
        this.msg_height = msg_height;
    }

    public int getView_x() {
        return view_x;
    }

    public void setView_x(int view_x) {
        this.view_x = view_x;
    }

    public int getView_y() {
        return view_y;
    }

    public void setView_y(int view_y) {
        this.view_y = view_y;
    }

    最后在toJSON()方法中添加：
        json.addProperty(VIEW_X, view_x);
        json.addProperty(VIEW_Y, view_y);
        json.addProperty(MSG_WIDTH,msg_width);
        json.addProperty(MSG_HEIGHT,msg_height);



