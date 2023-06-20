package com.example.liargame.list

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.liargame.R
import kotlin.coroutines.coroutineContext

class WordSelectAdapter(val mode : String, val list : ArrayList<String>) :
    RecyclerView.Adapter<WordSelectAdapter.ViewHolder>() {

    private var mMode : String? = null
    private var mList : ArrayList<String>? = null
    private lateinit var mItemClickListener : OnItemClickListener

    init {
        mMode = mode
        mList = list
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int, mode : String)
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val listItemTxt : TextView = view.findViewById(R.id.list_item_txt)

        fun bind(item : String) {
            listItemTxt.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList != null) {
            holder.bind(mList!![position])

            holder.itemView.setOnClickListener {
                Log.d("[PopupFragment]", "itemView onClick -> position : $position")
                mItemClickListener.onItemClick(it, position, mMode!!)
            }
        }
    }

    override fun getItemCount(): Int {
        if (mList != null) {
            return mList!!.size
        }
        return -1
    }
}