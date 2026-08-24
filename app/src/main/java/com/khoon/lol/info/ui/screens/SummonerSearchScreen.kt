package com.khoon.lol.info.ui.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.khoon.lol.info.R
import com.khoon.lol.info.data.api.AccountDto
import com.khoon.lol.info.model.SummonerViewModel
import com.khoon.lol.info.ui.components.ChampionImageLoader
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SummonerSearchScreen(viewModel: SummonerViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val servers = listOf(
        "NA", "EUW", "EUNE", "KR", "JP", "OCE", "LAN", "LAS", "TR", "BR", "RU",
        "SEA", "ME", "VN", "TW", "SG", "CN", "PH", "TH", "PBE",
    )
    var selectedServer by remember { mutableStateOf(servers[3]) }

    val rotationChampions by viewModel.rotationChampions.collectAsState()
    val newPlayerRotationChampions by viewModel.newPlayerRotationChampions.collectAsState()
    val rotationError by viewModel.rotationError.collectAsState()
    val searchCandidates by viewModel.searchCandidates.collectAsState()
    val accountResult by viewModel.accountResult.collectAsState()
    val summonerByPuuidResult by viewModel.summonerByPuuidResult.collectAsState()
    val leagueEntriesResult by viewModel.leagueEntriesResult.collectAsState()

    val onSearch: () -> Unit = {
        if (searchQuery.isNotBlank()) {
            viewModel.searchRiotId(searchQuery, selectedServer)
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchRotationChampions()
        delay(100.milliseconds)
        searchFocusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RotationChampionsSection(
                title = stringResource(R.string.free_rotation),
                rotationChampions = rotationChampions,
                emptyMessage = rotationError ?: "—",
            )

            Spacer(modifier = Modifier.height(12.dp))

            RotationChampionsSection(
                title = stringResource(R.string.new_player_rotation),
                rotationChampions = newPlayerRotationChampions,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.search_a_summoner),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        OutlinedTextField(
                            value = selectedServer,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.label_region)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                                .width(100.dp)
                                .padding(end = 8.dp),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            servers.forEach { server ->
                                DropdownMenuItem(
                                    text = { Text(server) },
                                    onClick = {
                                        selectedServer = server
                                        expanded = false
                                    },
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(stringResource(R.string.label_riot_id)) },
                        placeholder = { Text(stringResource(R.string.placeholder_riot_id_search)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .focusRequester(searchFocusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    )
                    Button(
                        onClick = onSearch,
                        enabled = searchQuery.isNotBlank(),
                    ) {
                        Text(stringResource(R.string.search))
                    }
                }
            }

            if (searchCandidates.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SearchCandidatesSection(
                    candidates = searchCandidates,
                    onCandidateClick = { candidate ->
                        searchQuery = "${candidate.gameName}#${candidate.tagLine}"
                        viewModel.selectSearchCandidate(candidate)
                        keyboardController?.hide()
                    },
                )
            }

            if (accountResult.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoCardSection(
                    title = stringResource(R.string.account_info),
                    body = accountResult,
                )
            }

            if (summonerByPuuidResult.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoCardSection(
                    title = stringResource(R.string.summoner_info),
                    body = summonerByPuuidResult,
                )
            }

            if (leagueEntriesResult.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoCardSection(
                    title = stringResource(R.string.ranked),
                    body = leagueEntriesResult,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun RotationChampionsSection(
    title: String,
    rotationChampions: List<Pair<String, String?>>,
    emptyMessage: String = "—",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            if (rotationChampions.isEmpty()) {
                Text(
                    text = emptyMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 24.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                )
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(rotationChampions.take(20)) { (name, imagePath) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(80.dp),
                        ) {
                            ChampionImageLoader(
                                imagePath = imagePath,
                                name = name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .aspectRatio(1f),
                            )
                            Text(
                                text = name,
                                modifier = Modifier.padding(top = 4.dp),
                                fontSize = 10.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchCandidatesSection(
    candidates: List<AccountDto>,
    onCandidateClick: (AccountDto) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.account_candidates),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(vertical = 4.dp),
        ) {
            Column {
                candidates.forEachIndexed { index, candidate ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        )
                    }
                    Text(
                        text = "${candidate.gameName}#${candidate.tagLine}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCandidateClick(candidate) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCardSection(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(16.dp),
        ) {
            Text(text = body, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummonerSearchScreenPreview() {
    SummonerSearchScreen()
}
