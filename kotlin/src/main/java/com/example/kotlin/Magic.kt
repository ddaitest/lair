package com.example.kotlin

/**
 * Created by qdhl on 2017/8/9.
 */

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

fun <T : View> Activity.bind(@IdRes idRes: Int): T {
    @Suppress("UNCHECKED_CAST")
    return  findViewById(idRes) as T
}
//
fun <T : View> Activity.bind2(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById(idRes) as T }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById(idRes) as T }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

