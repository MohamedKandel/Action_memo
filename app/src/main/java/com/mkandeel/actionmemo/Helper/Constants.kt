package com.mkandeel.actionmemo.Helper

import android.graphics.Color

object Constants {
    val TITAN_ONE = "TitanOne-Regular.ttf"
    val INTER_BOLD = "Inter-Bold.ttf"
    val INTER_REG = "Inter-Regular.ttf"
    val MODE = "app mode"
    val LANG = "app lang"
    val USERNAME = "username"
    val PASSWORD = "password"
    val ID = "id"

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

    val CLICKED = "clicked"
    val DELETE = "delete"
    val EDIT = "edit"
}