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
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_7_module_hits.ui.theme.NotificationTextStyle
import com.example.android_7_module_hits.ui.theme.NotificationTitleStyle
import com.example.android_7_module_hits.ui.theme.SuccessNotificationBackgroundColor
import com.example.android_7_module_hits.ui.theme.SuccessNotificationTextColor

data class SuccessNotification(
    override val message: String = "Successful compilation",
    val onDismiss: () -> Unit = {}
) : UiNotification() {
    override val backgroundColor: Color = SuccessNotificationBackgroundColor
    override val textColor: Color = SuccessNotificationTextColor

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
                        imageVector = Icons.Outlined.CheckBox,
                        contentDescription = "Success Icon",
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Success",
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
fun SuccessNotificationPreview() {
    MaterialTheme {
        SuccessNotification(onDismiss = {}).Render()
    }
}
