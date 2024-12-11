package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.aileenyx.wikigrimoire.util.wikis
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import com.aileenyx.wikigrimoire.util.updateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.aileenyx.wikigrimoire.beans.Wiki
import com.aileenyx.wikigrimoire.components.WikiCard
import com.aileenyx.wikigrimoire.util.DBHelper

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val wikisState = remember { mutableStateListOf<Wiki>() }
    val navBackStackEntry = LocalContext.current as? androidx.navigation.NavBackStackEntry
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(navBackStackEntry) {
        withContext(Dispatchers.IO) {
            updateList(context)
        }
        wikisState.clear()
        wikisState.addAll(wikis)
    }

    Scaffold(
        topBar = {
            GrimoireHeader(
                showProfilePicture = true,
                showBackArrow = false
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val dbHelper = DBHelper

            var list = dbHelper.fetchData(context, "SELECT * FROM wiki")

            for (wiki in wikisState) {
                Log.d("HomeScreen", "Printing wiki: ${wiki.name}")
                WikiCard(
                    name = wiki.name,
                    url = wiki.url,
                    image = wiki.bannerImage,
                    isTemplate = wiki.isTemplate,
                    isLarge = wikisState.indexOf(wiki) == 0
                )
            }
        }
    }
}