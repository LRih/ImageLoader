# ImageLoader
ImageLoader is a small lightweight Android library for asynchronously loading images into ImageViews. Supports loading images from all sources including resources, local storage and the internet. Images can optionally be cached.

## Usage
Loading an image from resource:
```java
ImageView img = (ImageView)findViewById(R.id.img);
new ImageLoader(this).fromResource(R.drawable.image).to(img);
```

## Compatibility
ImageLoader requires a minimum API level of 8 (2.2 Froyo).