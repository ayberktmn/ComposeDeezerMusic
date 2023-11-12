package com.ayberk.composedeezer

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.ayberk.composedeezer.model.User
import com.ayberk.composedeezer.util.Resource
import com.ayberk.composedeezer.viewmodel.LoginViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navHostController: NavHostController, viewLoginModel: LoginViewModel = hiltViewModel()){

    var allowBackNavigation by remember { mutableStateOf(true) }
    BackHandler(enabled = allowBackNavigation){}

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isShowingEmailError by remember { mutableStateOf(false) }
    var isShowingPasswordError by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current as ComponentActivity
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            isClickable = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = if (selectedImageUri != null) {
                rememberImagePainter(data = selectedImageUri)
            } else {
                painterResource(id = R.drawable.user)
            },
            contentDescription = "Kullanıcı Resim",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    if (isClickable) {
                        launcher.launch("image/*")
                    }
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = email)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email,
            onValueChange = {
                email = it
            }, label = { Text(text = stringResource(id = R.string.email)) },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp),
            leadingIcon = { //  burada sol tarafına simge (ikon) ekliyoruz
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            }
        )

        OutlinedTextField(value = password,
            onValueChange = {
                password = it
            }, label = { Text(text = stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp),
            leadingIcon = { //  burada sol tarafına simge (ikon) ekliyoruz
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { isChecked ->
                    rememberMe = isChecked
                }
            )

            Text(
                text = "Beni Hatırla"
            )

         /*   Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp)
            ) {
                Text(
                    text = "Şifremi unuttum",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable {
                            navHostController.navigate("register")
                        }
                )
                Spacer(
                    modifier = Modifier
                        .background(Color.Gray)
                        .height(1.dp)
                        .width(65.dp)
                        .align(Alignment.BottomStart) // Çizgiyi yazının altına hizalar
                )
            } */
        }
        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current

        Button(
            onClick = {
                isEmailValid = isValidEmail(email)
                isPasswordValid = isValidPassword(password)

                if (isEmailValid && isPasswordValid) {
                    viewLoginModel.login(email, password)
                    if (rememberMe) {
                        savePassword(context = context, password)
                        saveEmail(context = context,email)
                    }
                } else {
                    isShowingEmailError = true
                    isShowingPasswordError = true
                }
            },
            enabled = email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(bottom = 16.dp)
        ) {
            Text("Giriş Yap")
        }

        // LoginViewModel'den gelen değeri dinleyerek işlemi kontrol edebilirsiniz.
        LaunchedEffect(Unit) {
            viewLoginModel.login.collect() { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Yükleme durumunu burada kontrol et

                    }

                    is Resource.Success -> {
                        // Giriş başarılı olduğunda yapılacak işlemler burada
                        Toast.makeText(context,"Giriş yapılıyor",Toast.LENGTH_SHORT).show()
                        navHostController.navigate("anasayfa")

                    }

                    is Resource.Error -> {
                        // Hata durumunu burada kontrol et
                        // resource.message ile hata mesajına erişebilirsiniz: resource.message
                        Toast.makeText(context,"Lütfen bilgilerinizi kontrol edin",Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        Button(
            onClick = {
                isEmailValid = isValidEmail(email) // Email kontrolü burada yapılıyor
                isPasswordValid = isValidPassword(password)

                if (isEmailValid && isPasswordValid) {
                    navHostController.navigate("anasayfa")
                } else {
                    isShowingEmailError = true
                    isShowingPasswordError = true
                }
                val user = User(email,password,selectedImageUri?.path.toString())
                viewLoginModel.createEmailandPassword(user)

            },
            enabled = email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(bottom = 16.dp)
        ) {
            Text("Kayıt Ol")
        }

        Button(
            onClick = {
                val savedPassword = getSavedPassword(context = context)
                val savedEmail = getSavedEmail(context = context)

                if (!savedPassword.isNullOrBlank() && !savedEmail.isNullOrBlank()) {
                    email = savedEmail
                    password = savedPassword
                } else{
                    Toast.makeText(context,"Kayıtlı Hesap Bulunamadı",Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp)

        ) {
            Text("Kayıtlı hesabım")
        }

        if (!isEmailValid && isShowingEmailError) {
            Text(
                text = "Lütfen geçerli bir e-posta adresi giriniz.",
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else if (!isPasswordValid && isShowingPasswordError) {
            Text(
                text = "Şifre uzunluğu 5 karakterden fazla olmalıdır.",
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

    }

}


fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidPassword(password: CharSequence): Boolean {
    return password.length >= 5 // Şifrenin en az 5 karakter olmasını isteyelim.
}


private const val PASSWORD_KEY = "user_password"
private const val Email_KEY = "user_email"

fun savePassword(context: Context, password: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(PASSWORD_KEY, password)
    editor.apply()
}

fun getSavedPassword(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString(PASSWORD_KEY, null)
}

fun saveEmail(context: Context, email: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences1", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(Email_KEY, email)
    editor.apply()
}

fun getSavedEmail(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences1", Context.MODE_PRIVATE)
    return sharedPreferences.getString(Email_KEY, null)
}

fun clearPassword(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove(PASSWORD_KEY)
    editor.apply()
}