package com.mkandeel.actionmemo.Helper;

import android.os.Bundle

interface ClickListener {
    fun onItemClickListener(position: Int, extras: Bundle?)
    fun onLongItemClickListener(position: Int, extras: Bundle?)
}