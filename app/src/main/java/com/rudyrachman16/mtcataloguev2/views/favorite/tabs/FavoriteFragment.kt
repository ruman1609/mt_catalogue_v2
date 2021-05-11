package com.rudyrachman16.mtcataloguev2.views.favorite.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.databinding.FragmentTabBinding
import com.rudyrachman16.mtcataloguev2.views.detail.DetailActivity
import com.rudyrachman16.mtcataloguev2.views.home.MovieAdapter
import com.rudyrachman16.mtcataloguev2.views.home.TvShowAdapter
import com.rudyrachman16.mtcataloguev2.views.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {
    companion object {
        private const val POS_KEY = "com.rudyrachman16.mtcataloguev2.views.favorite.tabs.pos_key"

        @JvmStatic
        fun newInstance(position: Int): FavoriteFragment = FavoriteFragment().apply {
            arguments = Bundle().apply { putInt(POS_KEY, position) }
        }
    }

    private var binding: FragmentTabBinding? = null
    private val bind get() = binding!!

    private lateinit var viewModel: FavoriteTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(POS_KEY)
        if (position != null) {
            viewModel = ViewModelProvider(
                requireActivity(), ViewModelFactory.getInstance(requireActivity().application)
            )[FavoriteTabViewModel::class.java]
            viewModel.getSort().observe(requireActivity()) {
                when (position) {
                    0 -> {
                        val rvAdapter = MovieAdapter({ number, title, rating, voter ->
                            MovieAdapter.share(
                                requireActivity(), position, number,
                                title, rating, voter
                            )
                        }) { number, it1 ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    DetailActivity::class.java
                                ).apply {
                                    putExtra(DetailActivity.KEY_MOV, it1.id)
                                    putExtra(DetailActivity.KEY_POS, position)
                                    putExtra(DetailActivity.KEY_NUM, number)
                                    putExtra(DetailActivity.KEY_TITLE, it1.title)
                                    putExtra(DetailActivity.KEY_FAV, it1)
                                })
                        }
                        viewModel.getMovies().observe(requireActivity()) {
                            rvAdapter.submitList(it)
                            makeRV(rvAdapter)
                        }
                    }
                    1 -> {
                        val rvAdapter = TvShowAdapter({ number, title, rating, voter ->
                            MovieAdapter.share(
                                requireActivity(), position, number,
                                title, rating, voter
                            )
                        }) { number, it1 ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    DetailActivity::class.java
                                ).apply {
                                    putExtra(DetailActivity.KEY_TV, it1.id)
                                    putExtra(DetailActivity.KEY_POS, position)
                                    putExtra(DetailActivity.KEY_NUM, number)
                                    putExtra(DetailActivity.KEY_TITLE, it1.title)
                                    putExtra(DetailActivity.KEY_FAV, it1)
                                })
                        }
                        viewModel.getTvShow().observe(requireActivity()) {
                            rvAdapter.submitList(it)
                            makeRV(rvAdapter)
                        }
                    }
                }
            }
        }
    }

    fun <T> getSwipeCallback(model: T): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (model) {
                    is MovieAdapter -> {
                        val movie = model.getSwiped(position)
                        viewModel.setFavoriteMov(movie)
                        makeSnackbar(movie)
                    }
                    is TvShowAdapter -> {
                        val tvShow = model.getSwiped(position)
                        viewModel.setFavoriteTv(tvShow)
                        makeSnackbar(tvShow)
                    }
                }
            }
        }
    }

    private fun <T> makeRV(rvAdapter: PagedListAdapter<T, *>) {
        bind.tabLoading.visibility = View.GONE
        bind.tabRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }
        object :
            ItemTouchHelper(getSwipeCallback(rvAdapter)) {}.attachToRecyclerView(bind.tabRecycler)
    }

    private fun <T> makeSnackbar(model: T) {
        Snackbar.make(view as View, "Undo Action?", Snackbar.LENGTH_LONG).setAction("Undo") {
            when (model) {
                is MovieEntities -> viewModel.setFavoriteMov(model)
                is TvShowEntities -> viewModel.setFavoriteTv(model)
            }
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}