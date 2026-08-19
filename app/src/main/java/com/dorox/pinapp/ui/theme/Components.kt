package com.dorox.pinapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange200,
            contentColor = White100,
            disabledContentColor = Gray200,
            disabledContainerColor = Gray50
        ),
        enabled = enabled

    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoBoxText(
    modifier: Modifier = Modifier,
    text: String,
    colorText: Color,
    colorBackground: Color,
    contentAlignment: Alignment = Alignment.TopStart,
    horizontalDp: Dp = 8.dp,
    verticalDp: Dp = 4.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorBackground)
            .padding(horizontal = horizontalDp, vertical = verticalDp),
        contentAlignment = contentAlignment
    ) {
        Text(
            text = text,
            color = colorText,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun InfoBoxTextWithIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    titleInfo: String,
    descriptionInfo: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Gray400,
                shape = RoundedCornerShape(16.dp)
            )

    ) {
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Gray100
            )
            Column {
                Text(
                    text = titleInfo,
                    style = MaterialTheme.typography.titleMedium,
                    color = Gray500
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = descriptionInfo,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}