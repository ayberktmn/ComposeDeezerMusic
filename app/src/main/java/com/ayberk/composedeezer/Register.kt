package com.ayberk.composedeezer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayberk.composedeezer.viewmodel.LoginViewModel

@Composable
fun Register(navHostController: NavHostController) {

    ChangePasswordScreen(navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navHostController: NavHostController) {

    val LoginViewModel: LoginViewModel= hiltViewModel()
    var password by remember { mutableStateOf("") }
    var passwordagain by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isShowingPasswordError by remember { mutableStateOf(false) }


    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // Öğeleri yatayda ortala
        verticalArrangement = Arrangement.Center
    ) {

        Loading()

        Image(
            painter = painterResource(id = R.drawable.changepassword),
            contentDescription = "Şifre Değişikliği",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Yeni Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp),
            leadingIcon = { // İşte burada sol tarafına simge (ikon) ekliyoruz
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            isError = !isPasswordValid, // Hata durumunda isError'ı true yap
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (!isPasswordValid) Color.Red else Color.Gray, // Hata durumunda çerçeve rengini değiştir
                unfocusedBorderColor = if (!isPasswordValid) Color.Red else Color.Gray
            )
        )

        OutlinedTextField(
            value = passwordagain,
            onValueChange = { passwordagain = it },
            label = { Text("Şifre Tekrar") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp),
            leadingIcon = { // İşte burada sol tarafına simge (ikon) ekliyoruz
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            isError = !isPasswordValid, // Hata durumunda isError'ı true yap
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (!isPasswordValid) Color.Red else Color.Gray, // Hata durumunda çerçeve rengini değiştir
                unfocusedBorderColor = if (!isPasswordValid) Color.Red else Color.Gray
            )
        )

        val context = LocalContext.current
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {

                isPasswordValid = isValidPassword(password)

                if (isPasswordValid && password == passwordagain) {

                    LoginViewModel.changePassword(
                        password,
                        passwordagain
                    ) { success, message ->
                        if (success) {
                            // Şifre değişikliği başarılı
                            Toast.makeText(
                                context,
                                "Şifre Değişikliği Başarılı",
                                Toast.LENGTH_SHORT
                            ).show()
                            navHostController.navigate("login")
                            clearPassword(context)
                        } else {
                            // Şifre değişikliği başarısız
                            Toast.makeText(
                                context,
                                "Şifre Değişikliği Yapılamadı",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } else {
                    isShowingPasswordError = true
                }
            },
            enabled = passwordagain.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(bottom = 16.dp)

        ) {
            Text(text = stringResource(id = R.string.passwordchange))
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!isPasswordValid || isShowingPasswordError) {
            if (!isPasswordValid) {
                Text(
                    text = "Şifre uzunluğu 5 karakterden fazla olmalıdır.",
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

            } else if (isShowingPasswordError) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Şifreler uyuşmuyor.",
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

private fun isValidPassword(password: CharSequence): Boolean {
    return password.length >= 5 // Şifrenin en az 5 karakter olmasını isteyelim.
}