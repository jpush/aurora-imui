## ImageLoader
[中文文档](./imageLoader.md)

For loading images with free style(There are many third-party libraries can easily use to load image),
we define ImageLoader interface, so you can use the way you like to load image.

Take Picasso for example:
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

or Glide:
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
