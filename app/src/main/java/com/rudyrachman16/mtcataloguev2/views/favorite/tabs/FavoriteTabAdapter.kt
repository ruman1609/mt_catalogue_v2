package com.rudyrachman16.mtcataloguev2.views.favorite.tabs

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rudyrachman16.mtcataloguev2.R

class FavoriteTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    companion object {
        @StringRes
        val TAB_TITLE = intArrayOf(R.string.movies, R.string.tv_shows)
    }

    override fun getItemCount(): Int = TAB_TITLE.size

    override fun createFragment(position: Int): Fragment =
        FavoriteFragment.newInstance(position)
}