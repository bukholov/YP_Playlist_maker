package com.example.yp_playlist_maker.settings.view_model

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.settings.domain.SettingsInteractor
import com.example.yp_playlist_maker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
): ViewModel() {

    companion object{
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(Creator.provideSharingInteractor(context), Creator.provideSettingsInteractor(context))
            }
        }
    }
    fun setTheme(isChecked: Boolean){
        settingsInteractor.storeDarkTheme(isChecked)
        if (isChecked) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
        else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    }

    fun getSavedTheme():Boolean{
        return settingsInteractor.retrieveIsDarkTheme()
    }

    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun openSupport(){
        sharingInteractor.openSupport()
    }

    fun openTerms(){
        sharingInteractor.openTerms()
    }
}