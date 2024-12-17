package com.aileenyx.wikigrimoire.util

import android.content.Context
import android.util.Log
import com.aileenyx.wikigrimoire.beans.Wiki
import com.aileenyx.wikigrimoire.util.DBHelper.Companion.fetchData
import io.github.jan.supabase.postgrest.from

val wikis = mutableListOf<Wiki>()

suspend fun updateList(context: Context) {
    try {
        val query = "SELECT name, url, bannerImage, dashboardStatus, isDefault FROM wiki WHERE dashboardStatus = true"
        val result = fetchData(context, query)
        Log.d("updateList", "Query executed successfully with result: $result")

        wikis.clear()
        for (row in result) {
            val wiki = Wiki().apply {
                name = row["name"] as String
                url = row["url"] as String
                bannerImage = row["bannerImage"] as String
                isTemplate = row["isDefault"] == 1
            }
            wikis.add(wiki)
        }
        Log.d("updateList", "List updated successfully")
    } catch (e: Exception) {
        Log.e("updateList", "Error updating list", e)
    }

    val city = supabase.from("cities").select().decodeSingle<Wiki>()

}