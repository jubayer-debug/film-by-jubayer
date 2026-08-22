package com.example.data.models

data class AboutData(
    val name: String = "Jubayer",
    val brandTitle: String = "KHONCHITRO (ক্ষণচিত্র)",
    val subtitle: String = "Photographer / Visual Storyteller",
    val location: String = "Bangladesh",
    val heroHeading: String = "ABOUT",
    val shortIntro: String = "I photograph landscapes, people and quiet moments—following light through places that are easy to overlook.",
    val bioParagraph1: String = "My work moves between documentary observation and personal exploration. I am drawn to rivers, roads, changing weather, ordinary lives and the small details that give a place its character.",
    val bioParagraph2: String = "Over the past eight years, my practice has centered on the human geography of the Bengal delta. Using rangefinder cameras and natural light, I document how seasonal weather cycles, river erosion, and rural migration reshape memory and domestic environments.",
    val primaryStatement: String = "Photography, for me,\nis a way of staying\nwith a moment.",
    val personalPhilosophy: String = "I am less interested in extraordinary places than in the extraordinary details hidden inside ordinary ones.",
    val practices: List<PracticeItem> = listOf(
        PracticeItem(index = "01", title = "LANDSCAPE", description = "Deltaic topography, river horizons, and tidal marshlands"),
        PracticeItem(index = "02", title = "DOCUMENTARY", description = "Long-form observation of community memory and climate shifts"),
        PracticeItem(index = "03", title = "TRAVEL", description = "Remote transit routes and visual notes across South Asia"),
        PracticeItem(index = "04", title = "STREET & OLD QUARTERS", description = "Incandescent tungsten light and historical night alleyways"),
        PracticeItem(index = "05", title = "CINEMATIC SERIES", description = "Staged environmental narratives and monochromatic portraits"),
        PracticeItem(index = "06", title = "PERSONAL ARCHIVES", description = "Experimental 35mm film studies and handmade artist folios")
    ),
    val metadataList: List<Pair<String, String>> = listOf(
        "BASED IN" to "Bangladesh",
        "WORKING BETWEEN" to "Landscape / Documentary / Travel",
        "CURRENTLY EXPLORING" to "Light, memory, place and movement",
        "AVAILABLE FOR" to "Selected editorial and creative projects"
    ),
    val journeyItems: List<JourneyItem> = listOf(
        JourneyItem(year = "2026", title = "Independent photographic projects & delta archival research"),
        JourneyItem(year = "2025", title = "Landscape and documentary work featured in regional forums"),
        JourneyItem(year = "2024", title = "Travel photography and 35mm monochrome visual experiments"),
        JourneyItem(year = "2023", title = "Beginning of personal photographic archive & river documentation")
    ),
    val selectedProjects: List<AboutProjectLink> = listOf(
        AboutProjectLink(id = "proj_01", title = "RIVER COUNTRY", subtitle = "Lower Meghna Basin", year = "2025"),
        AboutProjectLink(id = "proj_02", title = "AFTER RAIN", subtitle = "Monsoon Atmospheric Series", year = "2025"),
        AboutProjectLink(id = "proj_03", title = "QUIET ROADS", subtitle = "Highway Transit at Blue Hour", year = "2024"),
        AboutProjectLink(id = "proj_04", title = "BETWEEN PLACES", subtitle = "Old Dhaka Architectural Memory", year = "2026")
    ),
    val contactEmail: String = "ijubayer1071@gmail.com",
    val portraitPhotoId: String = "photo_06",
    val secondaryPhotoId: String = "photo_01"
)

data class PracticeItem(
    val index: String,
    val title: String,
    val description: String
)

data class JourneyItem(
    val year: String,
    val title: String
)

data class AboutProjectLink(
    val id: String,
    val title: String,
    val subtitle: String,
    val year: String
)
