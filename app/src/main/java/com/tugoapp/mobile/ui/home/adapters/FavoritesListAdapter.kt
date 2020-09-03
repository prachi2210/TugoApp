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
import com.tugoapp.mobile.data.remote.model.response.FavoriteModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener


class FavoritesListAdapter(private val context: Context,
                           private val list: ArrayList<FavoriteModel>,
                           private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameFav)
        val image: ImageView = view.findViewById(R.id.imgFavoritesProvider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorites,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.companyName
        Glide.with(context)
                .load(data.imagePath)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(holder.image)

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}