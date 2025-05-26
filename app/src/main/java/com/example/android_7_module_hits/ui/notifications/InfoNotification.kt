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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.Info
import com.example.android_7_module_hits.ui.theme.InfoNotificationBackgroundColor
import com.example.android_7_module_hits.ui.theme.InfoNotificationTextColor
import com.example.android_7_module_hits.ui.theme.NotificationTextStyle
import com.example.android_7_module_hits.ui.theme.NotificationTitleStyle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

data class InfoNotification(
    override val message: String = "Project is saved",
    val onDismiss: () -> Unit = {}
) : UiNotification() {
    override val backgroundColor: Color = InfoNotificationBackgroundColor
    override val textColor: Color = InfoNotificationTextColor

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
        Card(
            modifier = Modifier
                .width(384.dp)
                .height(104.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Icon",
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Info",
                        style = NotificationTitleStyle,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Dismiss Notification",
                            tint = textColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = NotificationTextStyle,
                    color = textColor
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun InfoNotificationPreview() {
    MaterialTheme {
        InfoNotification(onDismiss = {}).Render()
    }
}

