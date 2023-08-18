package com.hi.dhl.pokemon.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hi.dhl.pokemon.ext.getEmptyOrDefault

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/7/11
 *     desc  :
 * </pre>
 */

@Entity
data class PokemonInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var height: Int,
    var weight: Int,
    var experience: Int,
    var types: List<Type>,
    var stats: List<Stats>,
    @Embedded val sprites: Sprites
) {

    data class Sprites(
        var backDefault: String,
        var backFemale: String,
        var backShiny: String,
        var backShinyFemale: String,
        var frontDefault: String,
        var frontfemale: String,
        var frontShiny: String,
        var frontShinyFemale: String
    )

    data class Type(var name: String, var url: String)

    data class Stats(var baseStat: Int, var name: String, var url: String)

    companion object {
        fun convert2PokemonInfoEntity(netWorkPokemonInfo: NetWorkPokemonInfo): PokemonInfoEntity {
            return netWorkPokemonInfo.run {

                val dbTypes = mutableListOf<Type>()
                val dbStats = mutableListOf<Stats>()

                types.forEach {
                    dbTypes.add(
                        Type(
                            name = it.type.name,
                            url = it.type.url
                        )
                    )
                }

                stats.forEach {
                    dbStats.add(
                        Stats(
                            baseStat = it.baseStat,
                            name = it.stat.name,
                            url = it.stat.url
                        )
                    )
                }

                val dbSprites = Sprites(
                    backDefault = sprites.backDefault.getEmptyOrDefault { "" },
                    backFemale = sprites.backFemale.getEmptyOrDefault { "" },
                    backShiny = sprites.backShiny.getEmptyOrDefault { "" },
                    backShinyFemale = sprites.backShinyFemale.getEmptyOrDefault { "" },
                    frontDefault = sprites.frontDefault.getEmptyOrDefault { "" },
                    frontfemale = sprites.frontfemale.getEmptyOrDefault { "" },
                    frontShiny = sprites.frontShiny.getEmptyOrDefault { "" },
                    frontShinyFemale = sprites.frontShinyFemale.getEmptyOrDefault { "" }
                )

                PokemonInfoEntity(
                    name = name,
                    height = height,
                    weight = weight,
                    experience = experience,
                    types = dbTypes,
                    stats = dbStats,
                    sprites = dbSprites
                )
            }
        }
    }
}

