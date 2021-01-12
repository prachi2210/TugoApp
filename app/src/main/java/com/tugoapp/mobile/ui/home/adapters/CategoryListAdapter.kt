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

class CategoryListAdapter(private val context: Context,
                          private val list: ArrayList<CategoryDetailModel>,
                          private val cellClickListener: OnListItemClickListener,
                            private var selectedCategoryIndex : Int
) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    init {
        cellClickListener.onListItemClick(selectedCategoryIndex)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.txtCategoryName)
        val llCategoryName: LinearLayout = view.findViewById(R.id.llCategoryName)
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
        holder.categoryName.text = data.name

        if(position == selectedCategoryIndex) {
            holder.categoryName.setTextColor(context.getColor(R.color.colorPrimary))
            holder.llCategoryName.background = context.getDrawable(R.drawable.rounded_border_category_selected)

        } else {
            holder.categoryName.setTextColor(context.getColor(R.color.color999999))
            holder.llCategoryName.background = context.getDrawable(R.drawable.rounded_border_category_unselected)
        }

        holder.categoryName.setOnClickListener {
            cellClickListener.onListItemClick(position)
            selectedCategoryIndex = position;
            notifyDataSetChanged()
        }
    }
}