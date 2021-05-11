package com.rudyrachman16.mtcataloguev2.views.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rudyrachman16.mtcataloguev2.BuildConfig
import com.rudyrachman16.mtcataloguev2.R
import com.rudyrachman16.mtcataloguev2.data.api.models.Company
import com.rudyrachman16.mtcataloguev2.databinding.PerCompanyBinding

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bind = PerCompanyBinding.bind(itemView)
        fun bind(company: Company) {
            bind.detCompanyName.text = company.name
            Glide
                .with(itemView.context).load(BuildConfig.BASE_IMAGE + company.imgURL)
                .apply(RequestOptions.placeholderOf(R.drawable.loading))
                .apply(RequestOptions.overrideOf(150))
                .error(R.drawable.error).into(bind.detCompany)
        }
    }

    private val list = ArrayList<Company>()
    fun setList(list: ArrayList<Company>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.per_company, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = list[position]
        holder.bind(company)
    }

    override fun getItemCount(): Int = list.size
}