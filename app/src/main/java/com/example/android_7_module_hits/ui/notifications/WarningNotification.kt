package com.example.android_7_module_hits.ui.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.android_7_module_hits.R
import com.example.android_7_module_hits.ui.theme.NotificationTextStyle
import com.example.android_7_module_hits.ui.theme.NotificationTitleStyle
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.cardCornerRadius
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.cardHeight
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.cardPadding
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.horizontalMargin
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.iconButtonSize
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.iconSpacing
import com.example.android_7_module_hits.ui.theme.NotificationsDimens.titleBottomSpacerHeight
import com.example.android_7_module_hits.ui.theme.WarningNotificationBackgroundColor
import com.example.android_7_module_hits.ui.theme.WarningNotificationTextColor

data class WarningNotification(
    val onDismiss: () -> Unit = {}
) : UiNotification() {
    override val backgroundColor: Color = WarningNotificationBackgroundColor
    override val textColor: Color = WarningNotificationTextColor

    @Composable
    override fun Render() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalMargin)
                .height(cardHeight),
            shape = RoundedCornerShape(cardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(cardPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = stringResource(id = R.string.warning_icon_description),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(iconSpacing))
                    Text(
                        text = stringResource(id = R.string.warning_title),
                        style = NotificationTitleStyle,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .size(iconButtonSize)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.notification_dismiss_icon),
                            tint = textColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(titleBottomSpacerHeight))
                Text(
                    text = stringResource(id = R.string.warning_text),
                    style = NotificationTextStyle,
                    color = textColor
                )
            }
        }
    }
}