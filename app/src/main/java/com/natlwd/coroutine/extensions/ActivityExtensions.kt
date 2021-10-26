package com.natlwd.coroutine.extensions

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.natlwd.coroutine.model.NetworkResult

/**
 *  Reference: https://github.com/SamYStudiO/beaver
 */
fun <T> NetworkResult<T>.handleStateWithLoading(
    fragmentActivity: FragmentActivity,
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {
        //fragment.showErrorDialog(throwable = it)
    },
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is NetworkResult.Started -> {
            fragmentActivity.showLoading()
            started()
        }
        is NetworkResult.Completed -> {
            fragmentActivity.hideLoading()
            complete(this.data)
        }
        is NetworkResult.Failed -> {
            fragmentActivity.hideLoading()
            failed(this.throwable)
        }
    }
}

/**
 *  Reference: https://github.com/SamYStudiO/beaver
 */
fun <T> NetworkResult<T>.handleState(
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {
        //fragment.showErrorDialog(throwable = it)
    },
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is NetworkResult.Started -> {
            started()
        }
        is NetworkResult.Completed -> {
            complete(this.data)
        }
        is NetworkResult.Failed -> {
            failed(this.throwable)
        }
    }
}

fun FragmentActivity.showLoading() {
    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
}

fun FragmentActivity.hideLoading() {
    Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
}