package oscar.kotlinplayer.control

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import oscar.kotlinplayer.R
import oscar.kotlinplayer.bean.Song
import oscar.kotlinplayer.utils.getAlbumArt

/**
 * Created by oscar on 2016/6/1.
 */
class CoverImg : View {
    companion object {
        val TAG = "CoverImg"

        private val VELOCITY = 1
        private val SLOW = 2
        private val ROTATE_DELAY = 20.toLong()
    }
    internal var playImage: Bitmap? = null
    internal var drawX: Int = 0
    internal var drawY: Int = 0
    private var rotateDegrees = 0

    private var centerX: Float = 0.toFloat()
    private var centerY: Float = 0.toFloat()
    private var needMeasure = false

    var song:Song = Song()
    var isRotate = false
    var rotateHandler = Handler(Looper.getMainLooper())

    var rotateFun = {}


    constructor(context: Context) : super(context) {
        rotateFun = {
            if(isRotate){
                updateCoverRotate()
                postDelayed(rotateFun, ROTATE_DELAY)
            }
        }
    }


    constructor(context: Context, paramAttributeSet: AttributeSet) : super(context, paramAttributeSet) {
        rotateFun = {
            if(isRotate){
                updateCoverRotate()
                postDelayed(rotateFun, ROTATE_DELAY)
            }
        }
    }

    fun makeDrawCover() {
        val width = width
        val height = height
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()

        val playLength: Int
        if (width > height) {
            playLength = height
            drawX = (width - height) / 2
            drawY = 0
        } else {
            playLength = width
            drawX = 0
            drawY = (height - width) / 2
        }

        val playDrawRect = Rect(0, 0, playLength, playLength)
        val imgDrawRect = Rect(playLength / 6, playLength / 6, playLength * 5 / 6, playLength * 5 / 6)


        val albumArt = getAlbumArt(song.albumId, context)
        var bitmap = BitmapFactory.decodeFile(albumArt)

        val imgSrcRect: Rect
        if (bitmap.height > bitmap.width) {
            imgSrcRect = Rect(0, (bitmap.height - bitmap.width) / 2, bitmap.width, (bitmap.height + bitmap.width) / 2)
        } else {
            imgSrcRect = Rect((bitmap.width - bitmap.height) / 2, 0, (bitmap.width + bitmap.height) / 2, bitmap.height)
        }
        val play_disc = BitmapFactory.decodeResource(context.resources, R.drawable.play_disc)

        val playSrc = Rect(0, 0, play_disc.width, play_disc.height)

        playImage = Bitmap.createBitmap(playLength, playLength, Bitmap.Config.ARGB_8888)

        val paint = Paint()
        val imgCanvas = Canvas(playImage!!)

        imgCanvas.drawBitmap(bitmap, imgSrcRect, imgDrawRect, paint)
        imgCanvas.drawBitmap(play_disc, playSrc, playDrawRect, paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (needMeasure) {
            Log.d(TAG, "onDraw mWidth:" + width + " mHeight:" + height + " " + this)
            makeDrawCover()
            needMeasure = false
        }

        if (playImage == null) {
            return
        }

        val paint = Paint()
        //paint.setAntiAlias(true);

        canvas.rotate(rotateDegrees * 1.0.toFloat() / SLOW, centerX, centerY)
        canvas.drawBitmap(playImage!!, drawX.toFloat(), drawY.toFloat(), paint)
        //canvas.drawBitmap(mPlay_disc, playSrcRect, playDrawRect, paint);

    }

    fun updateCoverRotate() {
        rotateDegrees += VELOCITY
        rotateDegrees %= 360 * SLOW
        postInvalidate()
    }

    fun start(){
        isRotate = true
        postDelayed(rotateFun, 200)
    }

    fun stop(){
        isRotate = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        needMeasure = true
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
