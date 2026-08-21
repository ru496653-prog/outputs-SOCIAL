package com.example.outputs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.outputs.data.model.PrivacyMode
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandPurpleLight

@Composable
fun IdentityBadge(
    identityMode: PrivacyMode,
    authorName: String,
    anonymousNumberCode: String = "#4827",
    modifier: Modifier = Modifier
) {
    val (badgeBg, badgeBorder, icon, displayName) = when (identityMode) {
        PrivacyMode.REAL_PROFILE -> BadgeVisualConfig(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            Icons.Default.AccountCircle,
            authorName
        )
        PrivacyMode.PSEUDONYM -> BadgeVisualConfig(
            BrandPurpleLight.copy(alpha = 0.15f),
            BrandPurpleLight.copy(alpha = 0.4f),
            Icons.Default.Person,
            authorName
        )
        PrivacyMode.ANONYMOUS_USERNAME -> BadgeVisualConfig(
            BrandCyan.copy(alpha = 0.15f),
            BrandCyan.copy(alpha = 0.4f),
            Icons.Default.Lock,
            authorName
        )
        PrivacyMode.ANONYMOUS_NUMBER -> BadgeVisualConfig(
            BrandAmber.copy(alpha = 0.15f),
            BrandAmber.copy(alpha = 0.5f),
            Icons.Default.Tag,
            "Anonymous $anonymousNumberCode"
        )
        PrivacyMode.COMPLETELY_ANONYMOUS -> BadgeVisualConfig(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            Icons.Default.VisibilityOff,
            "Anonymous"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(badgeBg)
            .border(1.dp, badgeBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = identityMode.label,
            modifier = Modifier.size(13.dp),
            tint = when (identityMode) {
                PrivacyMode.ANONYMOUS_NUMBER -> BrandAmber
                PrivacyMode.ANONYMOUS_USERNAME -> BrandCyan
                PrivacyMode.PSEUDONYM -> BrandPurpleLight
                PrivacyMode.COMPLETELY_ANONYMOUS -> MaterialTheme.colorScheme.onSurfaceVariant
                else -> MaterialTheme.colorScheme.primary
            }
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = displayName,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private data class BadgeVisualConfig<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
