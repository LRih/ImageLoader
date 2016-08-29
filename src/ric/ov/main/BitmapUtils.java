package ric.ov.main;

import android.content.Context;
import android.graphics.*;
import android.media.ExifInterface;

import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

final class BitmapUtils
{
    //========================================================================= INITIALIZE
    private BitmapUtils()
    {
        throw new AssertionError();
    }

    //========================================================================= FUNCTIONS
    public static Bitmap createRoundedBitmap(Bitmap bmp)
    {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

        Canvas canvas = new Canvas(newBmp);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.MAGENTA);

        canvas.drawRoundRect(new RectF(rect), bmp.getWidth() / 2f, bmp.getHeight() / 2f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);

        // recycle old bitmap
        bmp.recycle();

        return newBmp;
    }

    public static Bitmap decodeResource(Context context, int drawableId, int reqWidth, int reqHeight)
    {
        // first check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, options);

        // calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), drawableId, options);
    }

    public static Bitmap decodeFile(String imgPath, int reqWidth, int reqHeight) throws IOException
    {
        int orientation = getOrientation(imgPath);

        // rotate image if required
        if (orientation != ExifInterface.ORIENTATION_NORMAL)
            return decodeFileRotated(imgPath, reqWidth, reqHeight, orientation);

        return decodeFileUnrotated(imgPath, reqWidth, reqHeight);
    }

    private static Bitmap decodeFileRotated(String imgPath, int reqWidth, int reqHeight, int orientation)
    {
        Bitmap bmp = BitmapFactory.decodeFile(imgPath);

        switch (orientation)
        {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bmp, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bmp, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bmp, 270);
        }

        return createScaledBitmap(bmp, reqWidth, reqHeight);
    }
    private static Bitmap decodeFileUnrotated(String imgPath, int reqWidth, int reqHeight)
    {
        // first check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);

        // calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    public static Bitmap decodeUrl(String url, int reqWidth, int reqHeight) throws IOException
    {
        // first check dimensions
        InputStream stream = new URL(url).openStream();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);

        stream.close();


        // calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        // decode bitmap with inSampleSize set
        stream = new URL(url).openStream();

        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeStream(stream, null, options);

        stream.close();

        return bmp;
    }

    /**
     * Decodes images from the web, then saving them to local storage for use as a backup source
     * in the event of no internet access.
     */
    public static Bitmap decodeUrlAndCache(Context context, String url, int reqWidth, int reqHeight) throws IOException, NoSuchAlgorithmException
    {
        String name = CryptoUtils.md5(url);
        File file = new File(context.getCacheDir(), name);

        return decodeUrlAndSave(url, file, reqWidth, reqHeight);
    }
    public static Bitmap decodeUrlAndSaveInternal(Context context, String url, int reqWidth, int reqHeight) throws IOException, NoSuchAlgorithmException
    {
        String name = CryptoUtils.md5(url);
        File file = new File(context.getFilesDir(), name);

        return decodeUrlAndSave(url, file, reqWidth, reqHeight);
    }
    private static Bitmap decodeUrlAndSave(String url, File file, int reqWidth, int reqHeight) throws IOException, NoSuchAlgorithmException
    {
        // save image in file if it doesn't exist
        if (!file.exists())
            saveBitmap(url, file, reqWidth, reqHeight);

        // return saved image
        return BitmapUtils.decodeFile(file.getAbsolutePath(), reqWidth, reqHeight);
    }


    /**
     * Functions for saving images from the web to local storage.
     */
    private static String saveCacheBitmap(Context context, String imgUrl, String name, int reqWidth, int reqHeight) throws IOException
    {
        File file = new File(context.getCacheDir(), name);
        return saveBitmap(imgUrl, file, reqWidth, reqHeight);
    }
    private static String saveInternalBitmap(Context context, String imgUrl, String name, int reqWidth, int reqHeight) throws IOException
    {
        File file = new File(context.getFilesDir(), name);
        return saveBitmap(imgUrl, file, reqWidth, reqHeight);
    }
    private static String saveBitmap(String imgUrl, File file, int reqWidth, int reqHeight) throws IOException
    {
        Bitmap bmp = decodeUrl(imgUrl, reqWidth, reqHeight);

        FileOutputStream stream = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        stream.close();
        bmp.recycle();

        return file.getAbsolutePath();
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // raw width and height of image
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            /* calculate the largest inSampleSize value that is a power of 2 and keeps both
               width and height larger than the requested width and height */
            while ((halfWidth / inSampleSize) > reqWidth && (halfHeight / inSampleSize) > reqHeight)
                inSampleSize *= 2;
        }

        return inSampleSize;
    }

    private static int getOrientation(String imgPath) throws IOException
    {
        ExifInterface exif = new ExifInterface(imgPath);
        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    private static Bitmap createScaledBitmap(Bitmap bmp, int reqWidth, int reqHeight)
    {
        // scaling required
        if (bmp.getWidth() > reqWidth && bmp.getHeight() > reqHeight)
        {
            // calculate scale
            float scale = Math.max((float)reqWidth / bmp.getWidth(), (float)reqHeight / bmp.getHeight());
            int newWidth = (int)(bmp.getWidth() * scale);
            int newHeight = (int)(bmp.getHeight() * scale);

            Bitmap newBmp = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);

            // dispose old bitmap and replace with scaled bitmap
            bmp.recycle();
            bmp = newBmp;
        }

        return bmp;
    }

    private static Bitmap rotateBitmap(Bitmap bmp, int degrees)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        bmp.recycle();

        return newBmp;
    }
}
