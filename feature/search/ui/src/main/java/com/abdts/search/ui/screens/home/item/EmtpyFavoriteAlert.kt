package com.abdts.search.ui.screens.home.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyFavoritesAlert() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray.copy(alpha = .1f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.StarOutline,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = .5f),
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White.copy(alpha = .2f), shape = CircleShape)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Favorite Recipes",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.Gray.copy(alpha = .8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You haven't added any recipes to your favorites yet",
                fontSize = 14.sp,
                color = Color.Gray.copy(alpha = .7f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}
