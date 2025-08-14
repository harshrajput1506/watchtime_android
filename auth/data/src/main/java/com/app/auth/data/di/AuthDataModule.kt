package com.app.auth.data.di

import android.content.Context
import com.app.auth.data.R
import com.app.auth.data.repository.AuthRepositoryImpl
import com.app.auth.data.services.FirebaseAuthService
import com.app.auth.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import org.koin.dsl.module

val authDataModule = module {
    single<GetGoogleIdOption> {
        GetGoogleIdOption.Builder()
            .setServerClientId(get<Context>().getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()
    }

    single<FirebaseAuthService> {
        FirebaseAuthService(
            get<Context>(),
            get<GetGoogleIdOption>()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get<FirebaseAuthService>())
    }

}

