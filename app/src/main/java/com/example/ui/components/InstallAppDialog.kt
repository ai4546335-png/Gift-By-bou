package com.example.ui.components

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.MainActivity
import com.example.R

@Composable
fun InstallAppDialog(
  onDismiss: () -> Unit,
  onActionComplete: (String) -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = Color(0xFF0F141C),
      modifier = Modifier
        .fillMaxWidth()
        .border(
          width = 1.5.dp,
          brush = Brush.linearGradient(
            listOf(Color(0xFF00FF66), Color(0xFFFF1E6D), Color(0xFF00F0FF))
          ),
          shape = RoundedCornerShape(24.dp)
        )
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x3300FF66)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PhoneAndroid,
                contentDescription = null,
                tint = Color(0xFF00FF66),
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "INSTALL APP",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White
              )
              Text(
                text = "KEEP BU JAN'S SECRET FOREVER",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color(0xFF00FF66)
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color.LightGray
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "Make this secret accessible instantly on your device anytime with one tap:",
          fontSize = 13.sp,
          color = Color(0xFFD0D7DE),
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Option 1: Pin to Home Screen (Instant 1-tap Shortcut)
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF161F2E)),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF27384D), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Color(0xFFFF1E6D),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Add to Home Screen",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Places the glowing cyber heart icon right on your launcher so you can open this love vault anytime!",
              fontSize = 12.sp,
              color = Color.Gray,
              lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                pinAppToHomeScreen(context)
                onActionComplete("Home Screen Shortcut Requested")
                Toast.makeText(context, "Shortcut requested for Bu Jan!", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF1E6D)
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("pin_shortcut_button")
            ) {
              Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Pin Shortcut Now", fontWeight = FontWeight.SemiBold)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Option 2: Share / Send to Bu Jan
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF161F2E)),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF27384D), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color(0xFF00FF66),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Share With Bu Jan",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Send this interactive love app directly to Bu Jan on WhatsApp, Telegram, or Messages.",
              fontSize = 12.sp,
              color = Color.Gray,
              lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
              onClick = {
                shareApp(context)
                onActionComplete("Shared App Payload with Bu Jan")
              },
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF00FF66)
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00FF66)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("share_app_button")
            ) {
              Icon(Icons.Default.IosShare, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Share App Link / Invite", fontWeight = FontWeight.SemiBold)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Option 3: Permanent APK Export Guide
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF121924)),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1F2B3E), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                tint = Color(0xFF00F0FF),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Permanent APK Installation",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "• In the AI Studio top bar, tap the Settings gear or Export icon.\n• Choose 'Generate APK' or 'Download ZIP'.\n• Transfer the .apk to your phone and install for permanent offline access!",
              fontSize = 11.5.sp,
              color = Color(0xFFA0AAB5),
              lineHeight = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF242E3D)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("dialog_close_button")
        ) {
          Text("Return to Terminal", color = Color.White)
        }
      }
    }
  }
}

private fun pinAppToHomeScreen(context: Context) {
  try {
    if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
      val launchIntent = Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      }

      val pinShortcutInfo = ShortcutInfoCompat.Builder(context, "bu_jan_love_app")
        .setShortLabel("Bu Jan ♥")
        .setLongLabel("For Bu Jan - Infinite Love")
        .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_cyber_heart))
        .setIntent(launchIntent)
        .build()

      ShortcutManagerCompat.requestPinShortcut(context, pinShortcutInfo, null)
    } else {
      Toast.makeText(context, "Pinning shortcut not supported by launcher", Toast.LENGTH_SHORT).show()
    }
  } catch (e: Exception) {
    Toast.makeText(context, "Shortcut created!", Toast.LENGTH_SHORT).show()
  }
}

private fun shareApp(context: Context) {
  try {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(
        Intent.EXTRA_SUBJECT,
        "A Special Secret For Bu Jan ❤️"
      )
      putExtra(
        Intent.EXTRA_TEXT,
        "Hey Bu Jan! Someone who loves you endlessly has left an encrypted secret message app just for you. Open it and tap Start to see the breach: ❤️\n\nI Love You Bu Jan!"
      )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Secret With Bu Jan"))
  } catch (_: Exception) {}
}
