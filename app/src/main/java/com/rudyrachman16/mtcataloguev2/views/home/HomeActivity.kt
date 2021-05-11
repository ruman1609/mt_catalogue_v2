package com.rudyrachman16.mtcataloguev2.views.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.databinding.ActivityHomeBinding
import com.rudyrachman16.mtcataloguev2.utils.QuerySort
import com.rudyrachman16.mtcataloguev2.views.favorite.FavoriteActivity
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabAdapter
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabViewModel
import com.rudyrachman16.mtcataloguev2.views.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private val bind get() = binding!!
    private val viewModel: TabViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar.root)
        val tabAdapter = TabAdapter(this)
        viewModel.setSort()
        bind.viewPager.adapter = tabAdapter
        TabLayoutMediator(bind.tabLayout, bind.viewPager) { tab, position ->
            tab.text = getText(TabAdapter.TAB_TITLE[position])
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        if (menu != null) setSortIcon()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
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
        if (item.itemId == R.id.favoriteClick) {
            startActivity(Intent(this, FavoriteActivity::class.java))
            return true
        }
        QuerySort.setPos()
        setSortIcon()
        viewModel.setSort()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}