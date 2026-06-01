package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage

class HotelsTest : BaseTest() {

    @Test
    fun hotelBooking_redirectsToPartnerSite() {
        val hotels = HomePage(driver)
            .open()
            .openHotelsTab()
        assertTrue(hotels.isLoaded())

        val results = hotels
            .setCity("Санкт-Петербург")
            .selectDates()
            .search()
        assertTrue(results.isResultsVisible())

        val details = results.openFirstHotel()
        assertTrue(details.isLoaded())

        details.bookFirstAvailableRoom()
        assertTrue(details.isRedirectedToPartnerSite())
    }
}
