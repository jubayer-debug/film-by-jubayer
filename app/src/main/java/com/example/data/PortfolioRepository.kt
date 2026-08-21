package com.example.data

import com.example.data.models.CameraExif
import com.example.data.models.Exhibition
import com.example.data.models.JournalEntry
import com.example.data.models.PhotoCategory
import com.example.data.models.PhotoOrientation
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.data.models.VisualMood

object PortfolioRepository {

    val photographs: List<Photograph> = listOf(
        Photograph(
            id = "photo_01",
            title = "Beyond the River",
            bengaliTitle = "নদীর ওপারে",
            location = "Meghna River, Chandpur",
            year = "2025",
            category = PhotoCategory.RIVER,
            orientation = PhotoOrientation.PANORAMIC,
            caption = "A solitary wooden boat floats across the misty expanse of the Meghna at first light.",
            story = "Dawn over the lower Meghna breaks without sharp contrast—only layers of slate grey, silver mist, and the quiet rhythm of a single wooden oar touching the water. For centuries, these riverways have determined both arrival and departure.",
            mood = VisualMood.RIVER_DAWN,
            exif = CameraExif(camera = "Leica M11-P", lens = "Summilux-M 35mm f/1.4", aperture = "f/2.8", shutter = "1/250s", iso = "ISO 100", focalLength = "35mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_02",
            title = "The Weight of Rain",
            bengaliTitle = "বৃষ্টির ভার",
            location = "Munshiganj, Dhaka",
            year = "2025",
            category = PhotoCategory.MONSOON,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "A villager moves through flooded paths beneath a canopy of palm leaves as monsoon downpours intensify.",
            story = "In Bengal, the monsoon is not merely weather; it is an entire spatial condition. Fields dissolve into inland seas, paths turn to currents, and sound is replaced by the relentless thrum of water on tin roofs.",
            mood = VisualMood.MONSOON_MIST,
            exif = CameraExif(camera = "Leica SL2", lens = "APO-Summicron-SL 50mm f/2", aperture = "f/2.0", shutter = "1/1000s", iso = "ISO 400", focalLength = "50mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_03",
            title = "Lanterns of Shankhari Bazar",
            bengaliTitle = "শাঁখারীবাজারের আলো",
            location = "Old Dhaka",
            year = "2026",
            category = PhotoCategory.STREET,
            orientation = PhotoOrientation.LANDSCAPE,
            caption = "Tungsten glow spilling onto wet cobblestones in the three-hundred-year-old artisan alleyways.",
            story = "Walking the narrow alleys of Old Dhaka at blue hour reveals centuries of layered memory. The smell of mustard oil, incense, and wet clay merges with shadows cast by hand-hung incandescent lamps.",
            mood = VisualMood.OLD_DHAKA_NIGHT,
            exif = CameraExif(camera = "Leica Q3", lens = "Summilux 28mm f/1.7 ASPH", aperture = "f/1.7", shutter = "1/125s", iso = "ISO 1600", focalLength = "28mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_04",
            title = "Silent Shoreline",
            bengaliTitle = "স্তব্ধ তটরেখা",
            location = "Kuakata, Bay of Bengal",
            year = "2024",
            category = PhotoCategory.LANDSCAPE,
            orientation = PhotoOrientation.LANDSCAPE,
            caption = "Submerged mangrove roots standing against the tidal horizon under an overcast sky.",
            story = "At the southern tip of the delta, the land surrenders continuously to the sea. The dead trees stand like silent sentinels, charting the slow retreat of the shoreline over decades.",
            mood = VisualMood.COASTAL_SILENCE,
            exif = CameraExif(camera = "Hasselblad 907X 50C", lens = "XCD 45mm f/4 P", aperture = "f/8.0", shutter = "1/60s", iso = "ISO 64", focalLength = "45mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_05",
            title = "Morning in the High Hills",
            bengaliTitle = "পাহাড়ের ভোর",
            location = "Lawachara, Sreemangal",
            year = "2025",
            category = PhotoCategory.LANDSCAPE,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Emerald contours of tea plantations shrouded in cold morning vapor.",
            story = "Before the tea pickers arrive, the stepped terraces remain immersed in silent fog. The geometric lines carved by hands over a century echo the topography of the gentle rolling ridges.",
            mood = VisualMood.TEA_HIGHLANDS,
            exif = CameraExif(camera = "Leica M11-P", lens = "APO-Summicron-M 75mm f/2", aperture = "f/4.0", shutter = "1/320s", iso = "ISO 200", focalLength = "75mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_06",
            title = "Fisherman of the Padma",
            bengaliTitle = "পদ্মার মাঝি",
            location = "Mawa, Padma River",
            year = "2025",
            category = PhotoCategory.PORTRAIT,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Gaze of a veteran riverman who has navigated three decades of shifting sandbars and currents.",
            story = "His hands bear the deep grooves left by braided nylon fishing nets. He spoke of how the river changes its course every monsoon, erasing homesteads and carving new channels in the dark.",
            mood = VisualMood.PORTRAIT_LIGHT,
            exif = CameraExif(camera = "Leica M11-P", lens = "Noctilux-M 50mm f/0.95 ASPH", aperture = "f/1.4", shutter = "1/1000s", iso = "ISO 100", focalLength = "50mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_07",
            title = "Dusk Over Chalan Beel",
            bengaliTitle = "চলন বিলের গোধূলি",
            location = "Natore, Rajshahi",
            year = "2024",
            category = PhotoCategory.LANDSCAPE,
            orientation = PhotoOrientation.PANORAMIC,
            caption = "Golden reflections stretching across thousands of acres of tranquil marshland.",
            story = "When the sun dips below the horizon in Chalan Beel, the distinction between water and sky dissolves completely into warm sepia and lavender tones. Only the reeds anchor the frame.",
            mood = VisualMood.MEGHNA_DUSK,
            exif = CameraExif(camera = "Hasselblad 907X 50C", lens = "XCD 28mm f/4 P", aperture = "f/5.6", shutter = "1/40s", iso = "ISO 100", focalLength = "28mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_08",
            title = "The Golden Fiber",
            bengaliTitle = "সোনালী আঁশ",
            location = "Faridpur",
            year = "2025",
            category = PhotoCategory.PORTRAIT,
            orientation = PhotoOrientation.LANDSCAPE,
            caption = "Washing and drying freshly harvested raw jute in roadside canal waters.",
            story = "The golden fiber of Bengal has clothed empires and built port cities. Seeing it rinsed in murky ditch water before glowing golden in the sunlight captures the humble poetry of labor.",
            mood = VisualMood.JUTE_HARVEST,
            exif = CameraExif(camera = "Leica M11-P", lens = "Summicron-M 35mm f/2 ASPH", aperture = "f/2.8", shutter = "1/800s", iso = "ISO 200", focalLength = "35mm"),
            isCuratedFeatured = false
        ),
        Photograph(
            id = "photo_09",
            title = "Storm Over Meghna",
            bengaliTitle = "মেঘনার কালবৈশাখী",
            location = "Bhola Channel",
            year = "2026",
            category = PhotoCategory.MONSOON,
            orientation = PhotoOrientation.PANORAMIC,
            caption = "A pre-monsoon Kalbaishakhi storm cloud wall rolling across the wide delta.",
            story = "The sky turns the color of bruised slate in five minutes. Fishermen race their sails toward shore while thunder rumbles deep across thirty miles of open water.",
            mood = VisualMood.RIVER_STORM,
            exif = CameraExif(camera = "Leica SL2", lens = "Vario-Elmarit-SL 24-70mm f/2.8", aperture = "f/5.6", shutter = "1/500s", iso = "ISO 400", focalLength = "24mm"),
            isCuratedFeatured = true
        ),
        Photograph(
            id = "photo_10",
            title = "Shadows in the Courtyard",
            bengaliTitle = "উঠোনের ছায়া",
            location = "Sonargaon, Narayanganj",
            year = "2024",
            category = PhotoCategory.LANDSCAPE,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Afternoon sunlight filtering through betel nut palms onto a mud-swept veranda.",
            story = "The quietest moments in rural life happen between two and four in the afternoon. The birds fall silent, cattle rest in the shade, and only the light shifts across earthen floors.",
            mood = VisualMood.VILLAGE_SHADOW,
            exif = CameraExif(camera = "Leica M11-P", lens = "Summilux-M 35mm f/1.4", aperture = "f/2.0", shutter = "1/640s", iso = "ISO 100", focalLength = "35mm"),
            isCuratedFeatured = false
        ),
        Photograph(
            id = "photo_11",
            title = "Night Ferry at Sadarghat",
            bengaliTitle = "সদরঘাটের রাত",
            location = "Buriganga River, Dhaka",
            year = "2026",
            category = PhotoCategory.STREET,
            orientation = PhotoOrientation.LANDSCAPE,
            caption = "Giant passenger launches moored against the black water of the river terminal under floodlights.",
            story = "Sadarghat never sleeps. Giant multi-deck river steamers depart at midnight for the southern districts, their halogen beams piercing the diesel haze and river fog.",
            mood = VisualMood.OLD_DHAKA_NIGHT,
            exif = CameraExif(camera = "Leica Q3", lens = "Summilux 28mm f/1.7 ASPH", aperture = "f/1.7", shutter = "1/60s", iso = "ISO 3200", focalLength = "28mm"),
            isCuratedFeatured = false
        ),
        Photograph(
            id = "photo_12",
            title = "The Potter of Rayerbazar",
            bengaliTitle = "রায়েরবাজারের কুমার",
            location = "Dhaka",
            year = "2025",
            category = PhotoCategory.PORTRAIT,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Hands shaped by earth, shaping clay lamps before the annual autumn celebrations.",
            story = "For five generations, his family has spun the heavy stone wheel using only hand momentum. In an era of plastic, each clay pot represents stubborn continuity.",
            mood = VisualMood.PORTRAIT_LIGHT,
            exif = CameraExif(camera = "Leica M11-P", lens = "APO-Summicron-M 50mm f/2", aperture = "f/2.0", shutter = "1/400s", iso = "ISO 250", focalLength = "50mm"),
            isCuratedFeatured = false
        )
    )

    val projects: List<Project> = listOf(
        Project(
            id = "proj_01",
            number = "01",
            title = "MONSOON",
            bengaliTitle = "বর্ষাযাপন",
            subtitle = "The Season of Silver Water & Endless Rain",
            location = "Munshiganj & Sylhet, Bangladesh",
            year = "2025",
            photoCount = 14,
            coverPhotoId = "photo_02",
            description = "A multi-year visual chronicle examining how the monsoon reorganizes geography, light, and domestic life across the delta.",
            essayText = "When the monsoon arrives, Bangladesh sheds its dry earthen skin and becomes a vast water country. The boundaries between river and field blur into translucent silver sheets. The photographs in this series do not document catastrophe; rather, they observe the quiet adaptation, resilience, and meditative stillness that settles over communities when water claims the land.",
            photoIds = listOf("photo_02", "photo_09", "photo_01", "photo_04"),
            quote = "In the rain, all geography becomes memory."
        ),
        Project(
            id = "proj_02",
            number = "02",
            title = "RIVER COUNTRY",
            bengaliTitle = "নদীমাতৃক",
            subtitle = "Life Along the Restless Banks of the Meghna",
            location = "Chandpur & Bhola, Bangladesh",
            year = "2024 — 2026",
            photoCount = 18,
            coverPhotoId = "photo_01",
            description = "Documenting the eternal relationship between deltaic communities and the shifting watercourses that give and take away.",
            essayText = "The rivers of Bengal are living creatures. They carve new channels in the dark, submerge entire villages in a single night, and birth fertile silt islands called 'chars'. Those who live on their banks know that stability is an illusion, yet their connection to the water is tender, sacred, and unbroken.",
            photoIds = listOf("photo_01", "photo_06", "photo_07", "photo_09"),
            quote = "The river does not belong to us; we are simply passing through its current."
        ),
        Project(
            id = "proj_03",
            number = "03",
            title = "AFTER RAIN",
            bengaliTitle = "বৃষ্টির পর",
            subtitle = "The Quiet Pause When Earth Absorbs the Sky",
            location = "Chalan Beel & Faridpur",
            year = "2024 — 2025",
            photoCount = 12,
            coverPhotoId = "photo_07",
            description = "Subtle studies of light, damp surfaces, and atmospheric clearing following seasonal cloudbursts.",
            essayText = "The moments immediately after heavy rainfall are marked by an extraordinary acoustic and visual clarity. The air is stripped of dust, leaves hold trembling droplets of light, and the horizon opens up like a freshly washed canvas.",
            photoIds = listOf("photo_07", "photo_10", "photo_04", "photo_08"),
            quote = "Some places are remembered not because they were extraordinary, but because the light stayed."
        ),
        Project(
            id = "proj_04",
            number = "04",
            title = "CITY / NIGHT",
            bengaliTitle = "রাত্রির নগরী",
            subtitle = "Shadows, Lanterns and Steam Across Old Dhaka",
            location = "Dhaka, Bangladesh",
            year = "2025 — 2026",
            photoCount = 16,
            coverPhotoId = "photo_03",
            description = "An intimate nocturnal exploration through historic neighborhoods bathed in incandescent warmth and deep shadows.",
            essayText = "When night descends upon Old Dhaka, the chaos of daytime traffic gives way to intimate pockets of ritual: the hiss of brass samovars, the steady carving of conch shells in Shankhari Bazar, and solitary silhouettes traversing ancient brick archways.",
            photoIds = listOf("photo_03", "photo_11", "photo_06", "photo_12"),
            quote = "Night is when the city whispers its true history."
        ),
        Project(
            id = "proj_05",
            number = "05",
            title = "THE QUIET VILLAGE",
            bengaliTitle = "নিস্তব্ধ গ্রাম",
            subtitle = "Dust Paths, Betel Groves, and Disappearing Dwellings",
            location = "Sonargaon & Bikrampur",
            year = "2023 — 2025",
            photoCount = 20,
            coverPhotoId = "photo_10",
            description = "A preservationist visual archive capturing traditional earthen and timber architecture slowly giving way to concrete.",
            essayText = "As rural youth migrate toward urban centers, the traditional homestead—with its open earthen courtyard, hand-woven bamboo mats, and shaded betel nut groves—is transforming. This series serves as an homage to the architecture of slowness.",
            photoIds = listOf("photo_10", "photo_08", "photo_05", "photo_12"),
            quote = "I photograph places that disappear slowly."
        ),
        Project(
            id = "proj_06",
            number = "06",
            title = "TEA HIGHLANDS",
            bengaliTitle = "চায়ের দেশ",
            subtitle = "Mist in the Rolling Hills of Sreemangal",
            location = "Moulvibazar, Sylhet",
            year = "2024",
            photoCount = 15,
            coverPhotoId = "photo_05",
            description = "Explorations of geometric tea garden terraces, ancient rubber estates, and the indigenous tea communities.",
            essayText = "High above the floodplains, the tea estates of Sylhet form a quiet world of emerald green sculpted contours. The fog rolls in at dawn, wrapping the hilltops in cold solitude before the sun breaks through.",
            photoIds = listOf("photo_05", "photo_04", "photo_02", "photo_10"),
            quote = "In the hills, silence has weight."
        )
    )

    val journalEntries: List<JournalEntry> = listOf(
        JournalEntry(
            id = "journal_01",
            title = "The River After Rain",
            bengaliTitle = "বৃষ্টিপরবর্তী নদী",
            date = "AUGUST 2026",
            location = "Meghna, Bangladesh",
            readTime = "4 MIN READ",
            coverPhotoId = "photo_01",
            excerpt = "Notes on the peculiar quality of light that follows three days of continuous monsoon rainfall along the Chandpur coastline.",
            content = "For three days, the horizon was swallowed whole by grey vapor. When the rain finally ceased yesterday at 5:00 AM, the Meghna did not rush—it breathed. A low mist hovered exactly three feet above the water surface, creating the uncanny illusion that the wooden cargo boats were floating in mid-air.\n\nI sat on a bamboo mooring post for three hours with the Leica M11 and a 35mm lens. In digital photography, one is always tempted to chase sharpness, but here, the entire philosophy is about softness—how the eye perceives distant shapes through water-laden air.\n\nPhotography is less about the capture of a moment than the patience to let the landscape reveal its interior state.",
            quote = "Water is the only mirror that never lies, yet never stays still."
        ),
        JournalEntry(
            id = "journal_02",
            title = "Monsoon as a State of Mind",
            bengaliTitle = "বর্ষা এক অনুভূতির নাম",
            date = "JUNE 2026",
            location = "Munshiganj",
            readTime = "6 MIN READ",
            coverPhotoId = "photo_02",
            excerpt = "How the rhythm of endless rainfall alters human perception of time, distance, and solitude.",
            content = "In the West, rain is often framed as an interruption—a delay in productivity. In Bengal, rain is the fundamental rhythm of life. Everything slows down. Conversations on tin-roofed verandas stretch for hours. People walk with deliberate care, holding black umbrellas against the sky.\n\nTo photograph in the monsoon requires protecting your camera with wax canvas and accepting that your lenses will fog. But the reward is a tonal spectrum you cannot find anywhere else on earth: deep olive greens, silver reflections, and skin tones illuminated by soft diffused daylight.",
            quote = "The sound of rain on tin is the oldest lullaby of the delta."
        ),
        JournalEntry(
            id = "journal_03",
            title = "The Architecture of Passing Light",
            bengaliTitle = "ক্ষণস্থায়ী আলোর স্থাপত্য",
            date = "APRIL 2026",
            location = "Old Dhaka",
            readTime = "5 MIN READ",
            coverPhotoId = "photo_03",
            excerpt = "Observing how narrow three-foot alleyways funnel direct sunlight for only seven minutes each afternoon.",
            content = "In Shankhari Bazar, the buildings stand so tall and close together that the street below feels like a canyon. Direct sunlight reaches the cobblestones for only a fleeting window around 1:45 PM. During those seven minutes, particles of dust, spice powder, and incense glow like constellations.\n\nI have stood in the same corner for weeks waiting for that precise angle of illumination. When it arrives, the geometry of Old Dhaka transforms from cramped urban density into an open cathedral of light.",
            quote = "Light does not reveal space; light creates space."
        )
    )

    val exhibitions: List<Exhibition> = listOf(
        Exhibition(
            year = "2026",
            title = "Places That Disappear Slowly",
            venue = "Drik Gallery, Dhaka",
            location = "Dhaka, Bangladesh",
            type = "Solo Exhibition"
        ),
        Exhibition(
            year = "2025",
            title = "Deltaic Memories: South Asian Photography Survey",
            venue = "Bengal Foundation / Chobi Mela",
            location = "Dhaka, Bangladesh",
            type = "Group Exhibition"
        ),
        Exhibition(
            year = "2024",
            title = "The Quiet River: Contemporary Documentary Archives",
            venue = "National Museum of Art",
            location = "Kolkata, India",
            type = "Featured Archive"
        ),
        Exhibition(
            year = "2023",
            title = "Passing Light: Monochromatic Studies",
            venue = "Alliance Française de Dhaka",
            location = "Dhaka, Bangladesh",
            type = "Solo Exhibition"
        )
    )

    fun getPhotoById(id: String): Photograph? {
        return photographs.find { it.id == id }
    }

    fun getProjectById(id: String): Project? {
        return projects.find { it.id == id }
    }

    fun getJournalById(id: String): JournalEntry? {
        return journalEntries.find { it.id == id }
    }
}
