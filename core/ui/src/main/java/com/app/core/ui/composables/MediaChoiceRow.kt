package com.app.core.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun MediaChoiceRow(
    options: List<String> = listOf("Movies", "TV"),
    selectedIndex: Int = 0,
    onOptionSelected: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    onOptionSelected(index)
                },
                selected = index == selectedIndex,
                label = { Text(label, style = MaterialTheme.typography.labelMedium) }
            )
        }
    }
}
