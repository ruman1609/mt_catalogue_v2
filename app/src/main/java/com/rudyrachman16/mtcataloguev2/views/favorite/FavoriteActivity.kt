package com.rudyrachman16.mtcataloguev2.views.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.databinding.ActivityFavoriteBinding
import com.rudyrachman16.mtcataloguev2.utils.QuerySort
import com.rudyrachman16.mtcataloguev2.views.favorite.tabs.FavoriteTabAdapter
import com.rudyrachman16.mtcataloguev2.views.favorite.tabs.FavoriteTabViewModel
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabAdapter
import com.rudyrachman16.mtcataloguev2.views.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private val viewModel: FavoriteTabViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar.root)
        title = "Favorite"
        viewModel.setSort()
        val tabAdapter = FavoriteTabAdapter(this)
        bind.favViewPager.adapter = tabAdapter
        TabLayoutMediator(bind.favTabLayout, bind.favViewPager) { tab, position ->
            tab.text = getText(TabAdapter.TAB_TITLE[position])
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        if (menu != null) setSortIcon()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.favoriteClick).isVisible = false
        this.menu = menu
        setSortIcon()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setSortIcon() {
        when (QuerySort.getPos()) {
            0 -> menu?.findItem(R.id.sort)?.icon =
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_dsc)
            1 -> menu?.findItem(R.id.sort)?.icon =
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_asc)
            2 -> menu?.findItem(R.id.sort)?.icon =
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_ran)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        QuerySort.setPos()
        viewModel.setSort()
        setSortIcon()
        return true
    }
}