package com.ajou.helptmanager.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.databinding.ItemNoticeBinding
import com.ajou.helptmanager.home.model.EntryLog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EntryLogRVAdapter(val context: Context, var list: List<EntryLog>): RecyclerView.Adapter<EntryLogRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNoticeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: EntryLog) {
            val text = "${item.userName}님이 출입하셨습니다."
            binding.noticeTitleTV.text = text

            // 문자열을 LocalDateTime 객체로 변환
            val localDateTime = LocalDateTime.parse(item.entryTime, DateTimeFormatter.ISO_DATE_TIME)

            val format = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.KOREAN)
            val formattedEntryTime = localDateTime.format(format)
            binding.noticeCreateDateTV.text = formattedEntryTime
            binding.noticeMoreButtonCL.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntryLogRVAdapter.ViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryLogRVAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<EntryLog>){
        list = newList
        notifyDataSetChanged()
    }

    fun filterList(originList: List<EntryLog>, name: String) {
        list = originList.filter { it.userName.contains(name) }
        Log.d("filteredList",list.toString())
        notifyDataSetChanged()
    }
}