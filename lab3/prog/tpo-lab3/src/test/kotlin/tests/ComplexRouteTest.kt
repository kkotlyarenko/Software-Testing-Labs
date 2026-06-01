package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage

class ComplexRouteTest : BaseTest() {

    @Test
    fun complexRoute_buildsTwoSegmentsAndOpensAirlineSite() {
        val form = HomePage(driver)
            .open()
            .openComplexRouteForm()

        assertTrue(form.isLoaded())
        assertTrue(form.hasDirection(1))
        assertTrue(form.hasDirection(2))

        val results = form
            .setSegment(0, "Санкт-Петербург", "Москва", 14)
            .setSegment(1, "Москва", "Сочи", 21)
            .submitSearch()

        assertTrue(results.isResultsVisible())

        results.clickFirstTicket()
        assertTrue(results.isAirlineSiteOpened())
    }
}
