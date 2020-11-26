package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class PlanListAdapter(private val context: Context,
                      private val list: ArrayList<String>,
                      private val cellClickListener: OnListItemClickListener,
                      private var selectedCategoryIndex : Int
) : RecyclerView.Adapter<PlanListAdapter.ViewHolder>() {

    init {
        cellClickListener.onListItemClick(selectedCategoryIndex)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.txtCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category_list,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.categoryName.text = data

        if(position == selectedCategoryIndex) {
            holder.categoryName.setTextColor(context.getColor(R.color.colorPrimary))
        } else {
            holder.categoryName.setTextColor(context.getColor(R.color.color999999))
        }

        holder.categoryName.setOnClickListener {
            cellClickListener.onListItemClick(position)
            selectedCategoryIndex = position;
            notifyDataSetChanged()
        }
    }
}