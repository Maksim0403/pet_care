package com.petcare.app.pet_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.components.BackButton
import com.petcare.app.components.PetImage
import com.petcare.app.components.PetInfo
import com.petcare.app.data.MockPetRepository
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet

// ─────────────────────────────────────────────────────────────────
//  Screen entry point
// ─────────────────────────────────────────────────────────────────

@Composable
fun PetProfileScreen(modifier: Modifier = Modifier, petId: Int, onBackClicked: () -> Unit) {
    val viewModel = viewModel<PetProfileViewModel>(
        factory = PetProfileViewModel.Factory(petId)
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        when (val s = state) {
            is PetDetailState.Loading -> LoadingScreen(onBackClicked)
            is PetDetailState.Success -> PetProfile(
                modifier = modifier,
                pet = s.pet,
                relatedPets = s.relatedPets,
                statistics = s.statistics,
                onBackClicked = onBackClicked
            )

            is PetDetailState.Error -> ErrorScreen(s.message, onBackClicked)
        }
    }
}

// ─────────────────────────────────────────────────────────────────
//  PetProfile — contains Завдання 2 expandable section
// ─────────────────────────────────────────────────────────────────

@Composable
internal fun PetProfile(
    modifier: Modifier = Modifier,
    pet: Pet,
    relatedPets: List<Pet> = listOf(),
    showBackButton: Boolean = true,
    statistics: String = "",
    onBackClicked: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showBackButton) {
            BackButton(
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onBackClicked?.invoke() }
            )
        }

        PetImage(modifier = Modifier, imageResId = pet.imageResId ?: 0)
        PetInfo(pet = pet)

        Text(text = "Statistics", style = MaterialTheme.typography.headlineSmall)
        Text(text = statistics, style = MaterialTheme.typography.bodyMedium)

        // ── Завдання 2 — Animated expandable section ──────────────
        ExpandableDetailsSection(pet = pet)

        // Related pets
        if (relatedPets.isNotEmpty()) {
            Text(text = "Related Pets", style = MaterialTheme.typography.headlineSmall)
            relatedPets.forEach { related ->
                Text(text = related.name, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
//  Завдання 2 — Expandable section with animated header + content
// ─────────────────────────────────────────────────────────────────

@Composable
private fun ExpandableDetailsSection(pet: Pet) {
    var expanded by remember { mutableStateOf(false) }

    // 1. Arrow rotation animation
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
        label = "arrow_rotation"
    )

    // 2. Header background color animation
    val headerColor by animateColorAsState(
        targetValue = if (expanded)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "header_color"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Header row — clickable, animates color + arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Additional Details",
                style = MaterialTheme.typography.titleMedium,
                color = if (expanded)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            // 3. Rotating arrow icon
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.rotate(arrowRotation),
                tint = if (expanded)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Animated content visibility — height + fade
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(250)) + expandVertically(tween(300, easing = EaseOutCubic)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(250, easing = EaseInCubic))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailRow(label = "Summary", value = pet.summary.ifEmpty { "No summary" })
                DetailRow(label = "Breed", value = pet.breed ?: "Unknown")
                DetailRow(label = "Favorite", value = if (pet.isFavorite) "Yes ★" else "No")
                DetailRow(label = "ID", value = "#${pet.id}")

                // Type-specific extra fields
                when (pet) {
                    is Dog -> {
                        HorizontalDivider()
                        DetailRow(label = "Trained", value = if (pet.isTrained) "Yes" else "No")
                    }

                    is Parrot -> {
                        HorizontalDivider()
                        DetailRow(label = "Wingspan", value = "${pet.wingSpan} cm")
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.6f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────
//  Loading / Error screens
// ─────────────────────────────────────────────────────────────────

@Composable
private fun LoadingScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 16.dp)
                .clickable { onBackClicked() }
        )
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(message: String, onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 16.dp)
                .clickable { onBackClicked() }
        )
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

// ─────────────────────────────────────────────────────────────────
//  Previews
// ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun PetProfileScreenPreview() {
    PetProfileScreen(petId = MockPetRepository.getAllPets()[0].id, onBackClicked = {})
}

@Preview(showBackground = true)
@Composable
private fun ExpandableSectionPreview() {
    MaterialTheme {
        ExpandableDetailsSection(
            pet = Cat(
                id = 1, name = "Tom", age = 5, weight = 3.5, breed = "Tabby",
                summary = "A friendly tabby cat.", isFavorite = true
            )
        )
    }
}