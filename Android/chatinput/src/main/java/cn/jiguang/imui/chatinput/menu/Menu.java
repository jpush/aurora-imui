package cn.jiguang.imui.chatinput.menu;


public class Menu {

    public static final String TAG_VOICE = "voice";
    public static final String TAG_GALLERY = "gallery";
    public static final String TAG_CAMERA = "camera";
    public static final String TAG_EMOJI = "emoji";
    public static final String TAG_SEND = "send";

    private final boolean customize;
    private final String[] left;
    private final String[] right;
    private final String[] bottom;

    private Menu(Builder builder) {
        this.customize = builder.customize;
        this.left = builder.left;
        this.right = builder.right;
        this.bottom = builder.bottom;
    }

    public boolean isCustomize() {
        return customize;
    }

    public String[] getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public String[] getBottom() {
        return bottom;
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder{
        private boolean customize;
        private String[] left;
        private String[] right;
        private String[] bottom;

        public Builder customize(boolean customize){
            this.customize = customize;
            return this;
        }


        public Builder setLeft(String ... tags){
            this.left =tags;
            return this;
        }

        public Builder setRight(String ... tags){
            this.right =tags;
            return this;
        }

        public Builder setBottom(String ... tags){
            this.bottom =tags;
            return this;
        }

        public Menu build(){
            return  new Menu(this);
        }

    }
}
