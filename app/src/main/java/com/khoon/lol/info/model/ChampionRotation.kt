package com.khoon.lol.info.model

import com.google.gson.annotations.SerializedName

/**
 * Matches Riot platform rotation JSON (transparent bridge / live).
 * Fields: [sr] free rotation, [newplayer] low-level free rotation.
 */
data class ChampionRotation(
    @SerializedName("sr")
    val freeChampionIds: List<Int> = emptyList(),
    @SerializedName("newplayer")
    val freeChampionIdsForNewPlayers: List<Int> = emptyList(),
)

/** Resolved rotation rows for UI (display name + local image path). */
data class RotationChampionRows(
    val freeRotation: List<Pair<String, String?>> = emptyList(),
    val newPlayerRotation: List<Pair<String, String?>> = emptyList(),
)
