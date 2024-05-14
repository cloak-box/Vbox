package com.black.cat.system.widget.adapter

import androidx.viewbinding.ViewBinding

interface BaseItemClickListener<T> {
  fun onClick(binding: ViewBinding, positionData: T, position: Int)
}
