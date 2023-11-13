package com.ayberk.composedeezer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayberk.composedeezer.model.Genre.User
import com.ayberk.composedeezer.util.Constans.USER_COLLECTION
import com.ayberk.composedeezer.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore


) : ViewModel() {

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    fun login(email: String, password: String) {

        viewModelScope.launch {
            _login.emit(Resource.Loading())
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    viewModelScope.launch {
                        _login.emit(Resource.Success(user))
                    }
                } else {
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

    fun createEmailandPassword(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                it.user?.let { firebaseUser ->
                    saveUserInfo(firebaseUser.uid, user)
                }
            }
            .addOnFailureListener { e ->
                _register.value = Resource.Error(e.message.toString())
            }
    }

    fun changePassword(
        email: String,
        newPassword: String,
        confirmPassword: String,
        onResult: (Boolean, String) -> Unit
    ) {
        // E-posta formatını kontrol et
        if (newPassword == confirmPassword) {
            // Firebase Authentication üzerinde oturum aç
            auth.signInWithEmailAndPassword(email, newPassword)
                .addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        // Oturum açma başarılı ise, şifre değiştir
                        val user = auth.currentUser
                        user?.updatePassword(newPassword)
                            ?.addOnCompleteListener { updatePasswordTask ->
                                if (updatePasswordTask.isSuccessful) {
                                    onResult(true, "Şifre değişikliği başarılı")
                                } else {
                                    onResult(false, "Şifre değişikliği başarısız")
                                }
                            }
                    } else {
                        onResult(false, "Oturum açma başarısız")
                    }
                }
        } else {
            onResult(false, "E-posta formatı geçersiz veya şifreler uyuşmuyor")
        }
    }

    fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            val querySnapshot = db.collection(USER_COLLECTION).get().await()

            // QuerySnapshot'tan User listesini oluştur
            val usersList = mutableListOf<User>()
            for (document in querySnapshot.documents) {
                val user = document.toObject(User::class.java)
                user?.let {
                    usersList.add(it)
                }
            }

            usersList
        } catch (e: Exception) {
            // Hata durumunu ele al
            emptyList()
        }
    }
}
