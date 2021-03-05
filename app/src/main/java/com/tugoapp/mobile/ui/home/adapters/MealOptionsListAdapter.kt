package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class MealOptionsListAdapter(private val context: Context,
                             private val list: ArrayList<MealOptionsModel>,
                             private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<MealOptionsListAdapter.ViewHolder>() {

    var mLastCheckedPosition : Int = -1

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val mealOptionPrice: TextView = view.findViewById(R.id.mealOptionPrice)
        val mealOptionTitle: TextView = view.findViewById(R.id.txtMealOptionTitle)
        val isMealSelected: RadioButton = view.findViewById(R.id.cbMealOption)
        val llMenu : LinearLayout = view.findViewById(R.id.llMenuMealOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_meal_options,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.mealOptionTitle.text = data.noOfDays
        holder.mealOptionPrice.text = data.price
        holder.isMealSelected.isChecked = position == mLastCheckedPosition
        holder.llMenu.setOnClickListener {
            mLastCheckedPosition = position
            cellClickListener.onListItemClick(position)
            notifyDataSetChanged()
        }
    }
}