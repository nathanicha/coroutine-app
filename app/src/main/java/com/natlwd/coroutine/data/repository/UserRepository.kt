package com.natlwd.coroutine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natlwd.coroutine.MainApplication
import com.natlwd.coroutine.data.ApiClient
import com.natlwd.coroutine.data.remote.UserService
import com.natlwd.coroutine.model.NetworkResult
import com.natlwd.coroutine.model.UserResponse
import kotlinx.coroutines.*
import retrofit2.Response

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
        val instance = UserRepository()
    }

    private val api: UserService? =
        ApiClient(MainApplication.getContext()).getClient()?.create(UserService::class.java)

    suspend fun getUserReturnLiveData(id: String): LiveData<NetworkResult<UserResponse?>> {
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

    suspend fun getUserReturnResp(id: String): NetworkResult<UserResponse?> {
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

    suspend fun getUserParallel(): NetworkResult<UserResponse?> {
        return withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            NetworkResult.Failed(throwable)
        }) {
            try {
                val result1 = async {
                    api?.getUser("1")
                }
                val result2 = async {
                    delay(3000)
                    api?.getUser("2")
                }

                NetworkResult.Completed(processData(result1.await(), result2.await()))
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResult.Failed(e)
            }
        }
    }

    private fun processData(
        result1: Response<UserResponse?>?,
        result2: Response<UserResponse?>?
    ): UserResponse {
        return UserResponse(
            title = "${result1?.body()?.title} && ${result2?.body()?.title}"
        )
    }

    suspend fun getUserChain(): NetworkResult<UserResponse?> {
        return withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            NetworkResult.Failed(throwable)
        }) {
            try {
                val result1 = api?.getUser("1")
                val result2 = api?.getUser(result1?.body()?.id.toString())
                NetworkResult.Completed(result2?.body())
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResult.Failed(e)
            }
        }
    }
}