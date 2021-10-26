package com.natlwd.coroutine.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natlwd.coroutine.data.repository.UserRepository
import com.natlwd.coroutine.model.NetworkResult
import com.natlwd.coroutine.model.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = UserRepository.instance

    private val _userLiveData: MutableLiveData<NetworkResult<UserResponse?>> = MutableLiveData()
    val userLiveData: LiveData<NetworkResult<UserResponse?>> = _userLiveData

    /**
     * Coroutine - Thread
     *
     * Coroutine Scope - Define scope for Coroutine such as, ViewModelScope, LifeCycleScope, ...
     *
     * Coroutine Builder - Define method of initial coroutine
     *  runBlocking: Block Thread, Waiting until work done
     *  launch: Return Job, join()
     *  async: Return Deferred, get value from Deferred by await()
     *
     * Coroutine context - Define thread to work with
     *  Dispatchers.Main: Main/UI thread
     *  Dispatchers.IO: Shared Pool of Thread with Dispatcher.Default on JVM, Offloading Blocking IO
     *  Dispatcher.Default: Shared Pool of Thread with Dispatcher.IO, Maximum parallel depends on CPI Core
     *  Dispatcher.Unconfined: Current thread which work with suspend function
     *
     *  Suspending function - function that can start, pause and resume
     *   https://medium.com/mobile-app-development-publication/understanding-suspend-function-of-coroutines-de26b070c5ed
     *   https://medium.com/mobile-app-development-publication/coroutine-suspend-function-when-does-it-start-suspend-or-terminate-2762cabac54e
     */
    fun getUsers(userId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _userLiveData.value = NetworkResult.Started
            _userLiveData.value = repository.getUserSecondMethod(userId)
        }
    }
}