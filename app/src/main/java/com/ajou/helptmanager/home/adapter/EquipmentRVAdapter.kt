package com.ajou.helptmanager.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.ItemEquipmentBinding
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.fragment.EquipmentEditBottomSheetFragment
import com.ajou.helptmanager.home.view.fragment.EquipmentListFragment

class EquipmentRVAdapter(val context: Context, var list: List<Equipment>, val type : String, val link: AdapterToFragment): RecyclerView.Adapter<EquipmentRVAdapter.ViewHolder>(){

    inner class ViewHolder(val binding:ItemEquipmentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Equipment) {
            when(type) {
                "search" -> {
                    binding.more.visibility = View.VISIBLE
                    binding.exerciseInfo.visibility = View.VISIBLE
                    binding.name.text = item.equipmentName
                    val exerciseInfo = String.format(context.resources.getString(R.string.equip_set),item.defaultWeight,item.defaultCount,item.defaultSet)
                    binding.exerciseInfo.text = exerciseInfo

                    binding.more.setOnClickListener {
                        link.getSelectedItem(item, position)
                    }
                }
                "add" -> {
                    binding.addBtn.visibility = View.VISIBLE
                    binding.name.text = item.equipmentName
                    binding.addBtn.setOnClickListener {
                        link.getSelectedItem(item, position)
                    }
                }
            }

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipmentRVAdapter.ViewHolder {
        val binding = ItemEquipmentBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EquipmentRVAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<Equipment>) {
        list = newList
        notifyDataSetChanged()
    }
}