package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
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

@Composable
fun CreateScreen() {
    var selectedOption by remember { mutableStateOf("Preset") }
    var selectedTemplateId by remember { mutableStateOf(-1) }
    var customName by remember { mutableStateOf("") }
    var customUrl by remember { mutableStateOf("") }
    var customBannerImage by remember { mutableStateOf("") }
    val dbHelper = DBHelper(LocalContext.current)

    Scaffold(
        topBar = {
            GrimoireHeader(
                title = "Wiki Grimoire",
                showProfilePicture = true,
                showBackArrow = false,
                onProfileClick = { /* Handle profile click */ },
                onBackClick = { /* Handle back click */ }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            SegmentedButton(
                options = listOf("Preset", "Custom"),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedOption == "Preset") {
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
                    onBannerImageChange = { customBannerImage = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (selectedOption == "Preset" && selectedTemplateId != -1) {
                        dbHelper.toggleDashboardStatus(selectedTemplateId, true)
                    } else if (selectedOption == "Custom" && customName.isNotEmpty() && customUrl.isNotEmpty() && customBannerImage.isNotEmpty()) {
                        dbHelper.insertWiki(
                            name = customName,
                            url = customUrl,
                            bannerImage = customBannerImage,
                            dashboardStatus = true
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun SegmentedButton(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    Row {
        options.forEach { option ->
            Button(
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(option)
            }
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
fun CustomWikiFields(name: String, onNameChange: (String) -> Unit, url: String, onUrlChange: (String) -> Unit, bannerImage: String, onBannerImageChange: (String) -> Unit) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val file = File(context.filesDir, "assets/images/${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            onBannerImageChange(file.name)
            imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it).asImageBitmap()
        }
    }

    Column {
        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(8.dp)) {
                    if (name.isEmpty()) Text("Name")
                    innerTextField()
                }
            }
        )
        BasicTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(8.dp)) {
                    if (url.isEmpty()) Text("URL")
                    innerTextField()
                }
            }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { launcher.launch("image/*") }) {
            if (imageBitmap != null) {
                Image(bitmap = imageBitmap!!, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
            } else {
                Text("Select Banner Image")
            }
        }
    }
}