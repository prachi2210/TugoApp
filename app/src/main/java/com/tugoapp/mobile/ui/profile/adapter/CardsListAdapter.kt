package com.tugoapp.mobile.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class CardsListAdapter(private val context: Context,
                       private val list: ArrayList<String>,
                       private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<CardsListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleAddress: TextView = view.findViewById(R.id.txtCardNumber)
        val imgChecked: ImageView = view.findViewById(R.id.imgIsChecked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_card,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if(position == 0) {
            holder.imgChecked.visibility = View.VISIBLE
        } else {
            holder.imgChecked.visibility = View.GONE
        }
        
        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}