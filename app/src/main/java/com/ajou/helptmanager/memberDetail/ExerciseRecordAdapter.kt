package com.ajou.helptmanager.memberDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R

class ExerciseRecordAdapter() : ListAdapter<ExerciseRecord,ExerciseRecordAdapter.ExerciseRecordViewHolder>(
    ExerciseRecordDiffCallback
) {

    class ExerciseRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvExerciseRecordName)
        val reps: TextView = view.findViewById(R.id.tvExerciseRecordReps)
        val sets: TextView = view.findViewById(R.id.tvExerciseRecordSets)
        val time: TextView = view.findViewById(R.id.tvExerciseRecordTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseRecordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_record, parent, false)
        return ExerciseRecordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseRecordViewHolder, position: Int) {
        val exerciseRecord = getItem(position)
        holder.name.text = "${exerciseRecord.equipmentName}"
        holder.reps.text = holder.itemView.context.getString(R.string.reps_format, exerciseRecord.count)
        holder.sets.text = holder.itemView.context.getString(R.string.sets_format, exerciseRecord.setNumber)
        holder.time.text = exerciseRecord.recordTime
    }

    companion object ExerciseRecordDiffCallback : DiffUtil.ItemCallback<ExerciseRecord>() {
        override fun areItemsTheSame(oldItem: ExerciseRecord, newItem: ExerciseRecord): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ExerciseRecord, newItem: ExerciseRecord): Boolean {
            return oldItem == newItem
        }
    }
}
