package com.app.watchtime.di

import com.app.auth.data.di.authDataModule
import com.app.auth.ui.di.authUiModule

val appModules = listOf(
    authDataModule,
    authUiModule
)