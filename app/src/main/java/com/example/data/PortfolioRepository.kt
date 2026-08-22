package com.example.data

import com.example.data.models.CameraExif
import com.example.data.models.Exhibition
import com.example.data.models.JournalEntry
import com.example.data.models.PhotoCategory
import com.example.data.models.PhotoOrientation
import com.example.data.models.Photograph
import com.example.data.models.Project
import com.example.data.models.VisualMood
import kotlin.random.Random

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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=600&q=80"
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
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80"
        ),
        Photograph(
            id = "photo_13",
            title = "Whispers of the Mangrove",
            bengaliTitle = "সুন্দরবনের নিঃশব্দতা",
            location = "Sundarbans Delta",
            year = "2025",
            category = PhotoCategory.RIVER,
            orientation = PhotoOrientation.LANDSCAPE,
            caption = "Tidal channels cutting through ancient roots under low mist.",
            story = "The mangrove jungle breathes with the tides. Mud banks submerge and reappear twice a day, leaving intricate patterns in silt and roots.",
            mood = VisualMood.COASTAL_SILENCE,
            exif = CameraExif(camera = "Leica M11-P", lens = "Summilux-M 35mm f/1.4", aperture = "f/4.0", shutter = "1/500s", iso = "ISO 160", focalLength = "35mm"),
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=600&q=80"
        ),
        Photograph(
            id = "photo_14",
            title = "Cobblestone Solitude",
            bengaliTitle = "রাত্রির পথ",
            location = "Armanitola, Old Town",
            year = "2026",
            category = PhotoCategory.STREET,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Solitary cyclist gliding past century-old wrought iron balconies.",
            story = "Late night in the old quarter strips away the chaos of daytime commerce. Only the rhythmic click of bicycle pedals and amber streetlights remain.",
            mood = VisualMood.OLD_DHAKA_NIGHT,
            exif = CameraExif(camera = "Leica Q3", lens = "Summilux 28mm f/1.7 ASPH", aperture = "f/1.8", shutter = "1/80s", iso = "ISO 2500", focalLength = "28mm"),
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=600&q=80"
        ),
        Photograph(
            id = "photo_15",
            title = "Monsoon Canopy",
            bengaliTitle = "মেঘের ছাতা",
            location = "Tanguar Haor, Sunamganj",
            year = "2025",
            category = PhotoCategory.MONSOON,
            orientation = PhotoOrientation.PANORAMIC,
            caption = "Infinite horizon of wetlands merging with deep monsoon indigo clouds.",
            story = "The vast haor basin transforms into an inland sea. Wooden country boats look like tiny paper vessels resting on glassy stillness.",
            mood = VisualMood.MONSOON_MIST,
            exif = CameraExif(camera = "Hasselblad 907X 50C", lens = "XCD 28mm f/4 P", aperture = "f/8.0", shutter = "1/125s", iso = "ISO 100", focalLength = "28mm"),
            isCuratedFeatured = true,
            imageUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=600&q=80"
        ),
        Photograph(
            id = "photo_16",
            title = "Gaze of the Weaver",
            bengaliTitle = "তাঁতীর দৃষ্টি",
            location = "Tangail",
            year = "2025",
            category = PhotoCategory.PORTRAIT,
            orientation = PhotoOrientation.PORTRAIT,
            caption = "Master weaver adjusting fine silk threads in a sunlit wooden loom workshop.",
            story = "Decades of listening to the wooden shuttle's clack-clack rhythm have sharpened his instincts. He can detect a single broken thread by touch alone.",
            mood = VisualMood.PORTRAIT_LIGHT,
            exif = CameraExif(camera = "Leica M11-P", lens = "Summilux-M 50mm f/1.4", aperture = "f/1.4", shutter = "1/640s", iso = "ISO 200", focalLength = "50mm"),
            isCuratedFeatured = false,
            imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=1600&q=85",
            thumbUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=600&q=80"
        )
    )

    // Dynamic Random Discovery Helpers
    fun getRandomPhotograph(excludeId: String? = null): Photograph {
        val pool = if (excludeId != null) photographs.filter { it.id != excludeId } else photographs
        return (pool.ifEmpty { photographs }).random()
    }

    fun getShuffledPhotographs(seed: Long = System.currentTimeMillis()): List<Photograph> {
        val rnd = Random(seed)
        return photographs.shuffled(rnd)
    }

    fun getRandomCuratedDiscovery(count: Int = 4): List<Photograph> {
        return photographs.shuffled().take(count.coerceAtMost(photographs.size))
    }

    fun getRandomPhotographForCategory(category: PhotoCategory): Photograph {
        val pool = if (category == PhotoCategory.ALL) photographs else photographs.filter { it.category == category }
        return (pool.ifEmpty { photographs }).random()
    }

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
            photoIds = listOf("photo_02", "photo_09", "photo_01", "photo_04", "photo_15"),
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
            photoIds = listOf("photo_01", "photo_06", "photo_07", "photo_09", "photo_13"),
            quote = "The river does not belong to us; we are simply passing through its current."
        ),
        Project(
            id = "proj_03",
            number = "03",
            title = "AFTER RAIN",
            bengaliTitle = "বৃষ্টির পর",
            subtitle = "Quietude, Wet Silt & Soft Evening Horizon",
            location = "Faridpur & Munshiganj",
            year = "2025",
            photoCount = 12,
            coverPhotoId = "photo_04",
            description = "The fragile stillness that lingers in village paths immediately after hours of torrential downpour.",
            essayText = "The moments immediately following heavy rainfall hold a peculiar tenderness. The air smells intensely of wet humus and crushed grass. Children emerge cautiously into flooded paths, and the sky mirrors itself in thousands of shallow puddles across the courtyard.",
            photoIds = listOf("photo_04", "photo_08", "photo_10", "photo_12"),
            quote = "Light after water is softer than any memory."
        ),
        Project(
            id = "proj_04",
            number = "04",
            title = "OLD TOWN SHADOWS",
            bengaliTitle = "পুরান ঢাকা",
            subtitle = "Three Centuries of Indigo Twilight and Tungsten Glow",
            location = "Shankhari Bazar & Sadarghat, Old Dhaka",
            year = "2024 — 2026",
            photoCount = 22,
            coverPhotoId = "photo_03",
            description = "A nocturnal study of Old Dhaka's historic alleys, traditional artisans, and timeless communal architecture.",
            essayText = "Within the tightly woven maze of Old Dhaka, time functions non-linearly. Mughal gateways stand beside colonial brickwork and buzzing neon signs. In these narrow fissures, generations of craftsmen continue their ancestral callings with quiet dignity.",
            photoIds = listOf("photo_03", "photo_11", "photo_14", "photo_06"),
            quote = "History here is not carved in marble, but worn into the stones by barefoot steps."
        )
    )

    val journalEntries: List<JournalEntry> = listOf(
        JournalEntry(
            id = "journal_01",
            title = "On Analog Patience: Why 35mm Still Matters",
            bengaliTitle = "ফিল্মের ধৈর্য",
            date = "OCTOBER 14, 2025",
            readTime = "4 MIN READ",
            location = "Munshiganj Field Diary",
            excerpt = "When you only have 36 exposures on a roll of Tri-X, the shutter press ceases to be a reaction and becomes an act of deliberate listening.",
            content = "In an age where digital sensors can capture thirty frames per second, the deliberate restriction of 36 frames per roll enforces a meditative discipline. You learn to wait for the exact moment when the light strikes the water at a forty-five-degree angle, or when the subject's posture relaxes into authentic presence.\n\nIn the river delta, where weather and geography shift by the hour, film captures not just photons, but the tangible density of air, humidity, and time itself. The organic grain structure of silver gelatin emulsions possesses a warmth and emotional resonance that synthetic pixels struggle to emulate.",
            coverPhotoId = "photo_02"
        ),
        JournalEntry(
            id = "journal_02",
            title = "The Architecture of River Mist",
            bengaliTitle = "নদীর কুয়াশা",
            date = "NOVEMBER 28, 2025",
            readTime = "6 MIN READ",
            location = "Chandpur Meghna Confluence",
            excerpt = "Mist is not an obstruction of vision; it is a simplifier of space. It strips the world down to essential silhouettes and soft tonal gradients.",
            content = "Standing on the boat deck at 5:30 AM where the Padma, Meghna, and Dakatia rivers converge, the horizon disappears. There is no top, no bottom, only a uniform silver dome.\n\nPhotographing in thick river fog requires abandoning traditional composition rules based on leading lines and focal points. Instead, you compose with tonal mass and spatial breathing room. A solitary wooden mast sixty yards away becomes a profound philosophical statement about human fragility in vast nature.",
            coverPhotoId = "photo_01"
        ),
        JournalEntry(
            id = "journal_03",
            title = "Tungsten and Memory in Shankhari Bazar",
            bengaliTitle = "শাঁখারীবাজারের স্মৃতি",
            date = "JANUARY 08, 2026",
            readTime = "5 MIN READ",
            location = "Old Dhaka Alleyways",
            excerpt = "The 2700 Kelvin glow of incandescent bulbs preserves a visual warmth that modern cool LEDs have erased from contemporary cities.",
            content = "Old Dhaka at 8 PM is an alchemy of sound, aroma, and warm tungsten light. Walking with a 28mm lens close to the chest, you encounter artisans carving conch shells by the light of a single bare bulb.\n\nThe shadows here are deep and velvety. By exposing for the highlights on their weathered hands, the background naturally recedes into cinematic darkness, turning an everyday alley into an intimate stage of living heritage.",
            coverPhotoId = "photo_03"
        )
    )

    val exhibitions: List<Exhibition> = listOf(
        Exhibition(
            year = "2026",
            title = "SILVER WATER & SILENT DELTA",
            venue = "Bengal Foundation Gallery",
            location = "Dhaka, Bangladesh",
            type = "Solo Exhibition"
        ),
        Exhibition(
            year = "2025",
            title = "CHOBI MELA XII: RESILIENCE & TIDE",
            venue = "Shilpakala Academy",
            location = "Dhaka",
            type = "Featured Artist"
        ),
        Exhibition(
            year = "2024",
            title = "SOUTH ASIAN VISUAL FORUM",
            venue = "India International Centre",
            location = "New Delhi",
            type = "Group Exhibition"
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
