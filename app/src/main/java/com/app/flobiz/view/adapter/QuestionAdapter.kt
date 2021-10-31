package com.app.flobiz.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.flobiz.model.dataclass.Items
import com.app.flobiz.view.callbacks.QuestionCallbacks
import com.app.flobiz.view.viewholder.QuestionViewHolder

class QuestionAdapter(val iBookCallbacks: QuestionCallbacks.Companion.IQuestionCallbacks) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "QuestionAdapter"


    private val differCallback = object : DiffUtil.ItemCallback<Items>() {
        override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
            return oldItem.question_id == newItem.question_id
        }

        override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionViewHolder.createViewHolder(parent)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookViewHolder = holder as QuestionViewHolder
        bookViewHolder.bind(
            holder,
            position,
            differ.currentList.get(position),
            iBookCallbacks
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Items>?) {
        differ.submitList(list)
    }

}