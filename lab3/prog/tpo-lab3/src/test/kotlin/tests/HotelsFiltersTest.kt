package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage
import pages.HotelsResultsPage

class HotelsFiltersTest : BaseTest() {

    private fun openResults(): HotelsResultsPage {
        val hotels = HomePage(driver)
            .open()
            .openHotelsTab()
        assertTrue(hotels.isLoaded())

        val results = hotels
            .setCity("Санкт-Петербург")
            .selectDates()
            .search()
        assertTrue(results.isResultsVisible())
        return results
    }

    @Test
    fun priceSort_ordersHotelsByAscendingPrice() {
        val results = openResults().sortByPriceAscending()
        assertTrue(results.areHotelPricesSortedAscending())
    }

    @Test
    fun freeCancellationFilter_keepsOnlyRefundableHotels() {
        val results = openResults().applyFreeCancellationFilter()
        assertTrue(results.isResultsVisible())
        assertTrue(results.allHotelsHaveFreeCancellation())
    }

    @Test
    fun fiveStarsFilter_keepsOnlyFiveStarHotels() {
        val results = openResults().applyFiveStarsFilter()
        assertTrue(results.isResultsVisible())
        assertTrue(results.allHotelsHaveFiveStars())
    }
}
