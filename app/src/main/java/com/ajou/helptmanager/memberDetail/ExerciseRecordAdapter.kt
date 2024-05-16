package com.ajou.helptmanager.memberDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R

class ExerciseRecordAdapter(private val exerciseRecords: List<ExerciseRecord>) : RecyclerView.Adapter<ExerciseRecordAdapter.ExerciseRecordViewHolder>() {

    class ExerciseRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvExerciseRecordName)
        val part: TextView = view.findViewById(R.id.tvExerciseRecordPart)
        val reps: TextView = view.findViewById(R.id.tvExerciseRecordReps)
        val sets: TextView = view.findViewById(R.id.tvExerciseRecordSets)
        val time: TextView = view.findViewById(R.id.tvExerciseRecordTime)


        fun bind(record: ExerciseRecord) {
            name.text = record.exerciseName
            part.text = record.exercisePart
            reps.text = String.format("%d회", record.reps)
            sets.text = String.format("%d세트", record.sets)
            time.text = record.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_record, parent, false)
        return ExerciseRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseRecordViewHolder, position: Int) {
        val record = exerciseRecords[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int = exerciseRecords.size
}
