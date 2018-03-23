package cn.jiguang.imui.commons;


import android.widget.ImageView;

public interface ImageLoader {

    /**
     * Load image into avatar's ImageView.
     * @param avatarImageView Avatar's ImageView.
     * @param string A file path, or a uri or url.
     */
    void loadAvatarImage(ImageView avatarImageView, String string);

    /**
     * Load image into image message's ImageView.
     * @param imageView Image message's ImageView.
     * @param string A file path, or a uri or url.
     */
    void loadImage(ImageView imageView, String string);

    /**
     * Load video to video message's image cover.
     * @param imageCover Video message's image cover
     * @param uri Local path or url.
     */
    void loadVideo(ImageView imageCover, String uri);
}
