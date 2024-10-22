package com.abdts.search.ui.screens.home.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PremiumAd(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .clip(shape = RoundedCornerShape(30.dp))
            .shadow(elevation = 30.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF00008B), Color(0xFF001F3F)) // Dark blue to navy
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Unlock \nUnlimited Recipes",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, color = Color.White),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(7.dp))

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Go Premium")
            }
        }
    }

}