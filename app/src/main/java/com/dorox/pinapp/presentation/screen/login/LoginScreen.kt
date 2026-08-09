package com.dorox.pinapp.presentation.screen.login

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dorox.pinapp.R
import androidx.core.net.toUri
import com.dorox.pinapp.presentation.screen.main.MainScreen
import com.dorox.pinapp.ui.theme.Blue100
import com.dorox.pinapp.ui.theme.PinAppTheme
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray200
import com.dorox.pinapp.ui.theme.Gray300
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.White100

//@Composable
//fun LoginScreen(
//    onClickLoginButton: () -> Unit
//) {
//
//    val emailState = remember { TextFieldState() }
//    val passwordState = remember { TextFieldState() }
//    val isEnabled by remember {
//        derivedStateOf {
//            emailState.text.isNotEmpty() &&
//            passwordState.text.isNotEmpty() &&
//            emailState.text.matches(
//                Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
//            )
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 16.dp, end = 16.dp)
//    ) {
//        Spacer(modifier = Modifier.height(140.dp))
//        Text(
//            text = "Вход",
//            fontSize = 28.sp
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//        Text(
//            text = "Email",
//            fontSize = 16.sp
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            modifier = Modifier.fillMaxWidth(),
//            state = emailState,
//            placeholder = {
//                Text(
//                    text = "example@gmail.com",
//                    color = Gray100
//                )
//           },
//            shape = RoundedCornerShape(32.dp),
//            colors = TextFieldDefaults.colors(
//                focusedTextColor = White100,
//                unfocusedTextColor = Gray100,
//                focusedContainerColor = Gray200,
//                unfocusedContainerColor = Gray200
//            )
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = "Пароль",
//            fontSize = 16.sp
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            modifier = Modifier.fillMaxWidth(),
//            state = passwordState,
//
//            placeholder = {
//                Text(
//                    text = "Введите пароль",
//                    color = Gray100
//                )
//            },
//            shape = RoundedCornerShape(32.dp),
//            colors = TextFieldDefaults.colors(
//                focusedTextColor = White100,
//                unfocusedTextColor = Gray100,
//                focusedContainerColor = Gray200,
//                unfocusedContainerColor = Gray200
//            )
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(40.dp),
//            onClick = {
//                onClickLoginButton()
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Orange100,
//                disabledContainerColor = Gray200
//
//            ),
//            enabled = isEnabled
//        ) {
//            Text(
//                text = "Вход",
//                color = White100,
//                fontSize = 16.sp
//            )
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row {
//                Text(
//                    text = "Нету аккаунта?",
//                    fontSize = 12.sp
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(text = "Регистрация",
//                    fontSize = 12.sp,
//                    color = Orange100
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Забыл пароль",
//                fontSize = 12.sp,
//                color = Orange100
//            )
//        }
//        Spacer(modifier = Modifier.height(24.dp))
//        HorizontalDivider(color = Gray300)
//        Spacer(modifier = Modifier.height(24.dp))
//        Row() {
//            val context = LocalContext.current
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .weight(1f),
//                onClick = {
//                    context.startActivity(
//                        Intent(Intent.ACTION_VIEW, "https://vk.com/".toUri()
//                        )
//                    )
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Blue100
//                ),
//                enabled = true
//            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.ic_vk),
//                    contentDescription = null
//                )
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .weight(1f),
//                onClick = {
//                    context.startActivity(
//                        Intent(Intent.ACTION_VIEW, "https://ok.ru/".toUri()
//                        )
//                    )
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Orange100
//                ),
//                enabled = true
//            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.ic_ok),
//                    contentDescription = null
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewLoginScreen() {
//    PinAppTheme() {
//        MainScreen()
//    }
//}