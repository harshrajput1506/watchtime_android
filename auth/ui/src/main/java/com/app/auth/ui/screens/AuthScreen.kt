package com.app.auth.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.auth.ui.R
import com.app.auth.ui.composables.AnimatedHeroSection
import com.app.auth.ui.states.AuthState
import com.app.auth.ui.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigateToHome: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier
            .fillMaxSize(),

        content = { innerPadding ->
            val authState = viewModel.authState.value

            if (authState is AuthState.Error) {
                LaunchedEffect(Unit) {
                    snackBarHostState.showSnackbar(
                        message = authState.error,
                        actionLabel = "Dismiss",
                        withDismissAction = true
                    )
                }
            } else if (authState is AuthState.Authenticated) {
                navigateToHome()
            }


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedHeroSection()

                Spacer(modifier = Modifier.weight(1f))

                // Google Sign-In Button
                GoogleSignInButton(
                    isLoading = viewModel.authState.value is AuthState.Loading,
                    onSignIn = {
                        viewModel.login()
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}

@Composable
fun GoogleSignInButton(
    isLoading: Boolean = false,
    onSignIn: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSignIn() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {


        if (!isLoading) Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(
                    R.drawable.ic_google
                ),
                contentDescription = "Google Sign-In",
                tint = MaterialTheme.colorScheme.onSurface

            )

            Text(
                text = "Continue with Google",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        } else
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(20.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface,
                strokeWidth = 2.dp
            )


    }
}