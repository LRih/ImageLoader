package ric.ov.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * For loading images from resources asynchronously.
 */
final class ImageTask extends AsyncTask<Void, Void, Drawable>
{
    //========================================================================= VARIABLES
    private final ImageLoader _loader;

    //========================================================================= INITIALIZE
    public ImageTask(ImageLoader loader)
    {
        _loader = loader;
    }

    //========================================================================= FUNCTIONS
    protected final Drawable doInBackground(Void... params)
    {
        try
        {
            return doInBackground();
        }
        catch (Exception e)
        {
            Log.i(getClass().getSimpleName(), e.getMessage());
        }

        return null;
    }
    private Drawable doInBackground() throws Exception
    {
        Bitmap bmp;

        // decode based on source type
        switch (_loader.type())
        {
            case Resource:
                bmp = BitmapUtils.decodeResource(_loader.context(), _loader.drawableId(), _loader.reqWidth(), _loader.reqHeight());
                break;
            case File:
                bmp = BitmapUtils.decodeFile(_loader.imgPath(), _loader.reqWidth(), _loader.reqHeight());
                break;
            case Network:
                bmp = decodeUrl(_loader.context(), _loader.imgPath(), _loader.reqWidth(), _loader.reqHeight(), _loader.saveLocation());
                break;
            default:
                throw new IllegalArgumentException("Unsupported type specified");
        }

        return new BitmapDrawable(_loader.context().getResources(), bmp);
    }

    private Bitmap decodeUrl(Context context, String imgUrl, int reqWidth, int reqHeight, SaveLocation saveLocation) throws IOException, NoSuchAlgorithmException
    {
        switch (saveLocation)
        {
            case Cache:
                return BitmapUtils.decodeUrlAndCache(context, imgUrl, reqWidth, reqHeight);
            case Internal:
                return BitmapUtils.decodeUrlAndSaveInternal(context, imgUrl, reqWidth, reqHeight);
            case None:
                return BitmapUtils.decodeUrl(imgUrl, reqWidth, reqHeight);
            default:
                throw new IllegalArgumentException("Unsupported save location specified");
        }
    }

    /**
     * Use thread pools if Android version supports it.
     */
    public final void run()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(THREAD_POOL_EXECUTOR);
        else
            execute();
    }

    //========================================================================= EVENTS
    protected final void onPostExecute(Drawable drawable)
    {
        super.onPostExecute(drawable);

        if (drawable != null)
        {
            final ImageView view = _loader.view();
            if (view != null)
                view.setImageDrawable(drawable);
        }
    }
}
