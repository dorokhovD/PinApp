package com.dorox.pinapp.presentation.screen.select_city

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dorox.pinapp.R
import com.dorox.pinapp.ui.theme.CustomOutlinedTextField
import com.dorox.pinapp.ui.theme.LoadingIndicator
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange5
import com.dorox.pinapp.ui.theme.White100


@Composable
fun SelectCitySheet(
    viewModel: SelectCityViewModel = hiltViewModel(),
    onCitySelect: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
    ) {
        CustomOutlinedTextField(
            value = state.query,
            label = {
                Text("Название города")
            },
            onValueChange = {
                viewModel.onQueryChange(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = Orange300
                )
            }
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.isLoading) {
                LoadingIndicator()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = White100,
                        shape = RoundedCornerShape(24.dp)
                    ),
                //verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.filteredCities,
                ) {city ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.clearQuery()
                                onCitySelect(city)
                            }
                            .padding(16.dp),
                        text = city,
                        maxLines = 1
                        )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = Orange5, thickness = 2.dp
                    )

                }
            }
        }




    }
}