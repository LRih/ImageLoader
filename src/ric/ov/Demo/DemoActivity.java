package ric.ov.Demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import ric.ov.ImageLoader.ImageLoader;
import ric.ov.ImageLoader.SaveLocation;

public final class DemoActivity extends Activity
{
    private static final String URL = "http://www.planwallpaper.com/static/images/i-should-buy-a-boat.jpg";

    private ImageView _img1, _img2, _img3;
    private ImageView _img4, _img5, _img6;
    private ImageView _img7, _img8, _img9;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // get image view references
        _img1 = (ImageView)findViewById(R.id.img1);
        _img2 = (ImageView)findViewById(R.id.img2);
        _img3 = (ImageView)findViewById(R.id.img3);
        _img4 = (ImageView)findViewById(R.id.img4);
        _img5 = (ImageView)findViewById(R.id.img5);
        _img6 = (ImageView)findViewById(R.id.img6);
        _img7 = (ImageView)findViewById(R.id.img7);
        _img8 = (ImageView)findViewById(R.id.img8);
        _img9 = (ImageView)findViewById(R.id.img9);

        // load image from resources into separate image views
        ImageLoader loader = new ImageLoader(this).fromResource(R.drawable.image);
        loader.to(_img1);
        loader.to(_img2);
        loader.to(_img3);

        // load image from network into separate image views
        loader = new ImageLoader(this).fromNetwork(URL, SaveLocation.None);
        loader.to(_img4);
        loader.to(_img5);
        loader.to(_img6);

        // load image from network (cached) into separate image views
        loader = new ImageLoader(this).fromNetwork(URL, SaveLocation.Cache);
        loader.to(_img7);
        loader.to(_img8);
        loader.to(_img9);
    }
}