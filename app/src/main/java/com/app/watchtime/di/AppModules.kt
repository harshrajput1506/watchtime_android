package com.app.watchtime.di

import com.app.auth.data.di.authDataModule
import com.app.auth.ui.di.authUiModule
import com.app.collections.data.module.collectionDataModule
import com.app.core.network.di.networkModule
import com.app.core.room.di.roomModule
import com.app.core.ui.di.coreUiModule
import com.app.discover.data.di.discoverDataModule
import com.app.discover.ui.di.discoverUiModule
import com.app.media.data.di.mediaDataModule
import com.app.media.ui.di.mediaUiModule
import com.app.popular.data.di.popularDataModule
import com.app.popular.ui.di.popularUiModule
import com.app.profile.data.di.profileDataModule
import com.app.profile.ui.di.profileUiModule
import com.collections.ui.di.collectionsUiModule

val appModules = listOf(
    authDataModule,
    authUiModule,
    networkModule,
    popularDataModule,
    popularUiModule,
    discoverDataModule,
    discoverUiModule,
    roomModule,
    collectionDataModule,
    mediaDataModule,
    mediaUiModule,
    collectionsUiModule,
    profileDataModule,
    profileUiModule,
    coreUiModule
)