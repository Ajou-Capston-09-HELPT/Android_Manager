package com.ajou.helptmanager.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.setOnSingleClickListener

class NoticeAdapter(val listener: OnItemClickListener): ListAdapter<NoticeResponse, NoticeAdapter.NoticeViewHolder>(
    NoticeDiffCallback
) {
    interface  OnItemClickListener {
        fun onMoreButtonClicked(noticeId: Int, title: String, content: String, createAt: String)
        fun onDetailButtonClicked(noticeId: Int, title: String, content: String, createAt: String)
    }

    class NoticeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.noticeTitleTV)
        val createAt: TextView = view.findViewById(R.id.noticeCreateDateTV)
        val moreButton: ImageView = view.findViewById(R.id.noticeMoreButtonIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice = getItem(position)
        holder.title.text = notice.title
        holder.createAt.text = notice.createAt
        holder.moreButton.setOnSingleClickListener {
            listener.onMoreButtonClicked(notice.noticeId, notice.title, notice.content, notice.createAt)
        }
        holder.itemView.setOnSingleClickListener {
            listener.onDetailButtonClicked(notice.noticeId, notice.title, notice.content, notice.createAt)
        }

    }

    companion object NoticeDiffCallback : DiffUtil.ItemCallback<NoticeResponse>() {
        override fun areItemsTheSame(oldItem: NoticeResponse, newItem: NoticeResponse): Boolean {
            return oldItem.noticeId == newItem.noticeId
        }

        override fun areContentsTheSame(oldItem: NoticeResponse, newItem: NoticeResponse): Boolean {
            return oldItem == newItem
        }
    }


}