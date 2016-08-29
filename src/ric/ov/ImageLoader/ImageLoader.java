package ric.ov.ImageLoader;

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

    private SourceType _type;
    private int _drawableId;
    private String _imgPath;

    private int _reqWidth;
    private int _reqHeight;
    private SaveLocation _saveLocation;

    //========================================================================= INITIALIZE
    public ImageLoader(Context context)
    {
        _context = context;
    }
    private ImageLoader(ImageLoader loader)
    {
        _context = loader._context;
        _img = loader._img;

        _type = loader._type;
        _drawableId = loader._drawableId;
        _imgPath = loader._imgPath;

        _reqWidth = loader._reqWidth;
        _reqHeight = loader._reqHeight;
        _saveLocation = loader._saveLocation;
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

    public final void to(final ImageView img)
    {
        // post in case called from onCreate(), in which case view width/height will be zero
        img.post(new Runnable()
        {
            public final void run()
            {
                _img = new WeakReference<ImageView>(img);

                // get image view dimensions
                _reqWidth = _img.get().getWidth();
                _reqHeight = _img.get().getHeight();

                new ImageTask(new ImageLoader(ImageLoader.this)).run();
            }
        });
    }

    //========================================================================= PROPERTIES
    final Context context()
    {
        return _context;
    }
    final ImageView view()
    {
        return _img.get();
    }

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

    final int reqWidth()
    {
        return _reqWidth;
    }
    final int reqHeight()
    {
        return _reqHeight;
    }
    final SaveLocation saveLocation()
    {
        return _saveLocation;
    }
}
