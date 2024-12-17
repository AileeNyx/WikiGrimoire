package com.aileenyx.wikigrimoire.util

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.UUID

data class Wiki(
    val id: UUID,
    val name: String,
    val url: String,
    val bannerImage: String,
    val default: Boolean
)

data class WikiUser(
    val wikiId: UUID,
    val userId: UUID,
    val lastOpened: Instant
)

