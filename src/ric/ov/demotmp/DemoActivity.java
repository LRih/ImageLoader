package ric.ov.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import ric.ov.main.ImageLoader;
import ric.ov.main.SaveLocation;

public final class DemoActivity extends Activity
{
    private static final String URL = "http://www.planwallpaper.com/static/images/i-should-buy-a-boat.jpg";

    private ImageView _img1, _img2;
    private ImageView _img3, _img4;
    private ImageView _img5, _img6;

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

        // load image from resources
        new ImageLoader(this).fromResource(R.drawable.image).setRounded().to(_img1);
        new ImageLoader(this).fromResource(R.drawable.image).withAnimation(R.anim.fade_scale_in).to(_img2);

        // load image from network
        new ImageLoader(this).fromNetwork(URL, SaveLocation.None).to(_img3);
        new ImageLoader(this).fromNetwork(URL, SaveLocation.None).withAnimation(R.anim.fade_scale_in).to(_img4);

        // load image from network (cached)
        new ImageLoader(this).fromNetwork(URL, SaveLocation.Cache).to(_img5);
        new ImageLoader(this).fromNetwork(URL, SaveLocation.Cache).withAnimation(R.anim.fade_scale_in).to(_img6);
    }
}