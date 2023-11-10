package com.ayberk.composedeezer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayberk.composedeezer.model.User
import com.ayberk.composedeezer.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val auth : FirebaseAuth

) : ViewModel(){

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    fun login(email:String,password:String){

        viewModelScope.launch {
            _login.emit(Resource.Loading())
        }

        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null){
                    viewModelScope.launch {
                        _login.emit(Resource.Success(user))
                    }
                }
                else{
                    viewModelScope.launch {
                        _login.emit(Resource.Error("Giriş başarısız,kullanıcı bulunamadı..."))
                    }
                }
            }
            .addOnFailureListener { e ->
                viewModelScope.launch {
                    _login.emit(Resource.Error(e.message.toString()))
                }
            }
    }

    fun createEmailandPassword(user: User, password: String){

        auth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    viewModelScope.launch {
                        _login.emit(Resource.Success(user))
                    }
                }
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

}