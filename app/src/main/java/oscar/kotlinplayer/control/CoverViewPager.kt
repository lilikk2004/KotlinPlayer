package oscar.kotlinplayer.control

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import oscar.kotlinplayer.adapter.CoverPagerAdapter

/**
 * Created by oscar on 2016/6/10.
 */
class CoverViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var coverAdapter: CoverPagerAdapter? = null
    set(value) {super.setAdapter(value)}

/*    fun setAdapter(adapter: CoverPagerAdapter) {
        mCoverAdapter = adapter
        super.setAdapter(adapter)
    }*/

    fun updateCurrentCoverRotate() {
        val currentIndex = currentItem
        if (coverAdapter == null) {
            Log.d(TAG, "Adapter is null")
            return
        }
        val coverImg = coverAdapter!!.coverViewList[currentIndex]
        coverImg.updateCoverRotate()
    }

    companion object {
        val TAG = "CoverViewPager"
    }
}
