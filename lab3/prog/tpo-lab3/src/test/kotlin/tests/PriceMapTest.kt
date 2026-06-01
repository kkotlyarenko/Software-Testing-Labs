package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage

class PriceMapTest : BaseTest() {

    @Test
    fun priceMap_sortsCityCardsByPriceAscending() {
        val priceMap = HomePage(driver)
            .open()
            .openPriceMap()
        assertTrue(priceMap.isVisible())

        priceMap.sortByPrice()
        assertTrue(priceMap.arePricesSortedAscending())
    }
}
