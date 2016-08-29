package ric.ov.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;

/**
 * For loading images from resources asynchronously.
 */
final class ImageTask extends AsyncTask<Void, Void, Drawable>
{
    //========================================================================= VARIABLES
    private final ImageLoader _loader;
    private final WeakReference<ImageView> _img;

    //========================================================================= INITIALIZE
    public ImageTask(ImageLoader loader, ImageView img)
    {
        _loader = loader;
        _img = new WeakReference<ImageView>(img);
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

        // get image view dimensions
        int reqWidth = _img.get().getWidth();
        int reqHeight = _img.get().getHeight();

        // decode based on source type
        switch (_loader.type())
        {
            case Resource:
                bmp = BitmapUtils.decodeResource(_loader.context(), _loader.drawableId(), reqWidth, reqHeight);
                break;
            case File:
                bmp = BitmapUtils.decodeFile(_loader.imgPath(), reqWidth, reqHeight);
                break;
            case Network:
                bmp = decodeUrl(_loader.context(), _loader.imgPath(), reqWidth, reqHeight, _loader.saveLocation());
                break;
            default:
                throw new IllegalArgumentException("Unsupported type specified");
        }

        if (_loader.isRounded())
            bmp = BitmapUtils.createRoundedBitmap(bmp);

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

        if (drawable == null)
            return;

        final ImageView img = _img.get();

        if (img == null)
            return;

        img.setImageDrawable(drawable);

        // perform custom animation if set
        if (_loader.animationId() != -1)
            img.startAnimation(AnimationUtils.loadAnimation(_loader.context(), _loader.animationId()));
    }
}
