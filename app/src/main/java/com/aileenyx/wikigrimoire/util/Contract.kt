package com.aileenyx.wikigrimoire.util

import android.provider.BaseColumns

object Contract {
    object Wiki : BaseColumns {
        const val TABLE_NAME            = "wiki"
        const val COLUMN_NAME_NAME      = "name"
        const val COLUMN_NAME_URL       = "url"
        const val COLUMN_NAME_BANNER    = "bannerImage"
        const val COLUMN_NAME_DASHBOARD = "dashboardStatus"
        const val COLUMN_NAME_DEFAULT   = "isDefault"
    }
}

