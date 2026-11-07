@file:OptIn(ExperimentalMaterial3Api::class)

package com.dorox.pinapp.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dorox.pinapp.R
import com.dorox.pinapp.ui.theme.CustomAlertDialog
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.TextBoxWithStartAndEndIcons
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange5
import com.dorox.pinapp.ui.theme.Orange50
import com.dorox.pinapp.ui.theme.White100

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onRegisterButtonClick: () -> Unit,
    onUserPostsClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Профиль",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange5,
                ),

                actions = {
                    if (state.value is ProfileState.Authorized) {
                        IconButton(
                            onClick = {
                                openDialog.value = true
                            },
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_logout),
                                contentDescription = null
                            )
                        }
                    }

                }
            )
        }
    ) { innerPadding ->

        if (openDialog.value) {
            CustomAlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = "Вы действительно хотите выйти из учетной записи?",
                onCancelClick = {
                    openDialog.value = false
                },
                onConfirmClick = {
                    openDialog.value = false
                    viewModel.signOut()
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Orange5),
        ) {
            when (val currentState = state.value) {
                ProfileState.Anonymous -> {
                    AnonymousProfile(
                        onRegisterButtonClick = onRegisterButtonClick
                    )
                }
                is ProfileState.Authorized -> {
                    AuthorizedProfile(
                        state = currentState,
                        onUserPostsClick =  {
                            onUserPostsClick()
                        }
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White100
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                        .background(color = Orange5),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_account),
                        contentDescription = null,
                        tint = Orange300
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Гость",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.log_in_to_manage_your_ads),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )


                Spacer(Modifier.height(16.dp))

                CustomTextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onRegisterButtonClick()
                    }
                ) {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

}

@Composable
private fun AuthorizedProfile(
    modifier: Modifier = Modifier,
    state: ProfileState.Authorized,
    onUserPostsClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .background(color = Orange5)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(color = White100, shape = RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                        .background(color = Orange5),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_account),
                        contentDescription = null,
                        tint = Orange300
                    )
                }

                Column {
                    if (state.displayName.isNotEmpty()) {
                        Text(
                            text = state.displayName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    Text(
                        text = state.email,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }


        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = White100
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

                TextBoxWithStartAndEndIcons(
                    modifier = Modifier.padding()
                        .clickable {
                            onUserPostsClick()
                        },
                    startIcon = ImageVector.vectorResource(R.drawable.ic_ad),
                    descriptionText = "Мои объявления",
                    startIconColor = Orange300,
                    endIcon = ImageVector.vectorResource(R.drawable.arrow_right),
                    endIconColor = Orange300
                )

                HorizontalDivider(color = Orange5, thickness = 2.dp)

                TextBoxWithStartAndEndIcons(
                    modifier = Modifier
                        .clickable {

                        },
                    startIcon = ImageVector.vectorResource(R.drawable.ic_notifications),
                    descriptionText = "Уведомления",
                    startIconColor = Orange300,
                    endIcon = ImageVector.vectorResource(R.drawable.arrow_right),
                    endIconColor = Orange300
                )

                HorizontalDivider(color = Orange5, thickness = 2.dp)

                TextBoxWithStartAndEndIcons(
                    modifier = Modifier
                        .clickable {

                        },
                    startIcon = ImageVector.vectorResource(R.drawable.ic_settings),
                    descriptionText = "Настройки",
                    startIconColor = Orange300,
                    endIcon = ImageVector.vectorResource(R.drawable.arrow_right),
                    endIconColor = Orange300
                )

                HorizontalDivider(color = Orange5, thickness = 2.dp)

                TextBoxWithStartAndEndIcons(
                    modifier = Modifier
                        .clickable {

                        },
                    startIcon = ImageVector.vectorResource(R.drawable.ic_help),
                    descriptionText = "Помощь",
                    startIconColor = Orange300,
                    endIcon = ImageVector.vectorResource(R.drawable.arrow_right),
                    endIconColor = Orange300
                )
            }
    }
}

