package com.petcare.app.pet_api.add_pet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import com.petcare.app.components.BackButton
import com.petcare.app.models.PetType


@Composable
fun ApiPetAddScreen(
    modifier: Modifier = Modifier,
    onPetAdded: (() -> Unit)? = null
) {
    val viewModel: ApiPetAddViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val windowInfo = currentWindowAdaptiveInfo()
    val isExpanded =
        windowInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // Tap outside → hide keyboard & clear focus
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        contentAlignment = Alignment.TopCenter
    ) {
        val contentModifier = if (isExpanded)
            Modifier
                .widthIn(max = 700.dp)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        else
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        Column(
            modifier = contentModifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(modifier = Modifier)
                Text(
                    text = "Add Animal",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "✓ Connected to MockAPI Pet Service. Your data will be persisted in the API.",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            when (val s = state) {
                is ApiPetAddState.Success ->
                    SuccessState(onPetAdded)

                is ApiPetAddState.Error ->
                    ErrorBanner(s.message)

                ApiPetAddState.Loading ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                ApiPetAddState.Idle ->
                    FormContent(viewModel = viewModel, isExpanded = isExpanded)
            }
        }
    }
}


@Composable
private fun FormContent(
    viewModel: ApiPetAddViewModel,
    isExpanded: Boolean
) {
    val petName      by viewModel.petName.collectAsStateWithLifecycle()
    val petAge       by viewModel.petAge.collectAsStateWithLifecycle()
    val petWeight    by viewModel.petWeight.collectAsStateWithLifecycle()
    val petBreed     by viewModel.petBreed.collectAsStateWithLifecycle()
    val ownerEmail   by viewModel.ownerEmail.collectAsStateWithLifecycle()
    val ownerPhone   by viewModel.ownerPhone.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val isVaccinated by viewModel.isVaccinated.collectAsStateWithLifecycle()
    val healthScore  by viewModel.healthScore.collectAsStateWithLifecycle()
    val errors       by viewModel.errors.collectAsStateWithLifecycle()
    val isFormValid  by viewModel.isFormValid.collectAsStateWithLifecycle()

    val focusName  = remember { FocusRequester() }
    val focusBreed = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val focusPhone = remember { FocusRequester() }
    val keyboard   = LocalSoftwareKeyboardController.current

    SectionCard(title = "Basic Information") {
        if (isExpanded) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(
                    modifier = Modifier.weight(1f).focusRequester(focusName),
                    value = petName,
                    onValueChange = { viewModel.updateName(it) },
                    label = "Animal Name *",
                    error = errors[FormField.NAME],
                    onFocusLost = { viewModel.validateField(FormField.NAME) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusBreed.requestFocus() })
                )
                ValidatedTextField(
                    modifier = Modifier.weight(1f).focusRequester(focusBreed),
                    value = petBreed,
                    onValueChange = { viewModel.updateBreed(it) },
                    label = "Breed",
                    error = errors[FormField.BREED],
                    onFocusLost = { viewModel.validateField(FormField.BREED) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusEmail.requestFocus() })
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumericTextField(
                    modifier = Modifier.weight(1f),
                    value = petAge,
                    onValueChange = { viewModel.updateAge(it) },
                    label = "Age (years) *",
                    error = errors[FormField.AGE],
                    onFocusLost = { viewModel.validateField(FormField.AGE) }
                )
                NumericTextField(
                    modifier = Modifier.weight(1f),
                    value = petWeight,
                    onValueChange = { viewModel.updateWeight(it) },
                    label = "Weight (kg) *",
                    error = errors[FormField.WEIGHT],
                    onFocusLost = { viewModel.validateField(FormField.WEIGHT) }
                )
            }
        } else {
            ValidatedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusName),
                value = petName,
                onValueChange = { viewModel.updateName(it) },
                label = "Animal Name *",
                error = errors[FormField.NAME],
                onFocusLost = { viewModel.validateField(FormField.NAME) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusBreed.requestFocus() })
            )
            ValidatedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusBreed),
                value = petBreed,
                onValueChange = { viewModel.updateBreed(it) },
                label = "Breed",
                error = errors[FormField.BREED],
                onFocusLost = { viewModel.validateField(FormField.BREED) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusEmail.requestFocus() })
            )
            NumericTextField(
                modifier = Modifier.fillMaxWidth(),
                value = petAge,
                onValueChange = { viewModel.updateAge(it) },
                label = "Age (years) *",
                error = errors[FormField.AGE],
                onFocusLost = { viewModel.validateField(FormField.AGE) }
            )
            NumericTextField(
                modifier = Modifier.fillMaxWidth(),
                value = petWeight,
                onValueChange = { viewModel.updateWeight(it) },
                label = "Weight (kg) *",
                error = errors[FormField.WEIGHT],
                onFocusLost = { viewModel.validateField(FormField.WEIGHT) }
            )
        }

        PetTypeDropdown(
            modifier = Modifier.fillMaxWidth(),
            selectedType = selectedType,
            onTypeChange = { viewModel.updateType(it) },
            error = errors[FormField.TYPE],
            onFocusLost = { viewModel.validateField(FormField.TYPE) }
        )
    }

    SectionCard(title = "Owner Information") {
        if (isExpanded) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(
                    modifier = Modifier.weight(1f).focusRequester(focusEmail),
                    value = ownerEmail,
                    onValueChange = { viewModel.updateOwnerEmail(it) },
                    label = "Owner Email *",
                    error = errors[FormField.EMAIL],
                    onFocusLost = { viewModel.validateField(FormField.EMAIL) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusPhone.requestFocus() })
                )
                ValidatedTextField(
                    modifier = Modifier.weight(1f).focusRequester(focusPhone),
                    value = ownerPhone,
                    onValueChange = { viewModel.updateOwnerPhone(it) },
                    label = "Owner Phone *",
                    error = errors[FormField.PHONE],
                    onFocusLost = { viewModel.validateField(FormField.PHONE) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
                )
            }
        } else {
            ValidatedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusEmail),
                value = ownerEmail,
                onValueChange = { viewModel.updateOwnerEmail(it) },
                label = "Owner Email *",
                error = errors[FormField.EMAIL],
                onFocusLost = { viewModel.validateField(FormField.EMAIL) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusPhone.requestFocus() })
            )
            ValidatedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusPhone),
                value = ownerPhone,
                onValueChange = { viewModel.updateOwnerPhone(it) },
                label = "Owner Phone *",
                error = errors[FormField.PHONE],
                onFocusLost = { viewModel.validateField(FormField.PHONE) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }

    SectionCard(title = "Additional Parameters") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Vaccinated", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Animal has up-to-date vaccinations",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isVaccinated,
                onCheckedChange = { viewModel.updateIsVaccinated(it) }
            )
        }

        Spacer(Modifier.height(4.dp))

        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Health Score", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "${healthScore.toInt()} / 10",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = healthScore,
                onValueChange = { viewModel.updateHealthScore(it) },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Spacer(Modifier.height(8.dp))
    Button(
        onClick = { viewModel.submitForm() },
        modifier = Modifier.fillMaxWidth().height(52.dp),
        enabled = isFormValid
    ) {
        Text("Save Animal")
    }

    Text(
        text = "* Required fields",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun ValidatedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    onFocusLost: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (!it.isFocused) onFocusLost() },
            label = { Text(label) },
            singleLine = true,
            isError = error != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun NumericTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    onFocusLost: () -> Unit,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (!it.isFocused) onFocusLost() },
            label = { Text(label) },
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = imeAction
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun PetTypeDropdown(
    modifier: Modifier = Modifier,
    selectedType: PetType,
    onTypeChange: (PetType) -> Unit,
    error: String?,
    onFocusLost: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = if (selectedType == PetType.ALL) "" else selectedType.title

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (!it.isFocused) onFocusLost() }
        ) {
            OutlinedTextField(
                value = displayValue,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                label = { Text("Type *") },
                readOnly = true,
                isError = error != null,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { expanded = true }
                    )
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                PetType.entries
                    .filter { it != PetType.ALL }
                    .forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.title) },
                            onClick = {
                                onTypeChange(type)
                                expanded = false
                            }
                        )
                    }
            }
        }
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun SuccessState(onPetAdded: (() -> Unit)?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✓ Animal created successfully!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            onPetAdded?.invoke()
        }) {
            Text("Back to List")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ApiPetAddScreenPreview() {
    MaterialTheme {
        ApiPetAddScreen()
    }
}