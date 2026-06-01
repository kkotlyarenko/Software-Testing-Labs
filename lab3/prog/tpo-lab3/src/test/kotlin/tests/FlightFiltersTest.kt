package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage
import pages.SearchResultsPage

class FlightFiltersTest : BaseTest() {

    private fun openResults(): SearchResultsPage {
        val results = HomePage(driver)
            .open()
            .setOrigin("Санкт-Петербург")
            .setDestination("Москва")
            .selectDepartureDate(14)
            .searchOneWay()
        assertTrue(results.isResultsVisible())
        return results
    }

    @Test
    fun baggageFilter_marksAllTicketsWithBaggageLabel() {
        val results = openResults().applyBaggageFilter()
        assertTrue(results.allTicketsHaveBaggage())
    }

    @Test
    fun oneTransferFlightsFilter_hidesTicketsWithTransfers() {
        val results = openResults().applyOneTransferFlightFilter()
        assertTrue(results.allTicketsHaveTransfers())
    }

    @Test
    fun priceFilter_keepsTicketsWithinMaxPrice() {
        val results = openResults().applyMaxPriceFilter()
        assertTrue(results.allTicketsWithinMaxPrice())
    }
}
