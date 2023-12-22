package com.epilepto.dhyanapp.presentation.screens.main_screen.pranayama

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.CardHelperUtils
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.HelperCard
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.TimerCard
import com.epilepto.dhyanapp.theme.pranayamCardColor
import com.epilepto.dhyanapp.theme.pranayamaCardColors

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PranayamaScreenContent() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TimerCard(
            title = "Pranayama Timer",
            imagePainter = painterResource(id = R.drawable.meditation_timer_icon),
            time = 20,
            backgroundColor = pranayamCardColor,
            onClick = {}
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(
                items = CardHelperUtils.pranayamaCardTitles,
                key = { index,item->
                    item.title
                }) { index, item ->
                HelperCard(
                    title = item.title,
                    imagePainter = painterResource(id = item.icon),
                    time = 20,
                    backgroundColor = pranayamaCardColors[index],
                    onClick = {

                    }
                )
            }
        }
    }
}