package com.natlwd.coroutine.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.natlwd.coroutine.data.repository.UserRepository
import com.natlwd.coroutine.model.UserResponse

class MainViewModel : ViewModel() {

    private val repository = UserRepository.instance

    fun getUsers(userId: String): LiveData<UserResponse?> {
        return repository.getUser(userId)
    }
}