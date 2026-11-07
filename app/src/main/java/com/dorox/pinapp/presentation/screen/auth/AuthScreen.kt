package com.dorox.pinapp.presentation.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dorox.pinapp.R
import com.dorox.pinapp.presentation.screen.auth.AuthCommand.*
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.CustomOutlinedTextField
import com.dorox.pinapp.ui.theme.CustomScreenTopAppBar
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray550
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    //val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()


    val snackbar = remember { SnackbarHostState() }

    var passwordVisible by remember { mutableStateOf(false) }

    val selectedTabIndex = when (state.value) {
        is AuthState.Authorization -> 0
        is AuthState.Registration -> 1
        else -> 0
    }
    val titles = listOf("Войти", "Создать аккаунт")

    LaunchedEffect(error) {
        error?.let {
            snackbar.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            val title = if (state.value is AuthState.Authorization ||
                            state.value is AuthState.ResetPassword)
                stringResource(R.string.sign_in)
            else
                stringResource(R.string.sign_up)
            CustomScreenTopAppBar(
                title = title,
                onBackIconClick = {
                    onFinished()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbar)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Orange5),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                modifier = Modifier.size(80.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_app_svg),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Потеряно и найдено",
                style = MaterialTheme.typography.titleLarge,
                color = Orange300,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold

            )

            Spacer(modifier = Modifier.height(24.dp))


            SecondaryTabRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                selectedTabIndex = selectedTabIndex,
                containerColor = Orange5
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            val currentState =
                                if (index == 0)
                                    AuthState.Authorization()
                                else
                                    AuthState.Registration()
                            viewModel.processCommand(SwitchMainState(currentState))
                        },
                        selectedContentColor = Orange200,
                        unselectedContentColor = Gray550,
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                }
            }

            when(val currentState = state.value) {
                is AuthState.Authorization -> {

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.email,
                        placeholderText = "E-mail",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_email),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        onValueChange = {
                            viewModel.processCommand(InputEmail(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.password,
                        placeholderText = "Пароль",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                ImageVector.vectorResource(R.drawable.ic_visibility)
                            else
                                ImageVector.vectorResource(R.drawable.ic_visibility_off)

                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = null,
                                    tint = Orange300
                                )
                            }
                        },
                        onValueChange = {
                            viewModel.processCommand(InputPassword(it))
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(4.dp))


                    TextButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End),
                        onClick = {
                            viewModel.processCommand(SwitchResetPasswordState(currentState))
                        }
                    ) {
                        Text(
                            text = "Забыли пароль?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.processCommand(Complete)
                        }
                    ) {
                        Text(
                            text = "Войти",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                is AuthState.ResetPassword -> {

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.processCommand(SwitchResetPasswordState(currentState))
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                contentDescription = null,
                                tint = Black100
                            )
                        }
                        Text(
                            text = "Восстановление пароля",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.email,
                        placeholderText = "E-mail",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_email),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        onValueChange = {
                            viewModel.processCommand(InputEmail(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomTextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.processCommand(Complete)
//                            if (error == null)
//                                viewModel.processCommand(SwitchResetPasswordState(currentState))
//                            else

                        }
                    ) {
                        Text(
                            text = "Отправить ссылку восстановления",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }

                is AuthState.Registration -> {

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.displayName,
                        placeholderText = "Имя",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_account_circle_24),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        onValueChange = {
                            viewModel.processCommand(InputDisplayName(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.email,
                        placeholderText = "E-mail",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_email),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        onValueChange = {
                            viewModel.processCommand(InputEmail(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = currentState.password,
                        placeholderText = "Пароль",
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Orange300
                            )
                        },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                ImageVector.vectorResource(R.drawable.ic_visibility)
                            else
                                ImageVector.vectorResource(R.drawable.ic_visibility_off)

                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = null,
                                    tint = Orange300
                                )
                            }
                        },
                        onValueChange = {
                            viewModel.processCommand(InputPassword(it))
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomTextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.processCommand(Complete)
                        }
                    ) {
                        Text(
                            text = "Зарегистрироваться",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }
                AuthState.Finished -> {
                    onFinished()
                }

            }
        }
    }
}
