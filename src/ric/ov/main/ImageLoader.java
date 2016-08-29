package ric.ov.main;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Holds attributes for image to be loaded.
 */
public final class ImageLoader
{
    //========================================================================= VARIABLES
    private final Context _context;
    private WeakReference<ImageView> _img;

    private int _reqWidth;
    private int _reqHeight;

    private SourceType _type;
    private int _drawableId;
    private String _imgPath;
    private SaveLocation _saveLocation;

    private boolean _isRounded = false;
    private int _animationId = -1;

    //========================================================================= INITIALIZE
    public ImageLoader(Context context)
    {
        _context = context;
    }
    private ImageLoader(ImageLoader loader)
    {
        _context = loader._context;
        _img = loader._img;

        _reqWidth = loader._reqWidth;
        _reqHeight = loader._reqHeight;

        _type = loader._type;
        _drawableId = loader._drawableId;
        _imgPath = loader._imgPath;
        _saveLocation = loader._saveLocation;

        _isRounded = loader._isRounded;
        _animationId = loader._animationId;
    }

    //========================================================================= FUNCTIONS
    public final ImageLoader fromResource(int drawableId)
    {
        _type = SourceType.Resource;
        _drawableId = drawableId;

        return this;
    }
    public final ImageLoader fromFile(String imgPath)
    {
        _type = SourceType.File;
        _imgPath = imgPath;

        return this;
    }
    public final ImageLoader fromNetwork(String imgUrl, SaveLocation saveLocation)
    {
        _type = SourceType.Network;
        _imgPath = imgUrl;
        _saveLocation = saveLocation;

        return this;
    }

    /**
     * Create image with rounded corners.
     */
    public final ImageLoader setRounded()
    {
        _isRounded = true;
        return this;
    }

    /**
     * View animation to play after image has loaded.
     */
    public final ImageLoader withAnimation(int animId)
    {
        _animationId = animId;
        return this;
    }

    public final void to(final ImageView img)
    {
        final ImageLoader loader = new ImageLoader(this);

        // post in case called from onCreate(), in which case view width/height will be zero
        img.post(new Runnable()
        {
            public final void run()
            {
                new ImageTask(loader, img).run();
            }
        });
    }

    //========================================================================= PROPERTIES
    final Context context()
    {
        return _context;
    }
//    final ImageView view()
//    {
//        return _img.get();
//    }

//    final int reqWidth()
//    {
//        return _reqWidth;
//    }
//    final int reqHeight()
//    {
//        return _reqHeight;
//    }

    final SourceType type()
    {
        return _type;
    }
    final int drawableId()
    {
        return _drawableId;
    }
    final String imgPath()
    {
        return _imgPath;
    }
    final SaveLocation saveLocation()
    {
        return _saveLocation;
    }

    final boolean isRounded()
    {
        return _isRounded;
    }
    final int animationId()
    {
        return _animationId;
    }
}
