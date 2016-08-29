# ImageLoader
ImageLoader is a small lightweight Android library for asynchronously loading images into ImageViews. Supports loading images from resources, local storage and the internet. Images can optionally be cached.

## Usage
Loading an image from resources:
```java
ImageView img = (ImageView)findViewById(R.id.img);
new ImageLoader(this).fromResource(R.drawable.image).to(img);
```

Loading an image from the internet (cached):
```java
String url = "http://path.to.image";
ImageView img = (ImageView)findViewById(R.id.img);
new ImageLoader(this).fromNetwork(url, SaveLocation.Cache).to(img);
```

## Compatibility
ImageLoader requires a minimum API level of 8 (2.2 Froyo).
