package miu.sts.app.njangui.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import miu.sts.app.njangui.models.UserSettings

object UserSettingsHelper {

    private const val PREFERENCES_NAME = "user_settings_prefs"
    private const val SETTINGS_KEY = "user_settings"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun loadSettings(context: Context): UserSettings {
        val sharedPreferences = getSharedPreferences(context)
        val settingsJson = sharedPreferences.getString(SETTINGS_KEY, null)
        return if (settingsJson != null) {
            try {
                Json.decodeFromString<UserSettings>(settingsJson)
            } catch (e: Exception) {
                e.printStackTrace()
                UserSettings()
            }
        } else {
            UserSettings()
        }
    }

    fun saveSettings(context: Context, settings: UserSettings) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val settingsJson = Json.encodeToString(settings)
        editor.putString(SETTINGS_KEY, settingsJson)
        editor.apply()
    }

    fun setLanguage(context: Context, languageCode: String) {
        val settings = loadSettings(context).copy(language = languageCode)
        saveSettings(context, settings)
    }

    fun getLanguage(context: Context): String {
        return loadSettings(context).language
    }
}