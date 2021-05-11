package com.rudyrachman16.mtcataloguev2.views.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rudyrachman16.mtcataloguev2.BuildConfig
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.databinding.PerViewBinding
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabAdapter
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter(
    private val shareCallback: (
        number: Int, title: String, rating: Double, voter: Int
    ) -> Unit, private val clickCallback: (number: Int, movie: MovieEntities) -> Unit
) : PagedListAdapter<MovieEntities, MovieAdapter.ViewHolder>(CALLBACK) {
    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<MovieEntities>() {
            override fun areItemsTheSame(oldItem: MovieEntities, newItem: MovieEntities): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieEntities,
                newItem: MovieEntities
            ): Boolean =
                oldItem == newItem
        }

        fun share(
            activity: Activity, position: Int, number: Int,
            title: String, rating: Double, voter: Int
        ) {
            ShareCompat.IntentBuilder.from(activity).apply {
                setType("text/plain")
                setChooserTitle("Share to?")
                setText(
                    "Popular ${activity.applicationContext.getString(TabAdapter.TAB_TITLE[position])} #$number\n" +
                            "Title: $title\n" +
                            "Have $rating from $voter users"
                )
                startChooser()
            }
        }

        fun parseDate(oldStringDate: String): String {
            val oldFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val oldDate = oldFormat.parse(oldStringDate)!!
            val newFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return newFormat.format(oldDate)
        }
    }

    inner class ViewHolder(private val bind: PerViewBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(model: MovieEntities) {
            itemView.setOnClickListener {
                clickCallback(adapterPosition + 1, model)
            }
            initiate(model.id, model.title, model.imgUrl, model.rating, model.voter)
            bind.perAirTitle.text = itemView.context.getString(R.string.release_date)
            bind.perDate.text = parseDate(model.date)
        }

        private fun initiate(
            id: String, title: String, imgURL: String, rating: Double, voter: Int
        ) {
            bind.perID.text = id
            bind.perTitle.text = title
            bind.perRating.text = rating.toString()
            bind.perUserRate.text =
                itemView.context.resources.getQuantityString(R.plurals.user_rate, voter, voter)
            bind.perShare.setOnClickListener {
                shareCallback(adapterPosition + 1, title, rating, voter)
            }
            Glide
                .with(itemView.context).load(BuildConfig.BASE_IMAGE + imgURL)
                .apply(RequestOptions.placeholderOf(R.drawable.loading))
                .apply(RequestOptions.overrideOf(100, 200))
                .error(R.drawable.error).into(bind.perImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(PerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model!!)
    }

    fun getSwiped(position: Int): MovieEntities = getItem(position)!!
}