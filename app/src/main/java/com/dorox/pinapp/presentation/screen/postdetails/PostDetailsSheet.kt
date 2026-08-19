package com.dorox.pinapp.presentation.screen.postdetails

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.utils.toDateString
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray200
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.InfoBoxText
import com.dorox.pinapp.ui.theme.InfoBoxTextWithIcon
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsSheet(
    modifier: Modifier = Modifier,
    viewModel: PostDetailsViewModel
) {

    val state by viewModel.state.collectAsState()
    //val currentPost = state.post
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val statusBackgroundColor = if (state.post.status == Status.FOUND.text) Green100 else Red100
            val statusTextColor = if (statusBackgroundColor == Green100) Green200 else Red200

            Row() {
                InfoBoxText(
                    text = state.post.status,
                    colorText = statusTextColor,
                    colorBackground = statusBackgroundColor,
                )

                Spacer(Modifier.width(4.dp))

                InfoBoxText(
                    text = state.post.category,
                    colorText = Gray500,
                    colorBackground = Gray200,
                )
            }

            InfoBoxText(
                text = state.post.createdAt.toDateString(),
                colorText = Gray500,
                colorBackground = Color.Transparent,
                horizontalDp = 0.dp,
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = state.post.title,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = state.post.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        if (state.post.imageUrl != "") {
            state.post.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .heightIn(max = 250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    //placeholder = painterResource(R.drawable.ic_add_image),
                    error = painterResource(R.drawable.ic_clear),
                )
            } ?: run {
                CircularProgressIndicator()
            }
        }


        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Gray200
        )

        Spacer(Modifier.height(8.dp))

        InfoBoxTextWithIcon(
            titleInfo = "Адрес",
            descriptionInfo = state.post.address,
            imageVector = ImageVector.vectorResource(R.drawable.ic_location_pin)
        )

        Spacer(Modifier.height(8.dp))

        InfoBoxTextWithIcon(
            titleInfo = "Контакт",
            descriptionInfo = state.post.contact,
            imageVector = ImageVector.vectorResource(R.drawable.ic_call)
        )

        Spacer(Modifier.height(16.dp))

        CustomTextButton(
            onClick = {

            },
            text = "Связаться"
        )
        Spacer(Modifier.height(16.dp))
    }
    }
