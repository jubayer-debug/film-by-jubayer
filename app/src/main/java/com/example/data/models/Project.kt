package com.example.data.models

data class AlbumFrame(
    val id: String,
    val number: String,
    val title: String,
    val bengaliTitle: String = "",
    val location: String = "",
    val year: String = "2026",
    val exif: String = "Leica M11-P • 35mm f/1.4 • 1/500s • ISO 100",
    val imageUrl: String,
    val caption: String = ""
)

data class Project(
    val id: String,
    val number: String,
    val title: String,
    val bengaliTitle: String = "",
    val subtitle: String,
    val location: String,
    val year: String,
    val photoCount: Int,
    val coverPhotoId: String,
    val description: String,
    val essayText: String,
    val photoIds: List<String>,
    val quote: String = "",
    val viewCount: Int = 185,
    val dateFormatted: String = "APRIL 2026",
    val frameList: List<AlbumFrame> = emptyList()
)

data class JournalEntry(
    val id: String,
    val title: String,
    val bengaliTitle: String = "",
    val date: String,
    val location: String,
    val readTime: String,
    val excerpt: String,
    val content: String,
    val coverPhotoId: String,
    val quote: String = ""
)

data class Exhibition(
    val year: String,
    val title: String,
    val venue: String,
    val location: String,
    val type: String
)
