package com.rudyrachman16.mtcataloguev2.views.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rudyrachman16.mtcataloguev2.BuildConfig
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.data.api.models.Genre
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowDetail
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.databinding.ActivityDetailBinding
import com.rudyrachman16.mtcataloguev2.views.favorite.tabs.FavoriteTabViewModel
import com.rudyrachman16.mtcataloguev2.views.home.MovieAdapter
import com.rudyrachman16.mtcataloguev2.views.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_FAV = "com.rudyrachman16.mtcatalogue.views.detail.key_fav"
        const val KEY_MOV = "com.rudyrachman16.mtcatalogue.views.detail.key_mov"
        const val KEY_TV = "com.rudyrachman16.mtcatalogue.views.detail.key_tv"
        const val KEY_POS = "com.rudyrachman16.mtcatalogue.views.detail.key_pos"
        const val KEY_NUM = "com.rudyrachman16.mtcatalogue.views.detail.key_num"
        const val KEY_TITLE = "com.rudyrachman16.mtcatalogue.views.detail.key_title"
    }

    private var position = -1

    private var binding: ActivityDetailBinding? = null
    private val bind get() = binding!!

    private lateinit var movieEntities: MovieEntities
    private lateinit var tvShowEntities: TvShowEntities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)
        title = intent.extras?.getString(KEY_TITLE)
        val color =
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) resources.getColor(R.color.white)
            else resources.getColor(R.color.white, null)
        bind.detCollapse.apply {
            setExpandedTitleColor(color)
            setExpandedTitleTextAppearance(R.style.Text_H1)
            setStatusBarScrimColor(color)
            setCollapsedTitleTextColor(color)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.extras?.getString(KEY_MOV)
        val tvShow = intent.extras?.getString(KEY_TV)
        position = intent.getIntExtra(KEY_POS, -1)
        if (movie != null || tvShow != null) {
            if (movie != null) {
                val viewModel: DetailViewModel by viewModels {
                    ViewModelFactory.getInstance(application, movie)
                }
                viewModel.getDetail<MovieDetail>(DetailViewModel.MOVIE).observe(this) {
                    initMovie(it)
                }
            } else if (tvShow != null) {
                val viewModel: DetailViewModel by viewModels {
                    ViewModelFactory.getInstance(application, tvShow)
                }
                viewModel.getDetail<TvShowDetail>(DetailViewModel.TV_SHOW).observe(this) {
                    initTvShow(it)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initTvShow(tvShow: TvShowDetail) {
        tvShowEntities = intent?.extras?.getParcelable(KEY_FAV)!!
        initAll(
            tvShow.title, tvShow.imgUrl, tvShow.bgUrl, tvShow.desc,
            tvShow.rating, tvShow.voter, tvShow.genres, tvShowEntities.favorite
        )
        bind.detType.text = getString(
            R.string.tv_show,
            if (tvShow.episodes == 1) "1 episode" else "${tvShow.episodes} episodes",
            if (tvShow.seasons == 1) "1 season" else "${tvShow.seasons} seasons",
            tvShow.status
        )
        bind.detAirTitle.text = getString(R.string.first_air)
        bind.detDate.text = MovieAdapter.parseDate(tvShow.date)
        initCompany(tvShow)

        bind.detFav.setOnClickListener {
            setImage(!tvShowEntities.favorite)
            val viewModel: FavoriteTabViewModel by viewModels {
                ViewModelFactory.getInstance(application)
            }
            viewModel.setFavoriteTv(tvShowEntities)
            Toast.makeText(
                applicationContext,
                if (!tvShowEntities.favorite) "Added to Favorite" else "Removed from Favorite",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initMovie(movie: MovieDetail) {
        movieEntities = intent?.extras?.getParcelable(KEY_FAV)!!
        initAll(
            movie.title, movie.imgUrl, movie.bgUrl, movie.desc,
            movie.rating, movie.voter, movie.genres, movieEntities.favorite
        )
        bind.detType.text = getString(R.string.movie)
        bind.detAirTitle.text = getString(R.string.release_date)
        bind.detDate.text = MovieAdapter.parseDate(movie.date)
        initCompany(movie)

        bind.detFav.setOnClickListener {
            setImage(!movieEntities.favorite)
            val viewModel: FavoriteTabViewModel by viewModels {
                ViewModelFactory.getInstance(application)
            }
            viewModel.setFavoriteMov(movieEntities)
            Toast.makeText(
                applicationContext,
                if (!movieEntities.favorite) "Added to Favorite" else "Removed from Favorite",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initAll(
        title: String, imgURL: String, bgURL: String, desc: String,
        rating: Double, voter: Int, genres: ArrayList<Genre>, favorite: Boolean
    ) {
        bind.detLoading.visibility = View.GONE
        bind.detAppBarLayout.visibility = View.VISIBLE
        bind.detExpand.visibility = View.VISIBLE
        bind.detNested.visibility = View.VISIBLE
        bind.detRating.text = rating.toString()
        bind.detUserRate.text = resources.getQuantityString(R.plurals.user_rate, voter, voter)
        Glide
            .with(applicationContext).load(BuildConfig.BASE_IMAGE + imgURL)
            .apply(RequestOptions.placeholderOf(R.drawable.loading))
            .error(R.drawable.error).into(bind.detImg)
        Glide
            .with(applicationContext).load(BuildConfig.BASE_IMAGE + bgURL)
            .apply(RequestOptions.placeholderOf(R.drawable.loading))
            .error(R.drawable.error).into(bind.detBg)

        setImage(favorite)

        bind.detShare.setOnClickListener {
            MovieAdapter.share(
                this, position, intent.getIntExtra(KEY_NUM, 0),
                title, rating, voter
            )
        }
        var open = false
        bind.detExpand.setOnClickListener {
            val margin = resources.getDimension(R.dimen.halfNormal)
            if (!open) {
                bind.apply {
                    detFav.show()
                    detShare.show()
                    detShare.translationX = -(detExpand.customSize + margin)
                    detFav.translationX =
                        -(detExpand.customSize + detShare.customSize + (margin * 2))
                    open = true
                }
            } else {
                bind.apply {
                    detFav.hide()
                    detShare.hide()
                    detShare.translationX = 0F
                    detFav.translationX = 0F
                    open = false
                }
            }
        }

        bind.detOverview.text = getString(R.string.overview_in_english_only, title)
        bind.detDesc.text = desc
        genres.forEachIndexed { index, it ->
            bind.detGenres.append("${index + 1}. ${it.name}\n")
        }
    }

    private fun setImage(favorite: Boolean) {
        bind.detFav.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                if (favorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite
            )
        )
    }

    private fun <T> initCompany(model: T) {
        val detailAdapter = DetailAdapter()
        var available = false
        when (model) {
            is MovieDetail -> {
                if (model.companies.isNotEmpty()) {
                    available = true
                    detailAdapter.setList(model.companies)
                }
            }
            is TvShowDetail -> {
                if (model.companies.isNotEmpty()) {
                    available = true
                    detailAdapter.setList(model.companies)
                }
            }
        }
        if (available) {
            bind.detCompanies.apply {
                val manager = LinearLayoutManager(applicationContext)
                manager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = manager
                adapter = detailAdapter
                setHasFixedSize(true)
            }
        } else bind.detCompany.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}