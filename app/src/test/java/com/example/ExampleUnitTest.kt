package com.example

import com.example.ui.viewmodel.PortfolioViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun photoSearch_filtersByTitleAndLocation() {
        val viewModel = PortfolioViewModel()

        // Search by location
        viewModel.setSearchQuery("Meghna")
        val locationResults = viewModel.getFilteredPhotos()
        assertTrue(locationResults.isNotEmpty())
        assertTrue(locationResults.all {
            it.location.contains("Meghna", ignoreCase = true) ||
            it.title.contains("Meghna", ignoreCase = true) ||
            it.caption.contains("Meghna", ignoreCase = true)
        })

        // Search by Old Dhaka
        viewModel.setSearchQuery("Old Dhaka")
        val dhakaResults = viewModel.getFilteredPhotos()
        assertTrue(dhakaResults.isNotEmpty())
        assertTrue(dhakaResults.all {
            it.location.contains("Old Dhaka", ignoreCase = true) ||
            it.title.contains("Dhaka", ignoreCase = true) ||
            it.caption.contains("Dhaka", ignoreCase = true)
        })
    }

    @Test
    fun photoSearch_filtersByExifMetadata() {
        val viewModel = PortfolioViewModel()

        // Search by camera brand
        viewModel.setSearchQuery("Leica")
        val leicaResults = viewModel.getFilteredPhotos()
        assertTrue(leicaResults.isNotEmpty())
        assertTrue(leicaResults.all {
            it.exif.camera.contains("Leica", ignoreCase = true) ||
            it.title.contains("Leica", ignoreCase = true) ||
            it.exif.lens.contains("Leica", ignoreCase = true)
        })

        // Search by focal length / lens
        viewModel.setSearchQuery("35mm")
        val focalResults = viewModel.getFilteredPhotos()
        assertTrue(focalResults.isNotEmpty())
        assertTrue(focalResults.all {
            it.exif.focalLength.contains("35mm", ignoreCase = true) ||
            it.exif.lens.contains("35mm", ignoreCase = true)
        })
    }
}
