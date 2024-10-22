package com.abdts.search.ui.screens.home.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RecipeLogo(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Tasty Table",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(id = com.abdts.common.R.drawable.ingredients),
            contentDescription = null,
            modifier = Modifier.size(70.dp)
        )

    }
}