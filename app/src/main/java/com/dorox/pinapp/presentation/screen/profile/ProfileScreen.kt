@file:OptIn(ExperimentalMaterial3Api::class)

package com.dorox.pinapp.presentation.screen.profile

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dorox.pinapp.R
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray150
import com.dorox.pinapp.ui.theme.InfoTextBoxWithIcon
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.White100

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onRegisterButtonClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Профиль")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White100,
                    titleContentColor = Black100
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(color = Gray150),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (val currentState = state.value) {
                ProfileState.Anonymous -> {
                    AnonymousProfile(
                        onRegisterButtonClick = onRegisterButtonClick
                    )
                }
                is ProfileState.Authorized -> {
                    AuthorizedProfile(
                        state = currentState
                    )
                }
            }
        }
    }
}

@Composable
private fun AnonymousProfile(
    modifier: Modifier = Modifier,
    onRegisterButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White100
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_account_circle_24),
                contentDescription = null,
                tint = Orange200
            )

            Spacer(Modifier.height(16.dp))
            
            Text(
                text = "Гость",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = "Зарегистрируйтесь/ войдите,\n чтобы сохранить изменять объявления.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            CustomTextButton(
                onClick = {
                    onRegisterButtonClick()
                }
            ) {
                Text(
                    text = "Зарегистрироваться / Войти"
                )
            }
        }
    }
}

@Composable
private fun AuthorizedProfile(
    modifier: Modifier = Modifier,
    state: ProfileState.Authorized
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = White100
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = Orange200
                )
                Column {
                    if (state.displayName.isNotEmpty()) {
                        Text(
                            text = state.displayName,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        text = state.email,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    InfoTextBoxWithIcon(
        startIcon = TODO(),
        descriptionInfo = TODO(),
        startIconColor = TODO()
    )


}
