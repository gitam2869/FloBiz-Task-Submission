package com.app.flobiz.view.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.flobiz.R
import com.app.flobiz.databinding.ItemQuestionBinding
import com.app.flobiz.model.dataclass.Items
import com.app.flobiz.utility.DateTimeUtility
import com.app.flobiz.view.callbacks.QuestionCallbacks
import com.bumptech.glide.Glide

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val TAG = "QuestionViewHolder"
    private val itemQuestionBinding = ItemQuestionBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(
        holder: RecyclerView.ViewHolder,
        position: Int,
        items: Items,
        iBookCallbacks: QuestionCallbacks.Companion.IQuestionCallbacks
    ) {

        itemQuestionBinding.run {
            tvQuestionTitle.text = items.title

            tvOwnerName.text = items.owner.display_name
            Glide.with(holder.itemView.context)
                .load(items.owner.profile_image)
                .placeholder(R.drawable.default_user_pic)
                .into(ivProfilePic)

            tvPostedDate.text = holder.itemView.context.resources.getString(R.string.posted_on) + " " + DateTimeUtility.getDateFromTime(DateTimeUtility.QUESTION_POSTED_DATE_PATTERN, items.creation_date*1000L)

            cvQuestion.setOnClickListener{
                iBookCallbacks.onQuestionClick(holder.adapterPosition, items)
            }
        }

    }


    companion object {
        fun createViewHolder(parent: ViewGroup): QuestionViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
            return QuestionViewHolder(view)
        }
    }
}