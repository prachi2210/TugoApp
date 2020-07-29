package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class SearchHomeListAdapter(private val context: Context,
                            private val list: ArrayList<Int>,
                            private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<SearchHomeListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgBrowseByDiet)
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
        holder.image.setImageDrawable(ContextCompat.getDrawable(context, data))

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}