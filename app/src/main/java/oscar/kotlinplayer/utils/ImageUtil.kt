package oscar.kotlinplayer.utils

import android.graphics.Bitmap
import com.wingjay.blurimageviewlib.FastBlurUtil

/**
 * Created by oscar on 2017/9/2.
 */
object ImageUtil {

    fun doCut(source: Bitmap, width: Int, height: Int): Bitmap {
        if(source.width * height == width * source.height){
            return source
        }else if(source.width * height > width * source.height){
            var targetWidth = width * source.height / height
            var offsetX = (source.width - targetWidth) / 2
            return Bitmap.createBitmap(source, offsetX, 0, targetWidth, source.height, null, false)
        }else{
            var targetHeight = height * source.width / width
            var offsetY = (source.height - targetHeight) / 2
            return Bitmap.createBitmap(source, 0, offsetY, source.width, targetHeight, null, false)
        }
    }

    fun doBlur(source: Bitmap, radius: Int): Bitmap{
        return FastBlurUtil.doBlur(source, radius, true)
    }
}