package com.app.discover.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.app.discover.ui.R

@Composable
fun DiscoverSearchBar(
    modifier: Modifier = Modifier,
    label: String = "Search",
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(label)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                    keyboardController?.hide()
                }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        )
    }
}
