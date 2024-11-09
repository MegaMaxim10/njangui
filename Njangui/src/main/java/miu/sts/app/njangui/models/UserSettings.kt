package miu.sts.app.njangui.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val language: String = "en"
)