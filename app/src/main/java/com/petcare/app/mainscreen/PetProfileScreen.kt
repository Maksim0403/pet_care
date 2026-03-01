package com.petcare.app.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.data.animals
import com.petcare.app.models.Pet
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ColorBackground
import com.petcare.app.ui.theme.ColorPrimary
import com.petcare.app.ui.theme.ColorPrimaryLight
import com.petcare.app.ui.theme.Typography

@Composable
fun PetProfileScreen(modifier: Modifier = Modifier, pet: Pet) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        BackButton(modifier = Modifier.padding(top = 16.dp, start = 16.dp))

        PetImage(imageResId = pet.imageResId ?: 0)

        PetInfo(pet = pet)
    }
}

@Composable
fun BackButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
        )
    }
}

@Composable
fun PetImage(modifier: Modifier = Modifier, imageResId: Int) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(340.dp)
                .background(
                    color = ColorPrimaryLight,
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    color = ColorPrimary,
                    shape = CircleShape
                )
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            painter = painterResource(imageResId),
            contentDescription = null,
        )
    }
}

@Composable
fun PetInfo(modifier: Modifier = Modifier, pet: Pet) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalDivider(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray)
        )

        Spacer(Modifier.height(16.dp))
        PetTitle(pet.name)
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PetInfoItem(modifier = Modifier.weight(1f), title = "Age", text = "${pet.age} years")
            PetInfoItem(modifier = Modifier.weight(1f), title = "Weight", text = "${pet.weight} kg")
            PetInfoItem(modifier = Modifier.weight(1f), title = "Breed", text = "${pet.breed}")
        }

        Spacer(Modifier.height(16.dp))
        PetTitle(text = "Summary")
        Spacer(Modifier.height(16.dp))
        Text(text = pet.summary, style = Typography.bodyMedium, color = Color.DarkGray)
        Spacer(Modifier.height(16.dp))

        PetButton()
    }
}

@Composable
private fun PetButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = ButtonColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Contact me",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
    }
}

@Composable
private fun PetTitle(text: String) {
    Text(
        text = text,
        style = Typography.titleLarge,
        color = Color.Black,
        fontWeight = FontWeight(500),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PetInfoItem(modifier: Modifier = Modifier, title: String, text: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = Typography.bodyMedium, color = Color.DarkGray)
        Text(
            text = text,
            style = Typography.bodyLarge,
            color = Color.Black,
            fontWeight = FontWeight(500)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetProfileScreenPreview() {
    PetProfileScreen(pet = animals[0])
}