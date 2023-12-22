package com.epilepto.dhyanapp.presentation.screens.additional_details

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.theme.textFieldContainer
import com.epilepto.dhyanapp.utils.Constants


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GenderInputScreen(
    age: String,
    onAgeChange: (String) -> Unit,
    gender: String,
    onGenderSelect: (String) -> Unit,
    onNext: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Please fill out true\ninformation!",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(12.dp))

            GenderMenu(
                gender = gender,
                onSelect = { selectedGender ->
                    onGenderSelect(selectedGender)
                }
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment =Alignment.CenterHorizontally

        ) {
            Text(
                text = "Enter Your Age",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            )
            Spacer(modifier = Modifier.padding(12.dp))
            TextField(
                value = age,
                onValueChange = { onAgeChange(it.trim().take(2)) },
                label = { Text(text = "Age") },
                modifier = Modifier
                    .width(100.dp),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = textFieldContainer,
                    unfocusedContainerColor = textFieldContainer,
                    focusedLabelColor = Color.Transparent
                ),
                shape = Shapes().medium,
                maxLines = 1,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
            )
        }

        OutlinedButton(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = Shapes().small,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun GenderMenu(
    gender: String,
    onSelect: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderCard(
                modifier = Modifier.weight(.5f),
                gender = "Male",
                imageRes = R.drawable.male_icon,
                onSelect = onSelect,
                textColor = Color.Blue,
                isSelected = gender == "Male",
            )
            GenderCard(
                modifier = Modifier.weight(.5f),
                gender = "Female",
                imageRes = R.drawable.female_icon,
                onSelect = onSelect,
                textColor = Color.Red,
                isSelected = gender == "Female",
            )

        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            GenderCard(
                modifier = Modifier.fillMaxWidth(0.5f),
                gender = "Other",
                imageRes = R.drawable.others_icon,
                onSelect = onSelect,
                textColor = Color.Gray,
                isSelected = gender == "Other",
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,

                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderCard(
    modifier: Modifier = Modifier,
    gender: String,
    imageRes: Int,
    onSelect: (String) -> Unit,
    isSelected: Boolean,
    textColor: Color = Color.Gray,
    imageSize: Dp? = null,
    textStyle: TextStyle = LocalTextStyle.current
) {
    val painter = painterResource(id = imageRes)
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, imageRes)
    val dominantColor = Constants.getDominantColor(bitmap)

    Card(
        modifier = modifier
            .then(
                if (imageSize != null) {
                    Modifier.size(imageSize)
                } else {
                    Modifier
                }
            )
            .padding(12.dp),
        onClick = { onSelect(gender) },
        colors = if (isSelected) CardDefaults.cardColors(
            containerColor = dominantColor?.copy(alpha = 0.4f)
                ?: LocalContentColor.current
        )
        else
            CardDefaults.cardColors()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = gender,
                color = textColor,
                modifier = Modifier.padding(6.dp),
                style = textStyle
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painter,
                contentDescription = gender,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}


@Preview
@Composable
fun DetailsPrev() {

    val (age, setAge) = rememberSaveable { mutableStateOf("") }
    val (gender, setGender) = rememberSaveable { mutableStateOf("Male") }

    GenderInputScreen(
        age = age,
        onAgeChange = setAge,
        gender = gender,
        onGenderSelect = setGender
    ) {

    }
}