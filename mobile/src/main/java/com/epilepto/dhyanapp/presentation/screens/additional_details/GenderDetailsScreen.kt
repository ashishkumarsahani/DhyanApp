package com.epilepto.dhyanapp.presentation.screens.additional_details

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
import com.epilepto.dhyanapp.presentation.components.GradientButton
import com.epilepto.dhyanapp.theme.textFieldContainer
import com.epilepto.dhyanapp.utils.Constants


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
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.dhyan_logo_with_name),
                contentDescription = "dhyan_logo"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Please fill out true information!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Age",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(12.dp))
            TextField(
                value = age,
                onValueChange = { onAgeChange(it.trim().take(2)) },
                label = { Text(text = "") },
                modifier = Modifier
                    .fillMaxWidth(.25f)
                    .shadow(elevation = 12.dp),
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
                shape = ShapeDefaults.Medium,
                maxLines = 1,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
            )
        }

        /*        OutlinedButton(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = Shapes().small,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(text = "Next")
                }*/

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            GradientButton(
                title = "Next",
                onClick = onNext
            )
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
                modifier = Modifier
                    .weight(.3f),
                gender = "Male",
                imageRes = R.drawable.male_icon,
                onSelect = onSelect,
                textColor = Color.Blue,
                isSelected = gender == "Male",
                textStyle = MaterialTheme.typography.titleMedium
            )
            GenderCard(
                modifier = Modifier.weight(.3f),
                gender = "Female",
                imageRes = R.drawable.female_icon,
                onSelect = onSelect,
                textColor = Color.Red,
                isSelected = gender == "Female",
                textStyle = MaterialTheme.typography.titleMedium
            )

            GenderCard(
                modifier = Modifier.weight(0.3f),
                gender = "Other",
                imageRes = R.drawable.other_gender,
                onSelect = onSelect,
                textColor = Color.Gray,
                isSelected = gender == "Other",
                textStyle = MaterialTheme.typography.titleMedium,
            )

        }

/*        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            GenderCard(
                modifier = Modifier.fillMaxWidth(0.3f),
                gender = "Other",
                imageRes = R.drawable.others_icon,
                onSelect = onSelect,
                textColor = Color.Gray,
                isSelected = gender == "Other",
                textStyle = MaterialTheme.typography.titleMedium
            )
        }*/
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
                ?: Color(0xffF4F5F7)
        )
        else
            CardDefaults.cardColors()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = gender,
                contentScale = ContentScale.FillBounds,
                modifier = if(imageRes == R.drawable.other_gender){
                    Modifier.scale(1.5f)
                }else Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gender,
                color = textColor,
                modifier = Modifier.padding(6.dp),
                style = textStyle
            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
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