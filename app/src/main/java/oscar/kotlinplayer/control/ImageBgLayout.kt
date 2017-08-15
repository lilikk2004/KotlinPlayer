package oscar.kotlinplayer.control

/**
 * Created by oscar on 2017/8/6.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by oscar on 2016/6/26.
 */
public class ImageBgLayout : RelativeLayout {

    companion object {
        val TAG = "ImageBgLayout"
        val IMG_NULL = 0
        val IMG_A = 1
        val IMG_B = 2
        val CHANGE_DELAY = 20.toLong()
        val PER_ALPHA = 15
    }

    private var bitmapA : Bitmap? = null
    private var bitmapB : Bitmap? = null
    private var mode = IMG_NULL
    private var alphaA = 0
    private var mainHandler = Handler()
    private var changeRunnable = {}

    constructor (context : Context, attrs : AttributeSet) : super(context, attrs){
        setWillNotDraw(false)
        changeRunnable = {
            if (mode == IMG_B) {
                alphaA -= PER_ALPHA;
                if (alphaA <= 0) {
                    alphaA = 0;
                } else {
                    mainHandler.postDelayed(changeRunnable, CHANGE_DELAY);
                }
            } else if (mode == IMG_A) {
                alphaA += PER_ALPHA;
                if (alphaA >= 255) {
                    alphaA = 255;
                } else {
                    mainHandler.postDelayed(changeRunnable, CHANGE_DELAY);
                }
            }
            postInvalidate();
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw alphaA:$alphaA")
        if(mode == IMG_NULL){
            return
        }
        var paint = Paint()
        if(alphaA == 255){
            if(bitmapA != null) {
                Log.d(TAG, "draw bitmapA")
                canvas.drawBitmap(bitmapA, 0.toFloat(), 0.toFloat(), paint)
            }
            return
        }
        if(alphaA == 0){
            if(bitmapB != null) {
                Log.d(TAG, "draw bitmapB")
                canvas.drawBitmap(bitmapB, 0.toFloat(), 0.toFloat(), paint)
            }
            return
        }

        paint.alpha = alphaA
        canvas.drawBitmap(bitmapA, 0.toFloat(), 0.toFloat(), paint)
        paint.alpha = 255 - alphaA
        canvas.drawBitmap(bitmapB, 0.toFloat(), 0.toFloat(), paint)
    }

    fun setBitmap(bitmap : Bitmap){
        when(mode){
            IMG_NULL->{
                alphaA = 255
                mode = IMG_A
                bitmapA = bitmap
                postInvalidate()
            }
            IMG_A->{
                mode = IMG_B
                bitmapB = bitmap
                mainHandler.postDelayed(changeRunnable, CHANGE_DELAY)
            }
            IMG_B->{
                mode = IMG_A
                bitmapA = bitmap
                mainHandler.postDelayed(changeRunnable, CHANGE_DELAY)
            }
        }
    }

}