package com.example.data.models

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
    val quote: String = ""
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
