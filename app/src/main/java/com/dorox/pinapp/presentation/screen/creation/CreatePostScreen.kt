

package com.dorox.pinapp.presentation.screen.creation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.ui.theme.CustomOutlinedTextField
import com.dorox.pinapp.ui.theme.CustomScreenTopAppBar
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray150
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange5
import com.dorox.pinapp.ui.theme.SelectCategoryFilterChips
import com.dorox.pinapp.ui.theme.SelectStatusSegmentedButtons
import com.dorox.pinapp.ui.theme.White100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    viewModel: CreatePostViewModel = hiltViewModel(),
    onFinished: () -> Unit,

) {
    val state = viewModel.state.collectAsState()

    val categories = Category.entries.filter {
        it != Category.NOT_SELECTED &&
        it != Category.ALL
    }.toList()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.processCommand(CreatePostCommand.AddImage(uri = it))
            }
        }
    )

    var selectedChipCategory by remember { mutableStateOf(Category.NOT_SELECTED) }

    when (val currentState = state.value) {
        is CreatePostState.Creation -> {
            Scaffold(
                topBar = {
                    CustomScreenTopAppBar(
                        title = "Новое объявление",
                        onBackIconClick = {
                            onFinished()
                        }
                    )
                },
                bottomBar = {
                    CreateScreenBottomAppBar(
                        onButtonClick = {
                            viewModel.processCommand(CreatePostCommand.Save)
                        },
                        currentState = currentState
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .background(color = Orange5)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Тип *")

                    Spacer(modifier = Modifier.height(8.dp))

                    SelectStatusSegmentedButtons(
                        onSelectStatusClick = {
                            viewModel.processCommand(command = CreatePostCommand.SelectStatus(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Категория *")

                    SelectCategoryFilterChips(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        onSelectCategoryClick = {
                            viewModel.processCommand(command = CreatePostCommand.SelectCategory(it))
                            selectedChipCategory = it
                        },
                        categories = categories,
                        selectedChipCategory = selectedChipCategory
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LabelText(text = "Заголовок *")

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomOutlinedTextField(
                        value = currentState.title,
                        placeholderText = "Введите заголовок объявления",
                        onValueChange = {
                            viewModel.processCommand(CreatePostCommand.InputTitle(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Описание")

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomOutlinedTextField(
                        modifier = Modifier
                            .height(100.dp),
                        value = currentState.description,
                        placeholderText = "Особые приметы, размер, цвет",
                        onValueChange = {
                            viewModel.processCommand(CreatePostCommand.InputDescription(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Адрес *")

                    Spacer(modifier = Modifier.height(8.dp))

                    //

                    CustomOutlinedTextField(
                        value = currentState.address,
                        placeholderText = "Город, улица, метро",
                        onValueChange = {
                            viewModel.processCommand(CreatePostCommand.InputAddress(it))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // открыть карты
                                },
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_location_pin),
                                    contentDescription = null,
                                    tint = Gray500
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Контакт *")

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomOutlinedTextField(
                        value = currentState.contact,
                        placeholderText = "Как с вами связаться?",
                        onValueChange = {
                            viewModel.processCommand(CreatePostCommand.InputContact(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Фото")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentState.imageUrl == "") {
                        AddImageBox(
                            onAddImageClick = {
                                imagePicker.launch("image/*")

                            }
                        )
                    } else {
                        ShowImageBox(
                            imageUrl = currentState.imageUrl,
                            onDeleteImageClick = {
                                viewModel.processCommand(CreatePostCommand.DeleteImage)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText(text = "Вознаграждение")

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomOutlinedTextField(
                        value = currentState.reward,
                        placeholderText = "Например: 1000 Р (необязательно)",
                        keyboardType = KeyboardType.Phone,
                        onValueChange = {
                            viewModel.processCommand(CreatePostCommand.InputReward(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }

        }
        CreatePostState.Finished -> {
            onFinished()
        }
    }
}

@Composable
private fun CreateScreenBottomAppBar(
    onButtonClick: () -> Unit,
    currentState: CreatePostState.Creation
) {

    BottomAppBar(
        modifier = Modifier.shadow(4.dp).animateContentSize(),
        containerColor = White100
    ) {
        CustomTextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                onButtonClick()
            },
            enabled = currentState.isSaveEnable && !currentState.isLoading
        ) {
            if (currentState.isLoading) {

                CircularProgressIndicator(modifier = Modifier.size(24.dp))

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Создание...",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Опубликовать",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ShowImageBox(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onDeleteImageClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = White100)
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .heightIn(max = 250.dp),
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .align(Alignment.TopEnd)
                .background(color = Gray150, shape = CircleShape)
                .clip(CircleShape)
                .clickable { onDeleteImageClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                contentDescription = null,
                tint = Gray500
            )
        }
    }
}

@Composable
private fun AddImageBox(
    modifier: Modifier = Modifier,
    onAddImageClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = White100),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(4.dp))

            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_add_image),
                tint = Orange300,
                contentDescription = null
            )

            Spacer(Modifier.height(4.dp))

            CustomTextButton(
                modifier = Modifier.height(36.dp),
                onClick = {onAddImageClick()}
            ) {
                Text(text = "Добавить фото",
                    color = White100
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = "JPG, PNG до 10 МБ",
                color = Gray500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun LabelText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(horizontal = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}
