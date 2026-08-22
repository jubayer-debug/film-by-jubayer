package com.example.data.models

enum class PhotoCategory(val label: String) {
    ALL("ALL THEMES"),
    LANDSCAPE("LANDSCAPES"),
    RIVER("RIVERS"),
    PORTRAIT("HUMAN STORIES"),
    MONSOON("MONSOON"),
    STREET("STREET & NIGHT")
}

enum class PhotoOrientation {
    LANDSCAPE,
    PORTRAIT,
    SQUARE,
    PANORAMIC
}

enum class VisualMood {
    RIVER_DAWN,
    MONSOON_MIST,
    MEGHNA_DUSK,
    OLD_DHAKA_NIGHT,
    TEA_HIGHLANDS,
    VILLAGE_SHADOW,
    PORTRAIT_LIGHT,
    COASTAL_SILENCE,
    RIVER_STORM,
    JUTE_HARVEST
}

data class CameraExif(
    val camera: String = "Leica M11-P",
    val lens: String = "Summilux-M 35mm f/1.4 ASPH",
    val aperture: String = "f/2.0",
    val shutter: String = "1/500s",
    val iso: String = "ISO 200",
    val focalLength: String = "35mm",
    val format: String = "Raw (DNG) • 3:2"
)

data class Photograph(
    val id: String,
    val title: String,
    val bengaliTitle: String = "",
    val location: String,
    val year: String,
    val category: PhotoCategory,
    val orientation: PhotoOrientation = PhotoOrientation.LANDSCAPE,
    val caption: String,
    val story: String,
    val mood: VisualMood,
    val exif: CameraExif = CameraExif(),
    val isCuratedFeatured: Boolean = false,
    val imageUrl: String = "",
    val thumbUrl: String = ""
)
