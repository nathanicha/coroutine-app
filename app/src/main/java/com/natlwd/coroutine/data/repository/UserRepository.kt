package com.natlwd.coroutine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natlwd.coroutine.MainApplication
import com.natlwd.coroutine.data.ApiClient
import com.natlwd.coroutine.data.remote.UserService
import com.natlwd.coroutine.model.NetworkResult
import com.natlwd.coroutine.model.UserResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
        val instance = UserRepository()
    }

    private val api: UserService? =
        ApiClient(MainApplication.getContext()).getClient()?.create(UserService::class.java)

    //Return with LiveData
    suspend fun getUserFirstMethod(id: String): LiveData<NetworkResult<UserResponse?>> {
        val userLiveData = MutableLiveData<NetworkResult<UserResponse?>>()
        //Switching to BG Thread (Dispatchers.IO)
        withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            //Handling exception thrown by a coroutine
            userLiveData.value = NetworkResult.Failed(throwable)
        }) {
            try {
                val result = api?.getUser(id)
                if (result?.isSuccessful == true) {
                    Log.d(TAG, "Completed: ${result.body()}")
                    userLiveData.value = NetworkResult.Completed(result.body())
                } else {
                    Log.d(TAG, "onFailure: ${result?.errorBody()}")
                    userLiveData.value = NetworkResult.Failed(Throwable(result?.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "onFailure: ${e.message}")
                userLiveData.value = NetworkResult.Failed(e)
            }
        }

        return userLiveData
    }

    //Return with data class, ViewModel should have LiveData to handle this
    suspend fun getUserSecondMethod(id: String): NetworkResult<UserResponse?> {
        //Switching to BG Thread (Dispatchers.IO)
        return withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            //Handling exception thrown by a coroutine
            NetworkResult.Failed(throwable)
        }) {
            try {
                val result = api?.getUser(id)
                if (result?.isSuccessful == true) {
                    Log.d(TAG, "Completed: ${result.body()}")
                    NetworkResult.Completed(result.body())
                } else {
                    Log.d(TAG, "onFailure: ${result?.errorBody()}")
                    NetworkResult.Failed(Throwable(result?.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "onFailure: ${e.message}")
                NetworkResult.Failed(e)
            }
        }
    }

}