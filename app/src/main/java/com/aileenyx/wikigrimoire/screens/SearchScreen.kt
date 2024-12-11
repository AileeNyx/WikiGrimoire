package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import com.aileenyx.wikigrimoire.util.wikis
import androidx.compose.material3.Scaffold
import com.aileenyx.wikigrimoire.components.WikiCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val searchQuery = remember { mutableStateOf("") }
    val filteredWikis = remember(searchQuery.value) {
        wikis.filter { it.name.contains(searchQuery.value, ignoreCase = true) }
            .sortedBy { it.name }
    }

    Scaffold(
        topBar = {
            GrimoireHeader(
                showProfilePicture = true,
                showBackArrow = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            SearchBar(
                query = searchQuery.value,
                onQueryChange = { searchQuery.value = it },
                onSearch = {},
                active = false,
                onActiveChange = {},
                placeholder = { Text("Search") },
                content = {}
            )
            for (wiki in filteredWikis) {
                WikiCard(
                    name = wiki.name,
                    url = wiki.url,
                    image = wiki.bannerImage,
                    isTemplate = wiki.isTemplate,
                    isLarge = false
                )
            }
        }
    }
}