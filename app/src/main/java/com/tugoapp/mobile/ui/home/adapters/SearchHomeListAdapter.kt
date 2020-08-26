package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.ProviderModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class SearchHomeListAdapter(private val context: Context,
                            private val list: ArrayList<ProviderModel>,
                            private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<SearchHomeListAdapter.ViewHolder>(), Filterable {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgBrowseByProvider)
        val name: TextView = view.findViewById(R.id.name)
        val planDetail: TextView = view.findViewById(R.id.planDetail)
        val price: TextView = view.findViewById(R.id.price)
        val imgCompanyLogo: ImageView = view.findViewById(R.id.imgCompanyLogo)
        val txtOffer: TextView = view.findViewById(R.id.txtOfferProvider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search_home,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.companyName
        holder.price.text = data.startingFrom
        holder.planDetail.text = String.format(context.getString(R.string.txt_meal_plan_available),data.numOfPlans)

        if(data.offer.isNullOrBlank()) {
            holder.txtOffer.visibility = View.GONE
        } else {
            holder.txtOffer.visibility = View.VISIBLE
            holder.txtOffer.text = data.offer
        }

        Glide.with(context)
                .load(data.featuredImage)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(holder.image)

        Glide.with(context)
                .load(data.imagePath)
                .centerCrop()
                .into(holder.imgCompanyLogo)

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                filterResults.values = null
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            }
        }
    }
}