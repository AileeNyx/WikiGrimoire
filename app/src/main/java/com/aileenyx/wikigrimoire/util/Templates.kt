package com.aileenyx.wikigrimoire.util

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.util.Dictionary

val templates = HashMap<Int, String>()

fun migrateTemplates(context: Context) {
    val dbHelper = DBHelper(context)
    dbHelper.insertWiki("Minecraft", "https://minecraft.wiki/", "template-minecraft", false, true)
    dbHelper.insertWiki("Terraria", "https://terraria.fandom.com/wiki/Terraria_Wiki", "template-terraria", false, true)
}

fun populateTemplates(context: Context) {
    val query = "SELECT ID, ${Contract.Wiki.COLUMN_NAME_NAME} FROM ${Contract.Wiki.TABLE_NAME} WHERE ${Contract.Wiki.COLUMN_NAME_DEFAULT} = TRUE"
    val result = DBHelper.fetchData(context, query)
    templates.clear()
    result.forEach { row ->
        templates.put(row["ID"] as Int, row[Contract.Wiki.COLUMN_NAME_NAME] as String)
    }
}