## ImageLoader
[English Document](./imageLoaderEn.md)

为了不限定加载图片的方式（有很多第三方库可以很方便地用来加载图片），我们提供了 ImageLoader 接口，只要实现这个接口，就可以
使用你自己定义的方式来加载图片。
例如使用 Picasso 来加载图片：
```java
ImageLoader imageLoader = new ImageLoader() {
  @Override
  public void loadAvatarImage(ImageView avatarImageView, String string) {
      Glide.with(mContext)
          .load(string)
          .centerCrop()
          .into(avatarImageView);
  }

  @Override
  public void loadImage(ImageView imageView, String string) {
      Picasso.with(mContext)
          .load(string)
          .into(imageView);
  }
};
```

或者 Glide:
```java
ImageLoader imageLoader = new ImageLoader() {
  @Override
  public void loadAvatarImage(ImageView avatarImageView, String string) {
      Glide.with(mContext)
          .load(string)
          .centerCrop()
          .into(avatarImageView);
  }

  @Override
  public void loadImage(ImageView imageView, String string) {
      Glide.with(mContext)
          .load(string)
          .centerCrop()
          .into(imageView);
  }
};
```
