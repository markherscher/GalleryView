package com.herscher.galleryviewer.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible

/**
 * Modifies the visibility of all children for the [ViewGroup], setting to VISIBLE the child view
 * with the same ID as the specified view, and GONE for all other child views. Useful to easily
 * show only one child view for a `FrameLayout`.
 *
 * @param view the view to make visible
 */
fun ViewGroup.showOnlyChildView(view: View) {
    forEach { v -> v.isVisible = (v.id == view.id)  }
}