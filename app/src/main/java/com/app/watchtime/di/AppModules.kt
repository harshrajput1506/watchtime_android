package com.app.watchtime.di

import com.app.auth.data.di.authDataModule
import com.app.auth.ui.di.authUiModule
import com.app.core.network.di.networkModule
import com.app.popular.data.di.popularDataModule
import com.app.popular.ui.di.popularUiModule

val appModules = listOf(
    authDataModule,
    authUiModule,
    networkModule,
    popularDataModule,
    popularUiModule
)