package com.yjotdev.clasificarpeces.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SpeciesDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("tipo") val type: String = "",
    @SerializedName("nombre_comun") val commonName: String = "",
    @SerializedName("nombre_cientifico") val scientificName: String = "",
    @SerializedName("habitat_natural") val naturalHabitat: String = "",
    @SerializedName("dieta") val diet: String = "",
    @SerializedName("ph") val ph: String = "",
    @SerializedName("temperatura") val temperature: String = "",
    @SerializedName("espacio") val space: String = "",
    @SerializedName("imagen_uri") val imageUri: String = ""
)