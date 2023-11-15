package com.example.moviesapplication.searchfragment

import android.content.res.Resources
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.SinglepageDesignBinding
import com.google.android.material.color.MaterialColors.getColor

class PagesAdapter (var pagesNum:Int?):Adapter<PagesAdapter.PagerViewHolder>() {

    var onPageNumClickListener:OnPageNumClickListener?=null

    class PagerViewHolder(val pageBinding:SinglepageDesignBinding):ViewHolder(pageBinding.root)



    fun updateNum(pagesNum: Int?){
        this.pagesNum=pagesNum
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val PageBinding=SinglepageDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(PageBinding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        if(pagesNum?:1 >= 100){
            holder.pageBinding.pagenumber.text= "${position+1}"
        }else{
            holder.pageBinding.pagenumber.text= "${position+1}"
        }
        holder.pageBinding.constran.setOnClickListener({
            onPageNumClickListener?.OnPageNumClick(position)
        })
    }
    interface OnPageNumClickListener{
        fun OnPageNumClick(position: Int)
    }

    override fun getItemCount(): Int {
        return pagesNum?:1
    }

}