package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener


class BrowseByDietListAdapter(private val context: Context,
                              private val list: ArrayList<CategoryDetailModel>,
                              private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<BrowseByDietListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtCategory)
        val offer: TextView = view.findViewById(R.id.txtOffer)
        val image: ImageView = view.findViewById(R.id.imgBrowseByDiet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_browse_by_diet,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.name
        if(data.offer.isNullOrBlank()) {
            holder.offer.visibility = View.GONE
        } else {
            holder.offer.visibility = View.VISIBLE
            holder.offer.text = data.offer
        }
        Glide.with(context)
                .load(data.imagePath)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(holder.image)

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}