package com.herscher.galleryviewer.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible

fun ViewGroup.showOnlyChildView(view: View) {
    forEach { v -> v.isVisible = (v.id == view.id)  }
}