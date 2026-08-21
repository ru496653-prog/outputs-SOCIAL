package com.example.outputs.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.PrivacyMode
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurpleAccent
import com.example.ui.theme.BrandRed

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AgeGateScreen(
    onVerificationSuccess: (
        day: Int,
        month: Int,
        year: Int,
        citizenshipCountry: String,
        defaultPrivacyMode: PrivacyMode,
        publicUsername: String,
        pseudonym: String,
        interests: List<String>
    ) -> Boolean,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) }

    // Form states
    var dayText by remember { mutableStateOf("15") }
    var monthText by remember { mutableStateOf("06") }
    var yearText by remember { mutableStateOf("1998") }
    var citizenshipCountry by remember { mutableStateOf("United States") }
    var countryDropdownExpanded by remember { mutableStateOf(false) }

    var selectedPrivacyMode by remember { mutableStateOf(PrivacyMode.ANONYMOUS_NUMBER) }
    var usernameText by remember { mutableStateOf("ShadowWriter") }
    var pseudonymText by remember { mutableStateOf("TheNightArchivist") }

    val interestOptions = listOf(
        "#Mystery", "#RealIncident", "#Unexplained", "#History",
        "#Opinions", "#RadioCrypt", "#Science", "#Horror",
        "#UrbanExploration", "#Acoustics", "#Technology", "#Philosophy"
    )
    var selectedInterests by remember { mutableStateOf(listOf("#Mystery", "#RealIncident", "#Unexplained", "#History")) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val countries = listOf(
        "United States", "United Kingdom", "Canada", "Australia",
        "Nepal", "India", "Germany", "France", "Japan", "Other (Global)"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0C0E14),
                        Color(0xFF141724),
                        Color(0xFF1A132B)
                    )
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, BrandPurpleAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BrandPurpleAccent.copy(alpha = 0.2f))
                        .border(1.dp, BrandPurpleAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Age Verification",
                        tint = BrandPurpleAccent,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "OUTPUTS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = BrandPurpleAccent
                )

                Text(
                    text = "Mandatory 18+ Access Gate",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Privacy-First Age & Identity Verification",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // STEP 1: Date of Birth & Citizenship
                if (step == 1) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "1. DATE OF BIRTH (CONFIDENTIAL)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = BrandCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You must be 18 years or older. Your date of birth is strictly encrypted and never published.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = dayText,
                                onValueChange = { if (it.length <= 2) dayText = it },
                                label = { Text("Day") },
                                placeholder = { Text("DD") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = monthText,
                                onValueChange = { if (it.length <= 2) monthText = it },
                                label = { Text("Month") },
                                placeholder = { Text("MM") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = yearText,
                                onValueChange = { if (it.length <= 4) yearText = it },
                                label = { Text("Year") },
                                placeholder = { Text("YYYY") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1.5f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "2. CITIZENSHIP / REGION (PRIVATE)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = BrandCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Used solely to satisfy territorial adult age requirements. Never visible on posts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = countryDropdownExpanded,
                            onExpandedChange = { countryDropdownExpanded = !countryDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = citizenshipCountry,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Citizenship Country") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = countryDropdownExpanded,
                                onDismissRequest = { countryDropdownExpanded = false }
                            ) {
                                countries.forEach { country ->
                                    DropdownMenuItem(
                                        text = { Text(country) },
                                        onClick = {
                                            citizenshipCountry = country
                                            countryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = BrandRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BrandRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val d = dayText.toIntOrNull() ?: 0
                                val m = monthText.toIntOrNull() ?: 0
                                val y = yearText.toIntOrNull() ?: 0
                                if (d in 1..31 && m in 1..12 && y in 1900..2020) {
                                    step = 2
                                    errorMessage = null
                                } else {
                                    errorMessage = "Please enter a valid date of birth."
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("continue_to_privacy_mode"),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurpleAccent),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Verify Eligibility & Continue", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // STEP 2: Privacy Mode & Topics
                if (step == 2) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CHOOSE HOW YOU APPEAR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = BrandCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You can change your identity mode per post at any time.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        PrivacyMode.values().forEach { mode ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedPrivacyMode == mode) BrandPurpleAccent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .border(
                                        1.dp,
                                        if (selectedPrivacyMode == mode) BrandPurpleAccent else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedPrivacyMode = mode }
                                    .padding(10.dp)
                            ) {
                                RadioButton(
                                    selected = selectedPrivacyMode == mode,
                                    onClick = { selectedPrivacyMode = mode },
                                    colors = RadioButtonDefaults.colors(selectedColor = BrandPurpleAccent)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = mode.label,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = mode.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interest Topics
                        Text(
                            text = "CHOOSE YOUR INTERESTS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = BrandCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            interestOptions.forEach { interest ->
                                val isSelected = selectedInterests.contains(interest)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) BrandPurpleAccent else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            selectedInterests = if (isSelected) {
                                                selectedInterests - interest
                                            } else {
                                                selectedInterests + interest
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = interest,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val d = dayText.toIntOrNull() ?: 15
                                val m = monthText.toIntOrNull() ?: 6
                                val y = yearText.toIntOrNull() ?: 1998
                                val success = onVerificationSuccess(
                                    d, m, y,
                                    citizenshipCountry,
                                    selectedPrivacyMode,
                                    usernameText,
                                    pseudonymText,
                                    selectedInterests
                                )
                                if (!success) {
                                    errorMessage = "Outputs is available only to adults aged 18 and above."
                                    step = 1
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("enter_outputs_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enter Outputs Platform", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
