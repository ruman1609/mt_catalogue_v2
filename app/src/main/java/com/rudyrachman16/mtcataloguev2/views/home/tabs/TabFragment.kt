package com.rudyrachman16.mtcataloguev2.views.home.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudyrachman16.mtcataloguev2.databinding.FragmentTabBinding
import com.rudyrachman16.mtcataloguev2.utils.IdlingResource
import com.rudyrachman16.mtcataloguev2.views.detail.DetailActivity
import com.rudyrachman16.mtcataloguev2.views.home.MovieAdapter
import com.rudyrachman16.mtcataloguev2.views.home.TvShowAdapter
import com.rudyrachman16.mtcataloguev2.views.viewmodel.ViewModelFactory

class TabFragment : Fragment() {
    companion object {
        private const val POS_KEY = "com.rudyrachman16.mtcatalogue.views.home.tabs.pos_key"

        @JvmStatic
        fun newInstance(position: Int): TabFragment = TabFragment().apply {
            arguments = Bundle().apply { putInt(POS_KEY, position) }
        }
    }

    private var binding: FragmentTabBinding? = null
    private val bind get() = binding!!
    private lateinit var viewModel: TabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onResume() {
        super.onResume()
        val position = arguments?.getInt(POS_KEY)
        if (position != null) {
            viewModel = ViewModelProvider(
                requireActivity(), ViewModelFactory.getInstance(requireActivity().application)
            )[TabViewModel::class.java]
            viewModel.setSort()
            viewModel.getSort().observe(requireActivity()) {
                when (position) {
                    0 -> {
                        val rvAdapter = MovieAdapter({ number, title, rating, voter ->
                            MovieAdapter.share(
                                requireActivity(),
                                position,
                                number,
                                title,
                                rating,
                                voter
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
                                requireActivity(),
                                position,
                                number,
                                title,
                                rating,
                                voter
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

    private fun <T> makeRV(rvAdapter: PagedListAdapter<T, *>) {
        if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
        bind.tabLoading.visibility = View.GONE
        bind.tabRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}