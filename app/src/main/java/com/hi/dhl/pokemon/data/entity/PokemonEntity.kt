package com.hi.dhl.pokemon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/7/11
 *     desc  :
 * </pre>
 */

@Entity
data class PokemonEntity(
    @PrimaryKey
    var name: String,
    var pokemonId: Int = 0,
    var page: Int = 0,
    var url: String,
    var remoteName: String
)