package com.ajou.helptmanager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name= "userData")

class UserDataStore() {
    private val context = Application.context()
    private val dataStore : DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val GYM_STATUS = stringPreferencesKey("gym_status")
        val USER_NAME = stringPreferencesKey("user_name")
        val KAKAO_ID = stringPreferencesKey("kakao_id")
        val GYM_ID = intPreferencesKey("gym_id")
    }

    suspend fun saveAccessToken(token : String) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.ACCESS_TOKEN] = token
            }
        }
    }

    suspend fun getAccessToken():String? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN]
        }
    }

    suspend fun saveRefreshToken(token : String) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.REFRESH_TOKEN] = token
            }
        }
    }

    suspend fun getRefreshToken():String? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN]
        }
    }

    suspend fun saveUserName(name : String) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.USER_NAME] = name
            }
        }
    }

    suspend fun saveGymStatus(status : String) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.GYM_STATUS] = status
            }
        }
    }

    suspend fun getGymStatus() : String? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.GYM_STATUS]
        }
    }

    suspend fun getUserName():String? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.USER_NAME]
        }
    }

    suspend fun saveKakaoId(id:String) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.KAKAO_ID] = id
            }
        }
    }

    suspend fun getKakaoId():String? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.KAKAO_ID]
        }
    }

    suspend fun saveGymId(id:Int) {
        withContext(Dispatchers.IO){
            dataStore.edit { pref ->
                pref[PreferencesKeys.GYM_ID] = id
            }
        }
    }

    suspend fun getGymId():Int? {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[PreferencesKeys.GYM_ID]
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dataStore.edit { pref ->
                pref.clear()
            }
        }
    }

    suspend fun printAllValues() {
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        val gymStatus = getGymStatus()
        val userName = getUserName()
        val kakaoId = getKakaoId()
        val gymId = getGymId()

        Log.d("UserDataStore", "Access Token: $accessToken")
        Log.d("UserDataStore", "Refresh Token: $refreshToken")
        Log.d("UserDataStore", "Gym Status: $gymStatus")
        Log.d("UserDataStore", "User Name: $userName")
        Log.d("UserDataStore", "Kakao ID: $kakaoId")
        Log.d("UserDataStore", "Gym ID: $gymId")
    }

}