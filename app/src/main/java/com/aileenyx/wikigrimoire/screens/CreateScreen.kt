package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aileenyx.wikigrimoire.util.DBHelper
import com.aileenyx.wikigrimoire.util.templates
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.imageResource
import com.aileenyx.wikigrimoire.R
import com.aileenyx.wikigrimoire.util.generateImageFileName
import com.aileenyx.wikigrimoire.util.getImageFromName
import com.aileenyx.wikigrimoire.util.saveBitmapAsWebp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen
import kotlinx.coroutines.withContext

@Composable
fun CreateScreen() {
    val navController = LocalNavController.current
    var selectedTemplateId by remember { mutableStateOf(-1) }
    var customName by remember { mutableStateOf("") }
    var customUrl by remember { mutableStateOf("") }
    var customBannerImage by remember { mutableStateOf<Bitmap?>(null) }
    var selectedIndex by remember { mutableStateOf(0) }
    var useCustomImage by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    val dbHelper = DBHelper(LocalContext.current)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            GrimoireHeader(
                showProfilePicture = true,
                showBackArrow = false
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            SingleChoiceSegmentedButton(selectedIndex = selectedIndex, onSelectedIndexChange = { selectedIndex = it })

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedIndex == 0) {
                TemplateDropdown(
                    templates = templates,
                    selectedTemplateId = selectedTemplateId,
                    onTemplateSelected = { id -> selectedTemplateId = id }
                )
            } else {
                CustomWikiFields(
                    name = customName,
                    onNameChange = { customName = it },
                    url = customUrl,
                    onUrlChange = { customUrl = it },
                    bannerImage = customBannerImage,
                    onBannerImageChange = { customBannerImage = it },
                    useCustomImage = useCustomImage,
                    onUseCustomImageChange = { useCustomImage = it },
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        if (selectedIndex == 0 && selectedTemplateId != -1) {
                            loading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                dbHelper.toggleDashboardStatus(selectedTemplateId, true)
                                withContext(Dispatchers.Main) {
                                    loading = false
                                    navController.previousBackStackEntry?.savedStateHandle?.set("snackbarMessage", "Wiki added successfully!")
                                    navController.navigate(Screen.HomeScreen)
                                }
                            }
                        } else if (selectedIndex == 1 && customName.isNotEmpty() && customUrl.isNotEmpty() && (customBannerImage != null || !useCustomImage)) {
                            loading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                var fileName = "template-default"
                                if (customBannerImage != null) {
                                    fileName = generateImageFileName(customName)
                                    saveBitmapAsWebp(context, customBannerImage!!, fileName)
                                }
                                dbHelper.insertWiki(
                                    name = customName,
                                    url = customUrl,
                                    bannerImage = fileName,
                                    dashboardStatus = true
                                )
                                withContext(Dispatchers.Main) {
                                    loading = false
                                    navController.previousBackStackEntry?.savedStateHandle?.set("snackbarMessage", "Wiki added successfully!")
                                    navController.navigate(Screen.HomeScreen)
                                }
                            }
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                snackbarHostState.showSnackbar("Please fill in all fields.")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(selectedIndex: Int, onSelectedIndexChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    val options = listOf("Preset", "Custom")

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onSelectedIndexChange(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateDropdown(templates: HashMap<Int, String>, selectedTemplateId: Int, onTemplateSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTemplate by remember { mutableStateOf(templates[selectedTemplateId] ?: "Select a template") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedTemplate ?: "Select a template",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            templates.forEach { (id, template) ->
                DropdownMenuItem(
                    text = { Text(template) },
                    onClick = {
                        selectedTemplate = template
                        onTemplateSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CustomWikiFields(
    name: String,
    onNameChange: (String) -> Unit,
    url: String,
    onUrlChange: (String) -> Unit,
    bannerImage: Bitmap?,
    onBannerImageChange: (Bitmap?) -> Unit,
    useCustomImage: Boolean,
    onUseCustomImageChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                imageBitmap = bitmap
                onBannerImageChange(imageBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column {
        TextField(
            value = name,
            onValueChange = { if (it.length <= 36) onNameChange(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = url,
            onValueChange = { if (it.length <= 512) onUrlChange(it) },
            label = { Text("URL") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Use Custom Image")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = useCustomImage,
                onCheckedChange = onUseCustomImageChange
            )
        }
        if (useCustomImage) {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text(if (imageBitmap != null) "Change Banner Image" else "Select Banner Image")
            }
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }
        }
    }
}