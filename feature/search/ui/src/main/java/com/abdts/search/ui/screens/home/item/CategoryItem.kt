package com.abdts.search.ui.screens.home.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CategoryItem(
    name: String,
    image: Int,
    onClick:()->Unit
) {

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(shape = CircleShape)
                .clickable { onClick() }
                .padding(8.dp)
                .background(color = Color.Gray.copy(alpha = .2f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(model = image, contentDescription = null, modifier = Modifier.size(55.dp))
        }

        Text(text = name)
    }

}