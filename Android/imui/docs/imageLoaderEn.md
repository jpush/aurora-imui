## ImageLoader
[中文文档](./imageLoader.md)

For loading images with free style(There are many third-party libraries can easily use to load image),
we define ImageLoader interface, so you can use the way you like to load image.

Take Picasso for example:

```
ImageLoader imageLoader = new ImageLoader() {
   @Override
   public void loadImage(ImageView imageView, String url) {
       Picasso.with(MessagesListActivity.this).load(url).into(imageView);
   }
};
```

or Glide:

```
ImageLoader imageLoader = new ImageLoader() {
   @Override
   public void loadImage(ImageView imageView, String url) {
       Glide.with(mContext)
                   .load(url)
                   .centerCrop()
                   .into(imageView);
   }
};
```