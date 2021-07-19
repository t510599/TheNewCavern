package personal.secminhr.cavern.start

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import personal.secminhr.cavern.R
import personal.secminhr.cavern.main.MainActivity
import stoneapp.secminhr.cavern.cavernService.CavernCookieStore
import java.net.*
import kotlin.concurrent.thread

class StartActivity : AppCompatActivity() {

    private var showUpdateDialog by mutableStateOf(false)
    private var updateIsRequired = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            CookieHandler.setDefault(CookieManager(CavernCookieStore(applicationContext), CookiePolicy.ACCEPT_ALL))
        }

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.logo_img), null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth)
            }

            if (showUpdateDialog) {
                if (updateIsRequired) {
                    RequireUpdateDialog()
                } else {
                    SkippableUpdateDialog()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            checkUpdate(this@StartActivity)
        }
    }

    @Composable
    fun RequireUpdateDialog() {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(text = "Update Available")
            },
            text = {
                Column {
                    Text("There is a newer version of Cavern App")
                    Text("This update is required. The app will close if you choose not to update.")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUpdateDialog = false
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://github.com/secminhr/TheNewCavern/releases/latest")
                        startActivity(intent)
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        finish()
                    }
                ) {
                    Text("No")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    @Composable
    fun SkippableUpdateDialog() {
        AlertDialog(
            onDismissRequest = {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            },
            title = {
                Text(text = "Update Available")
            },
            text = {
                Text("There is a newer version of Cavern App")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUpdateDialog = false
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://github.com/secminhr/TheNewCavern/releases/latest")
                        startActivity(intent)
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showUpdateDialog = false
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                        finish()
                    }
                ) {
                    Text("Later")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    private suspend fun checkUpdate(context: Context, ) = withContext(Dispatchers.IO) {
        val url = URL("https://cavern-8e04d.firebaseio.com/appMeta.json")
        val appMeta: AppMeta = Json.decodeFromString(url.openStream().reader().readText())
        val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val currentVersion: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
        if (appMeta.newestVersion > currentVersion) {
            updateIsRequired = appMeta.required
            showUpdateDialog = true
        } else {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()
        }
    }

    @Serializable
    private data class AppMeta(val newestVersion: Int, val required: Boolean)
}