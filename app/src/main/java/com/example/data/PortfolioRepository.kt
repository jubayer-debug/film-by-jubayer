package com.example.data

import com.example.data.models.AlbumFrame
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

    private val defaultPhotographs: List<Photograph> = listOf(
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

    private val defaultProjects: List<Project> = listOf(
        Project(
            id = "proj_01",
            number = "01",
            title = "BOISHAKH 1433",
            bengaliTitle = "বৈশাখ ১৪৩৩",
            subtitle = "Terracotta Kilns, Folk Artisans & Pahela Boishakh",
            location = "DHAKA, BANGLADESH",
            year = "2026",
            photoCount = 26,
            coverPhotoId = "photo_08",
            description = "A photographic exploration into the artisanal terracotta studios and earthen kiln workshops preparing for Pahela Boishakh 1433 celebrations across Dhaka.",
            essayText = "In the narrow terracotta yards and pottery settlements on the city's outskirts, the weeks preceding Pahela Boishakh turn into a mesmerizing dance with earth, fire, and pigment. Sculptors shape traditional motifs while drying racks overflow with handcrafted lamps and terracotta figurines.",
            photoIds = listOf("photo_08", "photo_03", "photo_16", "photo_11"),
            quote = "Clay carries the warmth of thousand hands.",
            viewCount = 185,
            dateFormatted = "APRIL 2026",
            frameList = listOf(
                AlbumFrame("b1433_01", "01", "The Master Sculptor's Hands", "ভাস্করের করস্পর্শ", "Rayerbazar", "2026", "Leica M11-P • 50mm f/1.4 • 1/250s • ISO 200", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Centuries of lineage passed down in every thumbprint on raw clay."),
                AlbumFrame("b1433_02", "02", "Drying Racks in Morning Mist", "ভোরের রোদে মাটির সরা", "Dhamrai Pottery", "2026", "Leica M11-P • 35mm f/2.0 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Hundreds of freshly molded earthen plates absorbing the first sunlight."),
                AlbumFrame("b1433_03", "03", "Crimson Alpona Pigments", "আলপনার লাল ও সাদা", "Shankhari Bazar", "2026", "Leica M11-P • 50mm f/1.4 • 1/500s • ISO 160", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Natural vermilion and rice paste ground by mortar for the festival motifs."),
                AlbumFrame("b1433_04", "04", "Wheel of Clay & Memory", "কুমোরের চাকা", "Rayerbazar", "2026", "Hasselblad 907X • 45mm f/4 • 1/125s • ISO 100", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80", "Spun solely by wooden stick and centrifugal momentum."),
                AlbumFrame("b1433_05", "05", "Terracotta Folk Owl (Lokkhi Pecha)", "মাটির লক্ষ্মী পেঁচা", "Dhamrai", "2026", "Leica M11-P • 75mm f/2.0 • 1/320s • ISO 200", "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80", "Symbol of quiet wisdom and rural agrarian abundance."),
                AlbumFrame("b1433_06", "06", "Kiln Fire at Dawn", "ভাটির গনগনে আগুন", "Savar Outskirts", "2026", "Leica M11-P • 35mm f/1.4 • 1/80s • ISO 800", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=800&q=80", "Rice husk flames baking raw earth into resonant brick-red earthenware."),
                AlbumFrame("b1433_07", "07", "Dhak Drummer's Rehearsal", "ঢাকের তাল ও কাঠি", "Armanitola", "2026", "Leica M11-P • 50mm f/1.4 • 1/640s • ISO 400", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Tuning the goat-hide membrane before the sunrise procession."),
                AlbumFrame("b1433_08", "08", "Earthen Water Jugs (Kula & Hari)", "মাটির হাঁড়ি ও কলসি", "Bangla Bazar", "2026", "Leica M11-P • 35mm f/2.8 • 1/250s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Stacked geometrically in traditional woven straw baskets."),
                AlbumFrame("b1433_09", "09", "The Mask Carver of Fine Arts", "মুখোশের কারিগর", "Charukola, DU", "2026", "Leica M11-P • 50mm f/2.0 • 1/400s • ISO 200", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Papier-mâché tiger masks drying in the campus courtyard."),
                AlbumFrame("b1433_10", "10", "Child with Earthen Whistle", "মাটির দোতারা ও বাঁশি", "Ramna Batamul", "2026", "Leica M11-P • 50mm f/1.4 • 1/1000s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Testing the pitch of bird-shaped terracotta whistles."),
                AlbumFrame("b1433_11", "11", "Chalking the New Ledger (Halkhata)", "হালখাতার লাল খাতা", "Shankhari Bazar", "2026", "Leica M11-P • 35mm f/2.0 • 1/200s • ISO 320", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Traditional red cloth accounting books opened with sweets and incense."),
                AlbumFrame("b1433_12", "12", "Brass Bells & Cymbals", "কাঁসার ঘণ্টা ও মন্দিরা", "Dhamrai Brass", "2026", "Hasselblad 907X • 80mm f/2.8 • 1/250s • ISO 100", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Lost-wax cast bells polished to a golden resonance."),
                AlbumFrame("b1433_13", "13", "Sari Weaver's Red Border", "লাল পাড়ের শাড়ি", "Tangail", "2026", "Leica M11-P • 50mm f/1.4 • 1/640s • ISO 200", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "Fine cotton woven with classic red temple borders."),
                AlbumFrame("b1433_14", "14", "Shola Pith Floral Ornaments", "শোলা শিল্পের কদম ফুল", "Tantibazar", "2026", "Leica M11-P • 75mm f/2.4 • 1/320s • ISO 160", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Intricate white botanical carvings made from marsh reed pith."),
                AlbumFrame("b1433_15", "15", "Bamboo Flute Maker", "বাঁশের বাঁশির সুর", "Buriganga Ghat", "2026", "Leica M11-P • 35mm f/2.0 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Burning tuning holes into seasoned river bamboo with hot steel wire."),
                AlbumFrame("b1433_16", "16", "Jaggery & Puffed Rice Feast", "মুড়ি ও নলেন গুড়", "Nawabpur", "2026", "Leica M11-P • 50mm f/2.8 • 1/250s • ISO 200", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=800&q=80", "Fresh winter date-palm molasses prepared for festival morning breakfast."),
                AlbumFrame("b1433_17", "17", "Ghoti & Earthen Pitcher", "মাটির কলস ও ঘটি", "Dhamrai", "2026", "Leica M11-P • 35mm f/2.0 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "Porous earthen pottery that naturally chills drinking water."),
                AlbumFrame("b1433_18", "18", "Morning Raga at Ramna Batamul", "রমনার ভোরের রাগ", "Ramna Park", "2026", "Leica M11-P • 50mm f/1.4 • 1/125s • ISO 400", "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80", "Hundreds gathered under the banyan canopy at daybreak."),
                AlbumFrame("b1433_19", "19", "Handmade Paper Garlands", "রঙিন কাগজের ঝালর", "Chawkbazar", "2026", "Leica M11-P • 35mm f/2.8 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80", "Geometric accordion paper garlands strung across streets."),
                AlbumFrame("b1433_20", "20", "The Mask Painter's Stroke", "তুলির শেষ আঁচড়", "Charukola", "2026", "Leica M11-P • 75mm f/2.0 • 1/640s • ISO 200", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Applying bold black outlines around the eyes of the peace dove mask."),
                AlbumFrame("b1433_21", "21", "Folk Dancers in Golden Light", "নবান্ন ও বৈশাখের নৃত্য", "Shahbagh", "2026", "Leica M11-P • 50mm f/1.4 • 1/1000s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Swirling hand gestures echoing the waves of Bengal rivers."),
                AlbumFrame("b1433_22", "22", "Handwoven Palm-Leaf Fans", "তালপাতার পাখা", "Sonargaon", "2026", "Hasselblad 907X • 45mm f/4 • 1/200s • ISO 100", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Bordered with bright pink thread and geometric bamboo ribs."),
                AlbumFrame("b1433_23", "23", "Old Potter's Smile", "মৃত্তিকাশিল্পীর তৃপ্তি", "Rayerbazar", "2026", "Leica M11-P • 50mm f/1.4 • 1/400s • ISO 200", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Sixty years of turning earth into everyday poetry."),
                AlbumFrame("b1433_24", "24", "Festival Crowd at Sunset", "সন্ধ্যাবেলার মেলা", "Dhanmondi Lake", "2026", "Leica M11-P • 35mm f/1.4 • 1/160s • ISO 640", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Lanterns reflecting on water as the first day of 1433 turns to night."),
                AlbumFrame("b1433_25", "25", "Mangal Shobhajatra Procession", "মঙ্গল শোভাযাত্রা", "Manik Mia Avenue", "2026", "Leica M11-P • 28mm f/2.8 • 1/800s • ISO 100", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Giant woven tiger and bird effigies held aloft under golden sunlight."),
                AlbumFrame("b1433_26", "26", "Earthen Lamps at Twilight", "প্রদীপের স্নিগ্ধ আলো", "Old Dhaka Roof", "2026", "Leica M11-P • 50mm f/1.2 • 1/60s • ISO 1250", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Clay oil lamps lit on rooftop cornices welcoming the new year.")
            )
        ),
        Project(
            id = "proj_02",
            number = "02",
            title = "MONSOON ARCHIVE",
            bengaliTitle = "বর্ষার দলিল",
            subtitle = "The Season of Silver Water & Endless Rain",
            location = "MUNSHIGANJ, BANGLADESH",
            year = "2025",
            photoCount = 20,
            coverPhotoId = "photo_02",
            description = "A multi-year visual chronicle examining how the monsoon reorganizes geography, light, and domestic life across the delta.",
            essayText = "When the monsoon arrives, Bangladesh sheds its dry earthen skin and becomes a vast water country. The boundaries between river and field blur into translucent silver sheets. The photographs in this series do not document catastrophe; rather, they observe the quiet adaptation, resilience, and meditative stillness that settles over communities when water claims the land.",
            photoIds = listOf("photo_02", "photo_09", "photo_01", "photo_04", "photo_15"),
            quote = "In the rain, all geography becomes memory.",
            viewCount = 240,
            dateFormatted = "JULY 2025",
            frameList = listOf(
                AlbumFrame("ma_01", "01", "First Rain on Dhaleshwari", "ধলেশ্বরীতে প্রথম বর্ষণ", "Munshiganj", "2025", "Leica SL2 • 50mm f/2.0 • 1/1000s • ISO 400", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=800&q=80", "Heavy drops breaking the mirror stillness of the river surface."),
                AlbumFrame("ma_02", "02", "Submerged Country Pathway", "ডুবে যাওয়া মেঠোপথ", "Louhajang", "2025", "Leica SL2 • 35mm f/2.8 • 1/500s • ISO 200", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "Where cows once walked, small wooden skiffs now navigate easily."),
                AlbumFrame("ma_03", "03", "Rain on Corrugated Tin", "টিনের চালে বৃষ্টির শব্দ", "Sirajdikhan", "2025", "Leica SL2 • 50mm f/1.4 • 1/250s • ISO 800", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80", "The hypnotic acoustic landscape of rural Bengal during a storm."),
                AlbumFrame("ma_04", "04", "Solitary Fisherman with Cast Net", "ঝাঁকি জাল ও বৃষ্টির ধোঁয়া", "Padma Floodplain", "2025", "Hasselblad 907X • 45mm f/4 • 1/320s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Casting a circular net into swirling currents with practiced grace."),
                AlbumFrame("ma_05", "05", "Jute Bundles Soaking in Flood", "জলে ডোবানো পাট", "Tongibari", "2025", "Leica SL2 • 35mm f/2.8 • 1/640s • ISO 200", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Fermenting raw jute stalks weighted down with clay sods."),
                AlbumFrame("ma_06", "06", "Palm Leaf Umbrella (Mathal)", "মাথাল মাথায় চাষী", "Srinagar", "2025", "Leica SL2 • 50mm f/2.0 • 1/500s • ISO 400", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Conical woven palm hat protecting against torrential skies."),
                AlbumFrame("ma_07", "07", "Water Lily Floating Market", "শাপলার ভাসমান হাট", "Ariyal Beel", "2025", "Leica SL2 • 28mm f/2.8 • 1/800s • ISO 100", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Boats loaded high with national white water lilies harvested at dawn."),
                AlbumFrame("ma_08", "08", "Monsoon Twilight over Marshland", "বিল পারের মেঘের ছায়া", "Tanguar Haor", "2025", "Hasselblad 907X • 28mm f/4 • 1/60s • ISO 200", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Indigo cloud formations sinking into infinite wetlands."),
                AlbumFrame("ma_09", "09", "Children Swimming with Banana Raft", "কলাগাছের ভেলা", "Munshiganj", "2025", "Leica SL2 • 50mm f/1.4 • 1/1200s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Paddling across courtyards turned into clear freshwater lakes."),
                AlbumFrame("ma_10", "10", "Drying Fish in Rain Break", "রোদের দেখা ও শুঁটকি", "Padma Bank", "2025", "Leica SL2 • 35mm f/4.0 • 1/800s • ISO 100", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Spreading silver hilsa on bamboo scaffolds during temporary sunshine."),
                AlbumFrame("ma_11", "11", "Duck Herd in Low Silt Basin", "হাঁসের ঝাঁক", "Sirajdikhan", "2025", "Leica SL2 • 70mm f/2.8 • 1/500s • ISO 200", "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80", "A thousand white ducks navigating newly created monsoon ponds."),
                AlbumFrame("ma_12", "12", "Cattle on High Char Island", "চরে গরুর পাল", "Char Fasson", "2025", "Hasselblad 907X • 45mm f/4 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Shepherds gathering livestock on elevated mud mounds above high tide."),
                AlbumFrame("ma_13", "13", "The Tea Stall Rain Refuge", "টং দোকানে বৃষ্টিবিলাস", "Louhajang Ghat", "2025", "Leica SL2 • 35mm f/1.4 • 1/100s • ISO 1600", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Steaming condensed milk tea shared under canvas tarpaulins."),
                AlbumFrame("ma_14", "14", "Storm Approaching Meghna Mouth", "কালবৈশাখীর কালো মেঘ", "Bhola Channel", "2025", "Leica SL2 • 24mm f/5.6 • 1/500s • ISO 400", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "A towering wall of slate-grey vapor advancing over the water."),
                AlbumFrame("ma_15", "15", "Silver Hilsa Catch", "পদ্মার তাজা ইলিশ", "Mawa Ghat", "2025", "Leica SL2 • 50mm f/2.0 • 1/640s • ISO 200", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "Glistening scales reflecting the soft diffuse overcast daylight."),
                AlbumFrame("ma_16", "16", "Old Ferry in Thunderstorm", "ঝড়ে নদীর পারাপার", "Shimulia", "2025", "Leica SL2 • 35mm f/2.8 • 1/200s • ISO 800", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=800&q=80", "Steel hull plowing steadily through foam and churning waves."),
                AlbumFrame("ma_17", "17", "Lotus Leaf Water Pearls", "পদ্মপাতায় জলের ফোঁটা", "Gajner Beel", "2025", "Leica SL2 • 90mm f/2.8 Macro • 1/320s • ISO 100", "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80", "Perfect hydrophobic spheres rolling across emerald veined leaves."),
                AlbumFrame("ma_18", "18", "Night Mist in Village Courtyard", "উঠোনে রাতের বাদল", "Munshiganj", "2025", "Leica SL2 • 50mm f/1.4 • 1/30s • ISO 3200", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Kerosene flame haloed by thick humid air and wet foliage."),
                AlbumFrame("ma_19", "19", "Boat Builder Replacing Planks", "নৌকা মেরামতের কাজ", "Baligaon", "2025", "Hasselblad 907X • 45mm f/4 • 1/250s • ISO 200", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Caulking timber joints with coal tar and beaten jute fiber."),
                AlbumFrame("ma_20", "20", "Clearing Skies at Sunset", "শ্রাবণের মেঘমুক্ত আকাশ", "Dhaleshwari Confluence", "2025", "Leica SL2 • 35mm f/4.0 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Golden rays breaking through parting stratus clouds after three days of rain.")
            )
        ),
        Project(
            id = "proj_03",
            number = "03",
            title = "RIVER COUNTRY",
            bengaliTitle = "নদীমাতৃক",
            subtitle = "Life Along the Restless Banks of the Meghna",
            location = "CHANDPUR, BANGLADESH",
            year = "2025",
            photoCount = 22,
            coverPhotoId = "photo_01",
            description = "Documenting the eternal relationship between deltaic communities and the shifting watercourses that give and take away.",
            essayText = "The rivers of Bengal are living creatures. They carve new channels in the dark, submerge entire villages in a single night, and birth fertile silt islands called 'chars'. Those who live on their banks know that stability is an illusion, yet their connection to the water is tender, sacred, and unbroken.",
            photoIds = listOf("photo_01", "photo_06", "photo_07", "photo_09", "photo_13"),
            quote = "The river does not belong to us; we are simply passing through its current.",
            viewCount = 310,
            dateFormatted = "SEPTEMBER 2025",
            frameList = listOf(
                AlbumFrame("rc_01", "01", "Three Rivers Meeting at Dawn", "ত্রিনদীর মোহনা", "Chandpur", "2025", "Leica M11-P • 35mm f/2.8 • 1/250s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Padma, Meghna and Dakatia rivers converging in a silver dawn dome."),
                AlbumFrame("rc_02", "02", "The Veteran River Pilot", "অভিজ্ঞ নদীর মাঝি", "Mawa Ghat", "2025", "Leica M11-P • 50mm f/1.4 • 1/1000s • ISO 100", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Eyes trained to read unseen underwater sandbanks by subtle wave ripples."),
                AlbumFrame("rc_03", "03", "Char Island Migration", "চরের স্থানান্তর", "Haimchar", "2025", "Hasselblad 907X • 45mm f/4 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Dismantling tin homesteads to move two miles downriver before bank erosion."),
                AlbumFrame("rc_04", "04", "Wooden Dinghy at Rest", "ঘাটে বাঁধা ডিঙি", "Puran Bazar Ghat", "2025", "Leica M11-P • 35mm f/2.0 • 1/320s • ISO 100", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Moored to a weathered bamboo stake with coir rope."),
                AlbumFrame("rc_05", "05", "Morning Fish Auction on Barges", "নদীবক্ষে মাছের ডাক", "Chandpur Boro Station", "2025", "Leica M11-P • 50mm f/2.0 • 1/400s • ISO 400", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Hundreds of baskets of fresh catch traded before sunrise."),
                AlbumFrame("rc_06", "06", "Net Repair in Sand Dune", "বালুচরে জাল সেলাই", "Char Fasson", "2025", "Leica M11-P • 35mm f/2.8 • 1/800s • ISO 200", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Mending tear holes in blue monofilament netting under midday sun."),
                AlbumFrame("rc_07", "07", "The Shifting Bank Erosion", "নদীভাঙনের চিহ্ন", "Naria, Shariatpur", "2025", "Hasselblad 907X • 28mm f/5.6 • 1/250s • ISO 100", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "Sheer silt cliffs dropping into turbulent eddy currents."),
                AlbumFrame("rc_08", "08", "Paddle Wheel Steamer at Night", "রকেট স্টিমারের আলো", "Chandpur Ghat", "2025", "Leica Q3 • 28mm f/1.7 • 1/60s • ISO 2500", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=800&q=80", "Centuries-old colonial river transport cruising with rhythmic chugging."),
                AlbumFrame("rc_09", "09", "Sundarbans Mudflats & Roots", "সুন্দরবনের শ্বাসমূল", "Kotka, Sundarbans", "2025", "Leica M11-P • 35mm f/4.0 • 1/500s • ISO 160", "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80", "Pneumatophore root needles piercing tidal silt at low water."),
                AlbumFrame("rc_10", "10", "Night Ferry Searchlight", "লঞ্চের সার্চলাইট", "Meghna Estuary", "2025", "Leica M11-P • 50mm f/1.4 • 1/40s • ISO 3200", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Piercing 2000-meter halogen beam scanning river buoys in pitch darkness."),
                AlbumFrame("rc_11", "11", "Boy with River Silt Crab", "কাদামাটির কাঁকড়া শিকার", "Monpura Island", "2025", "Leica M11-P • 50mm f/1.4 • 1/800s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Harvesting mud crabs from mangrove estuary banks."),
                AlbumFrame("rc_12", "12", "Char Island Crop Farming", "পলিমাটিতে তরমুজ চাষ", "Char Alexander", "2025", "Hasselblad 907X • 45mm f/4 • 1/640s • ISO 100", "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80", "Watermelon seedlings thriving in mineral-rich fresh delta silt."),
                AlbumFrame("rc_13", "13", "Sails against the Setting Sun", "মেঘনায় পালের নাও", "Chandpur Estuary", "2025", "Leica M11-P • 35mm f/2.0 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80", "Traditional square sails billowing with the south wind."),
                AlbumFrame("rc_14", "14", "Washing Copper Vessels on Ghat", "ঘাটের পিতল মাজা", "Boro Station", "2025", "Leica M11-P • 50mm f/2.0 • 1/500s • ISO 200", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Scouring cooking handis with fine river sand until they gleam."),
                AlbumFrame("rc_15", "15", "River Gull in Flight", "গাঙচিলের ডানা", "Bhola Channel", "2025", "Leica SL2 • 70-200mm f/2.8 • 1/2000s • ISO 200", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Trailing fishing trawlers for discarded fish entrails."),
                AlbumFrame("rc_16", "16", "The Char Village School Boat", "ভাসমান পাঠশালা", "Chalan Beel / Meghna", "2025", "Leica M11-P • 35mm f/2.8 • 1/320s • ISO 100", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "Solar-powered boat classroom bringing education across river reaches."),
                AlbumFrame("rc_17", "17", "Elder Storyteller on Riverbank", "নদীর প্রাচীন কিসসা", "Haimchar", "2025", "Leica M11-P • 50mm f/1.4 • 1/250s • ISO 200", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Recounting three major floods that relocated his ancestral home."),
                AlbumFrame("rc_18", "18", "Sand Dredger Silhouette", "বালু তোলার ড্রেজার", "Dakatia River", "2025", "Leica M11-P • 35mm f/2.0 • 1/640s • ISO 100", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=800&q=80", "Industrial suction pipes pumping construction sand to barge holds."),
                AlbumFrame("rc_19", "19", "Midnight Crossing in Rain", "রাতের মেঘনা পারাপার", "Chandpur Terminal", "2025", "Leica Q3 • 28mm f/1.7 • 1/40s • ISO 3200", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Raindrops streaking across yellow navigation beacon glass."),
                AlbumFrame("rc_20", "20", "River Dolphin Surfacing", "শুশুকের নিঃশ্বাস", "Meghna Sanctuary", "2025", "Leica SL2 • 70-200mm f/2.8 • 1/1600s • ISO 400", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Endangered Ganges river dolphin arching its sleek grey dorsal ridge."),
                AlbumFrame("rc_21", "21", "Char Sunset in Lavender", "চরের লালচে গোধূলি", "Char Fasson", "2025", "Hasselblad 907X • 45mm f/4 • 1/100s • ISO 100", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Sun sinking into five miles of unbroken tidal water."),
                AlbumFrame("rc_22", "22", "The Eternal Flow", "অনন্ত জলধারা", "Lower Delta", "2025", "Leica M11-P • 35mm f/1.4 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Two hundred rivers surrendering harmoniously into the Bay of Bengal.")
            )
        ),
        Project(
            id = "proj_04",
            number = "04",
            title = "AFTER RAIN",
            bengaliTitle = "বৃষ্টির পর",
            subtitle = "Quietude, Wet Silt & Soft Evening Horizon",
            location = "FARIDPUR, BANGLADESH",
            year = "2025",
            photoCount = 20,
            coverPhotoId = "photo_04",
            description = "The fragile stillness that lingers in village paths immediately after hours of torrential downpour.",
            essayText = "The moments immediately following heavy rainfall hold a peculiar tenderness. The air smells intensely of wet humus and crushed grass. Children emerge cautiously into flooded paths, and the sky mirrors itself in thousands of shallow puddles across the courtyard.",
            photoIds = listOf("photo_04", "photo_08", "photo_10", "photo_12"),
            quote = "Light after water is softer than any memory.",
            viewCount = 195,
            dateFormatted = "AUGUST 2025",
            frameList = listOf(
                AlbumFrame("ar_01", "01", "Courtyard Mirror Puddle", "উঠোনের জলে মেঘের ছায়া", "Faridpur", "2025", "Hasselblad 907X • 45mm f/4 • 1/125s • ISO 64", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Rain puddle reflecting betel nut tree silhouettes with glass precision."),
                AlbumFrame("ar_02", "02", "Smell of Wet Earth (Petrichor)", "মাটির সোঁদা গন্ধ", "Madhukhali", "2025", "Leica M11-P • 50mm f/1.4 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "The first deep breath after three continuous hours of cloudburst."),
                AlbumFrame("ar_03", "03", "Dripping Thatched Eaves", "খড়ের চালের জলবিন্দু", "Boalmari", "2025", "Leica M11-P • 75mm f/2.0 • 1/1000s • ISO 200", "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80", "Slow water droplets falling in sync into brass water collection vessels."),
                AlbumFrame("ar_04", "04", "Children with Paper Boats", "কাগজের নাও ভাসানো", "Sadarpur", "2025", "Leica M11-P • 35mm f/2.0 • 1/640s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Racing folded school notebook paper boats down courtyard drains."),
                AlbumFrame("ar_05", "05", "Sunlight Striking Wet Jute", "ভেজা পাটে সোনার রোদ", "Faridpur", "2025", "Leica M11-P • 35mm f/2.8 • 1/800s • ISO 200", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Water droplets glistening like quartz crystals on golden fiber."),
                AlbumFrame("ar_06", "06", "Spider Silk Dew Pearls", "মাকড়সার জালে মুক্তো", "Nagarkanda", "2025", "Leica M11-P • 90mm f/2.8 Macro • 1/320s • ISO 100", "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80", "Orb weaver web weighed down into shimmering geometric suspension."),
                AlbumFrame("ar_07", "07", "The Bamboo Footbridge (Shako)", "ভিজে বাঁশের সাঁকো", "Alfadanga", "2025", "Leica M11-P • 35mm f/2.8 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80", "Slippery wet bamboo poles crossed cautiously with bare feet."),
                AlbumFrame("ar_08", "08", "Egrets in Flooded Paddy Field", "জলে নামা বকের দল", "Bhanga", "2025", "Leica SL2 • 70-200mm f/2.8 • 1/1200s • ISO 200", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Pristine white birds stalking small silver minnows trapped in furrows."),
                AlbumFrame("ar_09", "09", "The Potter Opening Mud Kiln", "ভাটি খোলার ক্ষণ", "Faridpur Kumorpara", "2025", "Leica M11-P • 50mm f/2.0 • 1/250s • ISO 400", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80", "Checking if the baked clay pots survived the sudden dampness."),
                AlbumFrame("ar_10", "10", "Golden Mist over Lotus Pond", "পদ্মপুকুরে সোনার কুয়াশা", "Kanaipur", "2025", "Hasselblad 907X • 45mm f/4 • 1/200s • ISO 100", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Evaporation mist rising as the tropical sun warms the rain-soaked water."),
                AlbumFrame("ar_11", "11", "Woman Sweeping Veranda", "উঠোন ঝাঁট দেওয়ার শব্দ", "Saltha", "2025", "Leica M11-P • 35mm f/2.0 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Sweeping away fallen mango leaves with a traditional coconut-rib broom."),
                AlbumFrame("ar_12", "12", "Frogs Calling from Ditches", "বাদল শেষের ব্যাঙের ডাক", "Char Bhadrasan", "2025", "Leica M11-P • 50mm f/1.4 • 1/160s • ISO 800", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=800&q=80", "Bullfrogs celebrating the arrival of freshwater in village roadside canals."),
                AlbumFrame("ar_13", "13", "Evening Woodsmoke in Damp Air", "ভিজে বাতাসে ধোঁয়ার গন্ধ", "Madhukhali", "2025", "Leica M11-P • 50mm f/1.4 • 1/100s • ISO 640", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Kitchen hearth smoke hanging low under saturated atmospheric pressure."),
                AlbumFrame("ar_14", "14", "Cattle Grazing on Fresh Grass", "সতেজ ঘাসে চরে বেড়ানো", "Boalmari", "2025", "Leica M11-P • 35mm f/2.8 • 1/400s • ISO 100", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Relishing lush green growth revived by afternoon precipitation."),
                AlbumFrame("ar_15", "15", "Rainbow over Faridpur Mosque", "মসজিদের মিনারে রামধনু", "Faridpur Town", "2025", "Leica M11-P • 28mm f/4.0 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "A double rainbow arching over terracotta domed heritage architecture."),
                AlbumFrame("ar_16", "16", "Washing Muddy Feet at Tubewell", "টিউবওয়েলে কাদা ধোয়া", "Nagarkanda", "2025", "Leica M11-P • 50mm f/2.0 • 1/640s • ISO 100", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Pumping cold iron-rich groundwater to rinse off red agricultural silt."),
                AlbumFrame("ar_17", "17", "Reflections in Rickshaw Bell", "রিকশার ঘণ্টির প্রতিবিম্ব", "Faridpur Station Road", "2025", "Leica M11-P • 50mm f/1.4 • 1/320s • ISO 200", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "Polished chrome dome mirroring neon signs and wet asphalt."),
                AlbumFrame("ar_18", "18", "Fireflies in Bamboo Grove", "বাঁশঝাড়ে জোনাকির মেলা", "Sadarpur", "2025", "Leica M11-P • 35mm f/1.4 • 4s Long Exp • ISO 1600", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=800&q=80", "Hundreds of synchronized bio-luminescent flashes in damp darkness."),
                AlbumFrame("ar_19", "19", "Old Woman on Veranda Edge", "বারান্দার কোণে দাদী", "Alfadanga", "2025", "Leica M11-P • 50mm f/1.4 • 1/200s • ISO 320", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "Watching the sky clear with a lifetime of serene patience."),
                AlbumFrame("ar_20", "20", "The Night Air at Peace", "বৃষ্টিভেজা রাতের প্রশান্তি", "Faridpur", "2025", "Leica M11-P • 35mm f/1.4 • 1/30s • ISO 1250", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Cool night breeze carrying the scent of night-blooming Shiuli jasmine.")
            )
        ),
        Project(
            id = "proj_05",
            number = "05",
            title = "OLD TOWN SHADOWS",
            bengaliTitle = "পুরান ঢাকা",
            subtitle = "Three Centuries of Indigo Twilight and Tungsten Glow",
            location = "OLD DHAKA, BANGLADESH",
            year = "2026",
            photoCount = 28,
            coverPhotoId = "photo_03",
            description = "A nocturnal study of Old Dhaka's historic alleys, traditional artisans, and timeless communal architecture.",
            essayText = "Within the tightly woven maze of Old Dhaka, time functions non-linearly. Mughal gateways stand beside colonial brickwork and buzzing neon signs. In these narrow fissures, generations of craftsmen continue their ancestral callings with quiet dignity.",
            photoIds = listOf("photo_03", "photo_11", "photo_14", "photo_06"),
            quote = "History here is not carved in marble, but worn into the stones by barefoot steps.",
            viewCount = 420,
            dateFormatted = "JANUARY 2026",
            frameList = listOf(
                AlbumFrame("ots_01", "01", "Tungsten Glow of Shankhari Bazar", "শাঁখারীবাজারের বাতি", "Old Dhaka", "2026", "Leica Q3 • 28mm f/1.7 • 1/125s • ISO 1600", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Three-hundred-year-old artisan alley lit by bare incandescent bulbs."),
                AlbumFrame("ots_02", "02", "Conch Shell Artisan at Work", "শাঁখার কারিগর", "Shankhari Bazar", "2026", "Leica M11-P • 50mm f/1.4 • 1/250s • ISO 800", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Carving fine auspicious bangles with traditional crescent hand-saws."),
                AlbumFrame("ots_03", "03", "Cobblestones under Midnight Fog", "কুয়াশাঘেরা পাথুরে গলি", "Armanitola", "2026", "Leica Q3 • 28mm f/1.8 • 1/80s • ISO 2500", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "Solitary cyclist gliding through yellow streetlamp glow."),
                AlbumFrame("ots_04", "04", "Ruqayya Mansion Iron Balconies", "লোহার নকশা ও বারান্দা", "Farashganj", "2026", "Hasselblad 907X • 45mm f/4 • 1/60s • ISO 400", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=800&q=80", "Intricate Victorian cast iron brackets imported from Glasgow in 1890."),
                AlbumFrame("ots_05", "05", "Bakarkhani Baker's Tandoor", "বাকরখানির তন্দুর", "Nazira Bazar", "2026", "Leica M11-P • 35mm f/1.4 • 1/200s • ISO 1250", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Slapping layered ghee dough onto clay oven walls at 4 AM."),
                AlbumFrame("ots_06", "06", "Ahsan Manzil Twilight View", "গোলাপী প্রাসাদের গোধূলি", "Kumartoli", "2026", "Leica M11-P • 35mm f/2.8 • 1/60s • ISO 200", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Pink palace dome silhouetted against the lavender Buriganga sky."),
                AlbumFrame("ots_07", "07", "Kite Maker of Chawkbazar", "ঘুড়ির কারিগর", "Chawkbazar", "2026", "Leica M11-P • 50mm f/1.4 • 1/400s • ISO 400", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "Gluing gossamer tissue paper to shaved bamboo splints for Shakrain."),
                AlbumFrame("ots_08", "08", "Spices of Maulvibazar", "মৌলভীবাজারের মশলার ঘ্রাণ", "Maulvibazar", "2026", "Leica M11-P • 50mm f/2.0 • 1/500s • ISO 320", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Sacks of turmeric, cardamom and star anise ground fresh daily."),
                AlbumFrame("ots_09", "09", "Tara Masjid (Star Mosque) Chini-Tikri", "তারা মসজিদের চিনিটিকরি", "Armanitola", "2026", "Hasselblad 907X • 28mm f/5.6 • 1/125s • ISO 100", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Broken ceramic porcelain mosaic stars reflecting interior chandeliers."),
                AlbumFrame("ots_10", "10", "Attar Perfumer of Mitford", "আতর বিক্রেতার শিশি", "Mitford Road", "2026", "Leica M11-P • 75mm f/2.0 • 1/320s • ISO 200", "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80", "Distilling pure rosewater, musk, and aged Assam agarwood oil."),
                AlbumFrame("ots_11", "11", "Rickshaw Hood Painter", "রিকশা আর্টের রঙতুলি", "Bangshal", "2026", "Leica M11-P • 50mm f/1.4 • 1/640s • ISO 200", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80", "Painting vibrant film heroines and taj mahal landscapes on tin backplates."),
                AlbumFrame("ots_12", "12", "Ruins of Ruplal House", "রূপলাল হাউসের ইতিহাস", "Buckland Bund", "2026", "Leica M11-P • 35mm f/2.8 • 1/125s • ISO 100", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=800&q=80", "Corinthian brick columns overlooking the historic riverfront embankment."),
                AlbumFrame("ots_13", "13", "Mustard Oil Press (Ghani)", "ঘানির খাঁটি সরিষার তেল", "Rahmatganj", "2026", "Leica M11-P • 50mm f/2.0 • 1/200s • ISO 800", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=800&q=80", "Cold-pressing black mustard seeds with ancient hardwood mortars."),
                AlbumFrame("ots_14", "14", "Shakrain Fire Breathing on Rooftop", "সাকরাইনের আগুনখেলা", "Tantibazar Roof", "2026", "Leica Q3 • 28mm f/1.7 • 1/500s • ISO 1600", "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80", "Blowing kerosene flame into the night sky to mark the solstice."),
                AlbumFrame("ots_15", "15", "Copper Utensil Hammerer", "তামার হাঁড়ির নকশা", "Kansaripatti", "2026", "Leica M11-P • 50mm f/1.4 • 1/400s • ISO 400", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Rhythmic beating of bronze caldrons echoing down narrow arcades."),
                AlbumFrame("ots_16", "16", "Armenian Church Graveyard", "আর্মেনিয়ান চার্চের সমাধি", "Armanitola", "2026", "Leica M11-P • 35mm f/2.0 • 1/250s • ISO 100", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Marble epitaphs dating from 1781 in the shade of ancient banyan trees."),
                AlbumFrame("ots_17", "17", "Old Dhaka Roof Pigeons", "ছাদের কবুতর ওড়ানো", "Islampur Roofs", "2026", "Leica M11-P • 50mm f/1.4 • 1/1600s • ISO 100", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80", "Fanciers whistling to direct high-flying tumbler pigeons."),
                AlbumFrame("ots_18", "18", "Tazia Procession Muharram", "তাজিয়া মিছিলের শোকগাথা", "Hussaini Dalan", "2026", "Leica M11-P • 35mm f/1.4 • 1/320s • ISO 640", "https://images.unsplash.com/photo-1534088568595-a066f410bcda?auto=format&fit=crop&w=800&q=80", "Reverent black banners carried past the 1642 Shi'ite shrine."),
                AlbumFrame("ots_19", "19", "Lalbagh Fort at Blue Hour", "লালবাগ কেল্লার সন্ধ্যা", "Lalbagh", "2026", "Hasselblad 907X • 45mm f/4 • 1/30s • ISO 100", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Mughal Pari Bibi tomb dome reflected in the placid garden reservoir."),
                AlbumFrame("ots_20", "20", "Paan Shop with Betel Leaves", "মিষ্টি পানের খিলি", "Bangla Bazar", "2026", "Leica M11-P • 50mm f/2.0 • 1/250s • ISO 200", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=800&q=80", "Folding sweet betel quid with silver foil (vark) and rose petals."),
                AlbumFrame("ots_21", "21", "Kashmiri Shawl Mender (Rafoogar)", "রাফুগারের সূক্ষ্ম কাজ", "Islampur", "2026", "Leica M11-P • 75mm f/2.4 • 1/200s • ISO 400", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80", "Invisible darning of antique pashmina wool with silk filaments."),
                AlbumFrame("ots_22", "22", "Old Bookbinder's Workshop", "বাংলাবাজারের বাঁধাইখানা", "Bangla Bazar", "2026", "Leica M11-P • 50mm f/2.0 • 1/200s • ISO 200", "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?auto=format&fit=crop&w=800&q=80", "Hand-stitching leather spine bindings with waxed linen thread."),
                AlbumFrame("ots_23", "23", "Winter Morning Mustard Oil Bath", "শীতের সকালে সর্ষের তেল", "Kumartoli Ghat", "2026", "Leica M11-P • 35mm f/2.0 • 1/500s • ISO 100", "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=800&q=80", "Warming the body with cold-pressed mustard oil before river ablutions."),
                AlbumFrame("ots_24", "24", "Peeling Plaster & History", "স্মৃতির চুনকাম", "Farashganj", "2026", "Leica M11-P • 50mm f/2.8 • 1/320s • ISO 100", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=800&q=80", "Generations of lime wash revealing terracotta brick beneath."),
                AlbumFrame("ots_25", "25", "Evening Prayer in Ancient Mosque", "প্রাচীন মসজিদে মাগরিবের নামাজ", "Chawk Mosque", "2026", "Leica M11-P • 35mm f/1.4 • 1/100s • ISO 800", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80", "Reverent devotion under 17th-century vaulted brick arches."),
                AlbumFrame("ots_26", "26", "Buriganga Night Barges", "বুড়িগঙ্গার রাতের নৌযান", "Sadarghat", "2026", "Hasselblad 503CW • 80mm f/2.8 • 1/15s • ISO 1600", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80", "Green and red navigation lanterns reflected in the dark river waters."),
                AlbumFrame("ots_27", "27", "Morning Fog over Chawk", "চকের ভোরের কুয়াশা", "Chawk Bazar", "2026", "Leica M11-P • 35mm f/2.0 • 1/320s • ISO 200", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "Silhouettes of porters and pushcarts cutting through thick winter mist."),
                AlbumFrame("ots_28", "28", "The Soul of Dhaka", "ঢাকার প্রাণ", "Old Dhaka", "2026", "Leica M11-P • 35mm f/1.4 • 1/250s • ISO 400", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80", "An unbroken human spirit enduring gracefully across three centuries.")
            )
        )
    )

    private val defaultJournalEntries: List<JournalEntry> = listOf(
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

    private val defaultExhibitions: List<Exhibition> = listOf(
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

    // Dynamic mutable runtime collections
    private val _photographs = defaultPhotographs.toMutableList()
    private val _projects = defaultProjects.toMutableList()
    private val _journalEntries = defaultJournalEntries.toMutableList()
    private val _exhibitions = defaultExhibitions.toMutableList()

    val photographs: List<Photograph>
        get() = synchronized(this) { _photographs.toList() }

    val projects: List<Project>
        get() = synchronized(this) { _projects.toList() }

    val journalEntries: List<JournalEntry>
        get() = synchronized(this) { _journalEntries.toList() }

    val exhibitions: List<Exhibition>
        get() = synchronized(this) { _exhibitions.toList() }

    // CRUD for Photographs
    fun addPhotograph(photo: Photograph) = synchronized(this) {
        _photographs.add(0, photo)
    }

    fun updatePhotograph(photo: Photograph) = synchronized(this) {
        val index = _photographs.indexOfFirst { it.id == photo.id }
        if (index != -1) {
            _photographs[index] = photo
        } else {
            _photographs.add(0, photo)
        }
    }

    fun deletePhotograph(id: String) = synchronized(this) {
        _photographs.removeAll { it.id == id }
    }

    // CRUD for Projects
    fun addProject(project: Project) = synchronized(this) {
        _projects.add(project)
    }

    fun updateProject(project: Project) = synchronized(this) {
        val index = _projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            _projects[index] = project
        } else {
            _projects.add(project)
        }
    }

    fun deleteProject(id: String) = synchronized(this) {
        _projects.removeAll { it.id == id }
    }

    // CRUD for Journal Entries
    fun addJournalEntry(journal: JournalEntry) = synchronized(this) {
        _journalEntries.add(0, journal)
    }

    fun updateJournalEntry(journal: JournalEntry) = synchronized(this) {
        val index = _journalEntries.indexOfFirst { it.id == journal.id }
        if (index != -1) {
            _journalEntries[index] = journal
        } else {
            _journalEntries.add(0, journal)
        }
    }

    fun deleteJournalEntry(id: String) = synchronized(this) {
        _journalEntries.removeAll { it.id == id }
    }

    // CRUD for Exhibitions
    fun addExhibition(exhibition: Exhibition) = synchronized(this) {
        _exhibitions.add(0, exhibition)
    }

    fun updateExhibition(index: Int, exhibition: Exhibition) = synchronized(this) {
        if (index in _exhibitions.indices) {
            _exhibitions[index] = exhibition
        }
    }

    fun deleteExhibition(index: Int) = synchronized(this) {
        if (index in _exhibitions.indices) {
            _exhibitions.removeAt(index)
        }
    }

    // Reset to defaults
    fun resetToDefaults() = synchronized(this) {
        _photographs.clear()
        _photographs.addAll(defaultPhotographs)
        _projects.clear()
        _projects.addAll(defaultProjects)
        _journalEntries.clear()
        _journalEntries.addAll(defaultJournalEntries)
        _exhibitions.clear()
        _exhibitions.addAll(defaultExhibitions)
    }

    // Curated high-res presets for 1-click admin addition
    val photoPresets = listOf(
        Pair("Meghna River Mist", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1600&q=85"),
        Pair("Monsoon Flood Path", "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?auto=format&fit=crop&w=1600&q=85"),
        Pair("Old Town Lanterns", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1600&q=85"),
        Pair("Tea Estate Fog", "https://images.unsplash.com/photo-1518495973542-4542c06a5843?auto=format&fit=crop&w=1600&q=85"),
        Pair("Riverman Portrait", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=1600&q=85"),
        Pair("Chalan Beel Sunset", "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1600&q=85"),
        Pair("Jute Field Harvest", "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1600&q=85"),
        Pair("Sundarbans Mangrove", "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=1600&q=85"),
        Pair("Sadarghat Night Steamers", "https://images.unsplash.com/photo-1514565131-fce0801e5785?auto=format&fit=crop&w=1600&q=85"),
        Pair("Coastal Trees Horizon", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=1600&q=85")
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
