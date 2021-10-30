package com.jogayed.core.presentation.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Implementation of the [RecyclerView.OnScrollListener]
 * Used to define the infinite scrolling behavior for RecyclerView
 * @property isLoadMoreEnabled higher order function to check if the load more is enabled or not
 * @property loadMore higher order function to load more with [currentPage]
 *
 * @param recyclerView
 */
class RecyclerViewPaginator(
    recyclerView: RecyclerView,
    val isLoadMoreEnabled: () -> Boolean = { false },
    val loadMore: (Int) -> Unit
) : RecyclerView.OnScrollListener() {
    //if there is at least 2 items in the list the load more will be called
    var threshold = 2
    var currentPage = 1

    init {
        recyclerView.addOnScrollListener(this)
    }

    fun resetCurrentPage() {
        currentPage = 1
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isLoadMoreEnabled()) {
            val layoutManager = recyclerView.layoutManager
            layoutManager?.run {
                val visibleItemCount = childCount
                val totalItemCount = itemCount
                val positionOfFirstVisibleItem = when (this) {
                    is LinearLayoutManager -> findLastVisibleItemPosition()
                    is GridLayoutManager -> findLastVisibleItemPosition()
                    else -> return
                }
                if (visibleItemCount + positionOfFirstVisibleItem + threshold >= totalItemCount)
                    loadMore(++currentPage)
            }
        }
    }

}