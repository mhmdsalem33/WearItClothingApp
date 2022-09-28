package com.example.wearit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration :RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
       // super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildLayoutPosition(view) % 2 != 0)
        {
            outRect.top = 50
            outRect.bottom =  -50
        }
        else
            outRect.top = 0

    }
}