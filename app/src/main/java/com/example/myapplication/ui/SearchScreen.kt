package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isDarkMode by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    MyApplicationTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Dark mode")
                                        Switch(
                                            checked = isDarkMode,
                                            onCheckedChange = { checked ->
                                                isDarkMode = checked
                                            }
                                        )
                                    }
                                },
                                onClick = {
                                    isDarkMode = !isDarkMode
                                }
                            )
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ScreenHeader()
                    }

                    item {
                        SearchInputSection(
                            phoneNumber = uiState.phoneNumber,
                            isLoading = uiState.isLoading,
                            onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
                            onSearchClicked = viewModel::searchNumber
                        )
                    }

                    uiState.errorMessage?.let { message ->
                        item {
                            ErrorMessage(message = message)
                        }
                    }

                    if (uiState.isLoading) {
                        item {
                            LoadingSection()
                        }
                    }

                    if (uiState.isSpam != null) {
                        item {
                            SearchResultSection(
                                isSpam = uiState.isSpam,
                                reportCount = uiState.searchResult?.reportCount,
                                category = uiState.searchResult?.category
                            )
                        }
                    }

                    item {
                        SectionTitle(title = "Histórico")
                    }

                    item {
                        HistoryTabs(
                            selectedTab = uiState.selectedHistoryTab,
                            onTabSelected = viewModel::onHistoryTabSelected
                        )
                    }

                    when (uiState.selectedHistoryTab) {
                        HistoryTab.SEARCHES -> {
                            if (uiState.searchHistory.isEmpty()) {
                                item {
                                    EmptyStateCard(
                                        message = "Ainda não existem pesquisas no histórico."
                                    )
                                }
                            } else {
                                items(uiState.searchHistory) { item ->
                                    SearchHistoryCard(
                                        phoneNumber = item.phoneNumber,
                                        isSpam = item.isSpam
                                    )
                                }
                            }
                        }

                        HistoryTab.BLOCKED_CALLS -> {
                            if (uiState.blockedCalls.isEmpty()) {
                                item {
                                    EmptyStateCard(
                                        message = "Ainda não existem chamadas bloqueadas."
                                    )
                                }
                            } else {
                                items(uiState.blockedCalls) { item ->
                                    BlockedCallCard(
                                        phoneNumber = item.phoneNumber,
                                        category = item.category
                                    )
                                }
                            }
                        }

                        HistoryTab.SPAM_LIST -> {
                            if (uiState.spamNumbers.isEmpty()) {
                                item {
                                    EmptyStateCard(
                                        message = "Não existem números de spam na base de dados."
                                    )
                                }
                            } else {
                                items(uiState.spamNumbers) { item ->
                                    SpamNumberCard(
                                        phoneNumber = item.phoneNumber,
                                        category = item.category,
                                        reportCount = item.reportCount
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTabs(
    selectedTab: HistoryTab,
    onTabSelected: (HistoryTab) -> Unit
) {
    val selectedIndex = when (selectedTab) {
        HistoryTab.SEARCHES -> 0
        HistoryTab.BLOCKED_CALLS -> 1
        HistoryTab.SPAM_LIST -> 2
    }

    PrimaryTabRow(
        selectedTabIndex = selectedIndex
    ) {
        Tab(
            selected = selectedTab == HistoryTab.SEARCHES,
            onClick = { onTabSelected(HistoryTab.SEARCHES) },
            text = { Text("Pesquisas") }
        )
        Tab(
            selected = selectedTab == HistoryTab.BLOCKED_CALLS,
            onClick = { onTabSelected(HistoryTab.BLOCKED_CALLS) },
            text = { Text("Bloqueadas") }
        )
        Tab(
            selected = selectedTab == HistoryTab.SPAM_LIST,
            onClick = { onTabSelected(HistoryTab.SPAM_LIST) },
            text = { Text("Spam") }
        )
    }
}

@Composable
private fun ScreenHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Pesquisa de números",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Valida números e consulta o histórico local.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchInputSection(
    phoneNumber: String,
    isLoading: Boolean,
    onPhoneNumberChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChanged,
            label = { Text("Número") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = onSearchClicked,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pesquisar")
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun LoadingSection() {
    CircularProgressIndicator()
}

@Composable
private fun SearchResultSection(
    isSpam: Boolean?,
    reportCount: Int?,
    category: String?
) {
    val isSpamResult = isSpam == true
    val backgroundColor = if (isSpamResult) {
        Color(0xFFFFF1F0)
    } else {
        Color(0xFFF1F8F4)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isSpamResult) {
                    "Número identificado como spam"
                } else {
                    "Número não encontrado na blacklist"
                },
                style = MaterialTheme.typography.titleMedium
            )

            if (isSpamResult) {
                Text("Categoria: ${category ?: "Sem categoria"}")
                Text("Nº de denúncias: ${reportCount ?: 0}")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun SearchHistoryCard(
    phoneNumber: String,
    isSpam: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (isSpam) "Spam" else "Não spam",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSpam) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}

@Composable
private fun BlockedCallCard(
    phoneNumber: String,
    category: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Categoria: ${category ?: "Sem categoria"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SpamNumberCard(
    phoneNumber: String,
    category: String?,
    reportCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Categoria: ${category ?: "Sem categoria"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nº de denúncias: $reportCount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}