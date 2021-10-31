package com.app.flobiz.view.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.flobiz.R
import com.app.flobiz.common.FloBizApplication
import com.app.flobiz.databinding.ActivityMainBinding
import com.app.flobiz.model.dataclass.Items
import com.app.flobiz.utility.IntentUtility
import com.app.flobiz.utility.NetworkUtility
import com.app.flobiz.utility.SnackBarUtility
import com.app.flobiz.view.adapter.QuestionAdapter
import com.app.flobiz.view.callbacks.QuestionCallbacks
import com.app.flobiz.viewmodel.BookViewModel
import com.app.flobiz.viewmodel.BookViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var activityMainBinding: ActivityMainBinding

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as FloBizApplication).bookRepository)
    }

    private var itemsList: MutableList<Items> = mutableListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bookAdapter: QuestionAdapter

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        init()
        callListeners()
    }


    private fun init() {
        linearLayoutManager = LinearLayoutManager(this)
        activityMainBinding.rvBookItem.layoutManager = linearLayoutManager

        bookAdapter = QuestionAdapter(object : QuestionCallbacks.Companion.IQuestionCallbacks {
            override fun onQuestionClick(position: Int, items: Items) {
                if (NetworkUtility.isInternetAvailable()) {
                    IntentUtility.openURL(this@MainActivity, items.link, activityMainBinding.root)
                } else {
                    SnackBarUtility.show(
                        activityMainBinding.root,
                        resources.getString(R.string.internet_not_available)
                    )
                }
            }
        })
        activityMainBinding.rvBookItem.adapter = bookAdapter

        observeBookViewModel()
        getData()
    }


    private fun callListeners() {
        activityMainBinding.btnRefresh.setOnClickListener {
            if (NetworkUtility.isInternetAvailable()) {
                getData()
            } else {
                SnackBarUtility.show(
                    activityMainBinding.root,
                    resources.getString(R.string.internet_not_available)
                )
            }
        }

        activityMainBinding.etSearchQuestion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                hideEmptyDataSearchView()
                timer?.cancel()

                val searchQuery = p0.toString()

                if (searchQuery.isEmpty()) {
                    setDefaultList()
                } else {
                    setFilterList(searchQuery)
                }

                setCloseButtonState(searchQuery)
            }
        })


        activityMainBinding.ivClose.setOnClickListener {
            setDefaultList()
            setDefaultInputBox()
            hideEmptyDataSearchView()
        }

        activityMainBinding.ivFilter.setOnClickListener {
            SnackBarUtility.show(
                activityMainBinding.ryRoot,
                resources.getString(R.string.filter_task_not_finish)
            )
        }
    }

    //observe view model for book
    private fun observeBookViewModel() {
        bookViewModel.bookResponse.observe(this, Observer {
            if (it != null) {
                itemsList.clear()
                itemsList.addAll(it.items)
                bookAdapter.submitList(itemsList)

                showSearchView()
                activityMainBinding.pbLoading.visibility = View.GONE
                activityMainBinding.lyData.visibility = View.VISIBLE

                showDefaultAverageViews()
                lifecycleScope.launch {
                    showAverageViewCount(itemsList)
                }

            } else {
                hideDefaultAverageViews()
                hideSearchView()
                activityMainBinding.lyData.visibility = View.GONE
                activityMainBinding.pbLoading.visibility = View.GONE
                activityMainBinding.tvMessage.visibility = View.VISIBLE

                activityMainBinding.tvMessage.text =
                    resources.getString(R.string.data_not_available)
            }

            activityMainBinding.btnRefresh.visibility = View.VISIBLE
        })

        bookViewModel.errorResponse.observe(this, Observer {
            if (NetworkUtility.isInternetAvailable()) {
                activityMainBinding.tvMessage.text =
                    resources.getString(R.string.server_error_message)
                activityMainBinding.tvMessage.visibility = View.VISIBLE
            } else {
                activityMainBinding.tvMessage.text =
                    resources.getString(R.string.internet_not_available)
                activityMainBinding.tvMessage.visibility = View.VISIBLE
                activityMainBinding.pbLoading.visibility = View.GONE
            }

            hideSearchView()
            activityMainBinding.pbLoading.visibility = View.GONE
            activityMainBinding.btnRefresh.visibility = View.VISIBLE
        })
    }

    private fun getData() {
        setDefaultInputBox()
        hideSearchView()
        hideDefaultAverageViews()
        activityMainBinding.lyData.visibility = View.GONE
        activityMainBinding.tvMessage.visibility = View.GONE
        activityMainBinding.pbLoading.visibility = View.VISIBLE

        bookViewModel.getQuestions()
    }

    private fun hideSearchView() {
        activityMainBinding.etSearchQuestion.visibility = View.GONE
        activityMainBinding.ivFilter.visibility = View.GONE
    }

    private fun showSearchView() {
        activityMainBinding.etSearchQuestion.visibility = View.VISIBLE
        activityMainBinding.ivFilter.visibility = View.VISIBLE
    }


    private fun setDefaultList() {
        showDefaultAverageViews()
        lifecycleScope.launch {
            showAverageViewCount(itemsList)
        }

        bookAdapter.submitList(itemsList)
    }


    private fun setFilterList(query: String) {
        timer?.cancel()
        timer = object : CountDownTimer(300, 100) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val list = getFilterdList(query)
                bookAdapter.submitList(list)

                showDefaultAverageViews()
                lifecycleScope.launch {
                    showAverageViewCount(list)
                }

                if (list.isEmpty()) {
                    hideDefaultAverageViews()
                    showEmptyDataSearchView(query)
                } else {
                    hideEmptyDataSearchView()
                }
            }
        }
        timer?.start()
    }

    private fun getFilterdList(text: String): MutableList<Items> {
        val filteredList: ArrayList<Items> = ArrayList()
        for (item in itemsList) {
            if (item.title.lowercase()
                    .contains(text.lowercase()) || item.owner.display_name.lowercase()
                    .contains(text.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun setCloseButtonState(query: String) {
        if (query.isEmpty()) {
            activityMainBinding.ivClose.visibility = View.GONE
        } else {
            activityMainBinding.ivClose.visibility = View.VISIBLE
        }
    }

    private fun setDefaultInputBox() {
        activityMainBinding.etSearchQuestion.setText("")
        activityMainBinding.etSearchQuestion.clearFocus()
        activityMainBinding.etSearchQuestion.clearAnimation()
    }

    private fun showEmptyDataSearchView(query: String) {
        if (!activityMainBinding.tvMessage.isVisible) {
            activityMainBinding.tvMessage.text =
                resources.getString(R.string.empty_search_data, query)
            activityMainBinding.tvMessage.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyDataSearchView() {
        activityMainBinding.tvMessage.visibility = View.GONE
    }


    // Dispatchers.Main
    suspend fun showAverageViewCount(list: MutableList<Items>) {
        val result = getAverageViewCount(list)
        show(result)
    }

    private suspend fun getAverageViewCount(list: MutableList<Items>): Pair<Float, Float> {
        var averageViewCount = 0F
        var averageAnswerCount = 0F
        withContext(Dispatchers.IO) {
            for (item in list) {
                averageViewCount += item.view_count
                averageAnswerCount += item.answer_count
            }

            averageViewCount /= list.size
            averageAnswerCount /= list.size
        }

        return Pair(averageViewCount, averageAnswerCount)
    }

    private fun showDefaultAverageViews() {
        activityMainBinding.tvAverageViewCount.text = resources.getString(R.string.loading)
        activityMainBinding.tvAverageAnswerCount.text = resources.getString(R.string.loading)
        activityMainBinding.lyAverage.visibility = View.VISIBLE
    }

    private fun hideDefaultAverageViews() {
        activityMainBinding.lyAverage.visibility = View.GONE
    }

    private fun show(result: Pair<Float, Float>) {
        activityMainBinding.tvAverageViewCount.text =
            resources.getString(R.string.average_view_count, result.first)
        activityMainBinding.tvAverageAnswerCount.text =
            resources.getString(R.string.average_answer_count, result.second)
    }
}