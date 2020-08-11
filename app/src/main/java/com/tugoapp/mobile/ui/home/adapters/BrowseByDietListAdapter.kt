package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        Glide.with(context)
                .load(data.imagePath)
                .centerCrop()
                .into(holder.image)

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}