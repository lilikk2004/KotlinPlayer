package oscar.kotlinplayer.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import oscar.kotlinplayer.bean.Song
import oscar.kotlinplayer.control.CoverImg

/**
 * Created by oscar on 2016/5/7.
 */
class CoverPagerAdapter(var context: Context) : PagerAdapter() {

    val coverViewList: MutableList<CoverImg> = mutableListOf()//view数组
    var songList: MutableList<Song> = mutableListOf()
    set(value) {
        coverViewList.clear()
        for (song in value) {
            val coverImg = CoverImg(context)
            coverImg.song = song
            coverViewList.add(coverImg)
        }
    }

    init {
    }

    override fun getCount(): Int {
        return coverViewList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        // TODO Auto-generated method stub
        return view === `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(coverViewList[position])//删除页卡
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {  //这个方法用来实例化页卡
        container.addView(coverViewList[position], 0)//添加页卡
        return coverViewList[position]
    }

    companion object {
        val TAG = "CoverPagerAdapter"
    }
}
