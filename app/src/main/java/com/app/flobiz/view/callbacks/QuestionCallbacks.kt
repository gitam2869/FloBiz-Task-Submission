package com.app.flobiz.view.callbacks

import com.app.flobiz.model.dataclass.Items

class QuestionCallbacks {

    companion object {
        interface IQuestionCallbacks {
            fun onQuestionClick(position: Int, items: Items)
        }
    }
}