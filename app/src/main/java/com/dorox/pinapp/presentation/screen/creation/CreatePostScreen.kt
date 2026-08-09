

package com.dorox.pinapp.presentation.screen.creation

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray200
import com.dorox.pinapp.ui.theme.Gray400
import com.dorox.pinapp.ui.theme.Gray50
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.White100


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    innerPadding: PaddingValues,
    viewModel: CreatePostViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.processCommand(CreatePostCommand.AddImage(uri = it))
            }
        }
    )

    when (val currentState = state.value) {
        is CreatePostState.Creation -> {

                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Тип *")
                    SelectStatusSegmentedButton(
                        onSelectStatusClick = {
                            viewModel.processCommand(command = CreatePostCommand.SelectStatus(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Категория *")

                    SelectCategoryFilterChips(
                        onSelectCategoryClick = {
                            viewModel.processCommand(command = CreatePostCommand.SelectCategory(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Заголовок *")

                    CustomOutlinedTextField(
                        text = currentState.title,
                        placeholderText = "Например: Потерялся кот Пушок",
                        onTextChanged = {
                            viewModel.processCommand(CreatePostCommand.InputTitle(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Описание *")

                    CustomOutlinedTextField(
                        text = currentState.description,
                        placeholderText = "Особые приметы, размер, цвет",
                        onTextChanged = {
                            viewModel.processCommand(CreatePostCommand.InputDescription(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Место *")

                    CustomOutlinedTextField(
                        text = currentState.address,
                        placeholderText = "Улица, метро",
                        onTextChanged = {
                            viewModel.processCommand(CreatePostCommand.InputAddress(it))
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_location_pin),
                                contentDescription = null,
                                tint = Black100
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Контакт *")

                    CustomOutlinedTextField(
                        text = currentState.contact,
                        placeholderText = "Номер телефона или email",
                        onTextChanged = {
                            viewModel.processCommand(CreatePostCommand.InputContact(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelText("Фото")

                    if (currentState.imageUrl.isEmpty()) {
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

                    LabelText("Вознаграждение")

                    CustomOutlinedTextField(
                        text = currentState.reward,
                        placeholderText = "Например: 1000 Р (необязательно)",
                        onTextChanged = {
                            viewModel.processCommand(CreatePostCommand.InputContact(it))
                        }
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    ConfirmTextButton(
                        enabled = currentState.isSaveEnable,
                        onSavePostClick = {
                            viewModel.processCommand(CreatePostCommand.Save)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        CreatePostState.Finished -> {
            onFinished()
        }
    }
}

@Composable
fun ConfirmTextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onSavePostClick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth(),
        enabled = enabled,
        shape = CircleShape,
        onClick = {
            onSavePostClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange200,
            contentColor = White100,
            disabledContentColor = Gray200,
            disabledContainerColor = Gray50
        ),
        elevation = buttonElevation(
            defaultElevation = 2.dp
        )

    ) {
        Text(
            text = "Разместить объявление",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
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
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = Orange200,
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),

            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable {
                    onDeleteImageClick()
                },
            imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
            contentDescription = null,
            tint = Gray400
        )
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
            .clickable {
                onAddImageClick()
            }
            .background(color = Orange100.copy(0.5f))
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = Orange200,
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_add_image),
                tint = Orange200,
                contentDescription = null
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Добавить фото",
                color = Orange200
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = "JPG, PNG до 10 МБ",
                color = Gray100,
                fontSize = 12.sp
            )
        }


    }
}

@Composable
private fun SelectCategoryFilterChips(
    onSelectCategoryClick: (Category) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(Category.NOT_SELECTED) }
    val categories = Category.entries.filter { it != Category.NOT_SELECTED }.toList()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        itemVerticalAlignment = Alignment.Top
    ) {
        categories.forEachIndexed { index, category ->
            FilterChip(
                modifier = Modifier.padding(end = 4.dp),
                selected = selectedCategory == category,
                onClick = {
                    selectedCategory = category
                    onSelectCategoryClick(selectedCategory)
                },
                label = {
                    Text(
                        text = category.text,
                        fontSize = 14.sp
                    )
                },
                shape = CircleShape,
                colors = filterChipColors(
                    containerColor = White100,
                    selectedContainerColor = Orange100,
                    selectedLabelColor = Orange200
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedCategory == category,
                    selectedBorderColor = Orange200,
                    borderWidth = 2.dp
                )
            )
        }
    }
}

@Composable
private fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholderText: String,
    onTextChanged: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 16.sp
            )
        },
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedPlaceholderColor = Gray100,
            focusedPlaceholderColor = Gray100
        )
    )
}

@Composable
private fun LabelText(
    text: String
) {
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = text,
        color = Black100,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )

}

@Composable
private fun SelectStatusSegmentedButton(
    onSelectStatusClick: (Status) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(Status.NOT_SELECTED) }
    val statuses = Status.entries.filter { it != Status.NOT_SELECTED }.toList()

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        statuses.forEachIndexed { index, status ->
            SegmentedButton(
                modifier = Modifier.height(70.dp),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = statuses.size,
                    baseShape = RoundedCornerShape(16.dp)
                ),
                onClick = {
                    selectedStatus = status
                    onSelectStatusClick(selectedStatus)
                },
                selected = status == selectedStatus,
                label = {
                    Text(
                        text = status.text,
                        fontSize = 18.sp
                    )
                },
                icon = {
                    SegmentedButtonDefaults.Icon(
                        active = false
                    )
                },
                border = BorderStroke(
                    width = 2.dp,
                    color = Orange200
                ),
                colors = SegmentedButtonDefaults.colors()
            )
        }
    }
}



//@Preview
//@Composable
//fun PreviewCreatePostScreen() {
//    PinAppTheme() {
//
//        val previewPadding = PaddingValues(
//            start = 16.dp,
//            top = 16.dp,
//            end = 16.dp,
//            bottom = 16.dp
//        )
//
////        CreatePostScreen(
////            paddingValues = previewPadding
////        )
//        CreatePostScreen(
//            paddingValues = PaddingValues(),
//            onFinished = {  }
//        )
//    }
//}

