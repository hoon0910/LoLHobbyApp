package com.khoon.lol.info.ui.components

import android.util.Log // For logging errors
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator // For loading UI
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // For ImageRequest
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage // Coil import
import coil.request.ImageRequest // Coil import
import com.khoon.lol.info.R
import com.khoon.lol.info.model.ChampionDetail
// import com.khoon.lol.info.R // If using a local drawable for error

@Composable
fun ChampionSkillsSection(
    detail: ChampionDetail?,
    modifier: Modifier = Modifier
) {
    val ddragonVersion = "14.10.1" // Placeholder, manage dynamically
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (detail == null) {
            Text(stringResource(R.string.skill_info_unavailable))
            return
        }

        Text(
            stringResource(R.string.tab_skills),
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
        )

        // Passive Skill
        detail.passive?.let { passive ->
            val passiveImageUrl = passive.image?.full?.let {
                "https://ddragon.leagueoflegends.com/cdn/$ddragonVersion/img/passive/$it" // Changed to HTTPS
            }

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(passiveImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = passive.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(56.dp),
                        loading = {
                            CircularProgressIndicator(modifier = Modifier.size(36.dp))
                        },
                        error = {
                            Text(stringResource(R.string.skill_key_p), color = Color.Red, fontSize = 24.sp)
                            Log.e("SkillIconError", "Passive: Failed to load ${passive.image?.full}, URL: $passiveImageUrl, Error: ${it.result.throwable}")
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(passive.name, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                        Text(stringResource(R.string.skill_passive), fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Active Spells (Q, W, E, R)
        val spellKeys = listOf(
            stringResource(R.string.skill_key_q),
            stringResource(R.string.skill_key_w),
            stringResource(R.string.skill_key_e),
            stringResource(R.string.skill_key_r)
        )
        detail.spells?.forEachIndexed { index, spell ->
            val spellImageUrl = spell.image?.full?.let {
                "https://ddragon.leagueoflegends.com/cdn/$ddragonVersion/img/spell/$it" // Changed to HTTPS
            }

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                if (index > 0 || detail.passive != null) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 0.dp, bottom = 8.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(spellImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = spell.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(56.dp),
                        loading = {
                            CircularProgressIndicator(modifier = Modifier.size(36.dp))
                        },
                        error = {
                            Text(
                                spellKeys.getOrElse(index) { stringResource(R.string.skill_key_unknown) },
                                color = Color.Red,
                                fontSize = 24.sp
                            )
                            Log.e("SkillIconError", "Spell '${spell.name}': Failed to load ${spell.image?.full}, URL: $spellImageUrl, Error: ${it.result.throwable}")
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(
                                R.string.spell_name_format,
                                spellKeys.getOrElse(index) { stringResource(R.string.skill_generic) },
                                spell.name
                            ),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}
