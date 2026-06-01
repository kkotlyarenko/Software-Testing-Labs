package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class PriceMapPage(
    driver: WebDriver
) : BasePage(driver) {

    private val citiesCollection = By.xpath("//*[@data-test-id='price-map-v2-cities-collection']")
    private val cityCard = By.xpath(".//*[@data-test-id='city-card']")
    private val sortChip = By.xpath("//*[@data-test-id='price-map-v2-sort-chip-button']")
    private val sortByPriceOption = By.xpath("//*[@data-test-id='price']")

    fun sortByPrice(): PriceMapPage {
        click(sortChip)
        click(sortByPriceOption)
        return this
    }

    fun isVisible(): Boolean = waitFor {
        driver.findElements(cityCard).any { it.isDisplayed }
    }

    fun arePricesSortedAscending(): Boolean = waitFor {
        val collections = driver.findElements(citiesCollection)
        collections.isNotEmpty() && collections.all { collection ->
            val prices = extractPricesFromCollection(collection)
            prices.size < 2 || prices.zipWithNext().all { (a, b) -> a <= b }
        }
    }

    private fun extractPricesFromCollection(collection: WebElement): List<Int> =
        collection.findElements(cityCard)
            .mapNotNull { card ->
                card.text
                    .lineSequence()
                    .firstOrNull()
                    ?.replace(Regex("[^0-9]"), "")
                    ?.toIntOrNull()
            }
}
