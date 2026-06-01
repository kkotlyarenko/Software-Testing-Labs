package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage

class FlightSearchTest : BaseTest() {

    @Test
    fun buyOneWayTicket_redirectsToAirlineSite() {
        val results = HomePage(driver)
            .open()
            .setOrigin("Санкт-Петербург")
            .setDestination("Москва")
            .selectDepartureDate(14)
            .searchOneWay()

        assertTrue(results.isResultsVisible())

        results.clickFirstTicket()
        assertTrue(results.isAirlineSiteOpened())
    }

    @Test
    fun swapDirections_exchangesOriginAndDestination() {
        val home = HomePage(driver)
            .open()
            .setOrigin("Санкт-Петербург")
            .setDestination("Москва")
            .swapDirections()

        assertTrue(home.hasOriginValue("Москв"))
        assertTrue(home.hasDestinationValue("Санкт"))
    }

    @Test
    fun roundTrip_showsTicketsWithReturnLegAndOpensAirlineSite() {
        val results = HomePage(driver)
            .open()
            .setOrigin("Санкт-Петербург")
            .setDestination("Москва")
            .selectRoundTripDates(14, 21)
            .searchOneWay()

        assertTrue(results.isResultsVisible())
        assertTrue(results.areRoundTripTicketsVisible())

        results.clickFirstTicket()
        assertTrue(results.isAirlineSiteOpened())
    }
}
