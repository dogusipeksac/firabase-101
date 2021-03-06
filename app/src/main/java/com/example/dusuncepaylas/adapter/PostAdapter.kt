package com.example.dusuncepaylas.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dusuncepaylas.R
import com.example.dusuncepaylas.model.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter(private val postList: ArrayList<Post>,private  val context:Context) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(context)
        val view = inflate.inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.postDescribeText.text = postList[position].postDescribe
        holder.itemView.userNameText.text = postList[position].userName
        if(postList[position].downloadUrl!=null){
            holder.itemView.postImage.visibility=View.VISIBLE
            Glide.with(context).load(postList[position].downloadUrl).into(holder.itemView.postImage)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}