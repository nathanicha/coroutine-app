package com.natlwd.coroutine.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natlwd.coroutine.MainApplication
import com.natlwd.coroutine.data.ApiClient
import com.natlwd.coroutine.data.remote.UserService
import com.natlwd.coroutine.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
        val instance = UserRepository()
    }

    private val api: UserService? =
        ApiClient(MainApplication.getContext()).getClient()?.create(UserService::class.java)

    //    private val todoListLiveData: MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()
    private val userLiveData: MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()

//    val todos: LiveData<UserResponse>
//        get() {
//            api?.getUser()?.enqueue(object : Callback<UserResponse?> {
//                override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
//                    todoListLiveData.value = response.body()
//                }
//
//                override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
//
//                    call.cancel()
//                }
//            })
//            return todoListLiveData
//        }

    fun getUser(id: String): LiveData<UserResponse?> {
        api?.getUser(id)?.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                userLiveData.value = response.body()
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
        return userLiveData
    }

}