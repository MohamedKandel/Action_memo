package com.mkandeel.actionmemo.Helper

import android.graphics.Color

object Constants {
    const val TITAN_ONE = "TitanOne-Regular.ttf"
    const val INTER_BOLD = "Inter-Bold.ttf"
    const val INTER_REG = "Inter-Regular.ttf"
    const val MODE = "app mode"
    const val LANG = "app lang"
    const val USERNAME = "username"
    const val PASSWORD = "password"
    const val ID = "id"

    /*
    * 0 -> not urgent or important
    * 1 -> normal
    * 2 -> important, but not urgent
    * 3 -> important and urgent
    * */
    val colors = mapOf(
        0 to Color.rgb(250, 251, 186),
        1 to Color.rgb(195, 251, 186),
        2 to Color.rgb(186, 188, 251),
        3 to Color.rgb(251, 186, 186)
    )

    const val CLICKED = "clicked"
    const val DELETE = "delete"
    const val EDIT = "edit"
    const val NOTE = "note"

    const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="

    const val FCM_URL = "https://fcm.googleapis.com/"
    const val SERVER_KEY = "key=AAAAJbFvnyU:APA91bHyHTh_LRscj40q3NY44NvarzB3W3jF2QNxrWWkubsYCAhp32r1EGakmHa6ViEngRspNyxZZ94fNkrzHq75sWuWafFWtYmZWplPNtlVZI4QGEVXtstSEqEP73d-xVQePx4wiBhL"
    const val CONTENT_TYPE = "application/json"

}