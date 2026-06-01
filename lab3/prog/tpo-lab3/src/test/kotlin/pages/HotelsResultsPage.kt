package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class HotelsResultsPage(
    driver: WebDriver
) : BasePage(driver) {

    private val hotelPreview = By.xpath("//*[@data-test-id='hotel-preview']")
    private val sortDropdown = By.xpath("//*[@data-test-id='dynamic-filter-instance-sort_select']")
    private val sortMenu = By.xpath("//*[@id='downshift-0-menu']")
    private val sortPriceAsc = By.xpath("//*[@data-test-id='select-option-sort_price_asc']")
    private val freeCancellationFilter = By.xpath("//*[@data-test-id='set-filter-row-free_cancellation']")
    private val starsGroup = By.xpath("//*[@data-test-id='filter-group-hotel_stars_group']")
    private val starsSet = By.xpath("//*[@data-test-id='dynamic-filter-instance-hotel_stars_set']")
    private val fiveStarsFilter = By.xpath("//*[@data-test-id='set-filter-row-5']")
    private val loader = By.xpath("//svg[contains(@class,'s__zTEXtG5Y2bwPNtcx')]")

    fun openFirstHotel(): HotelDetailsPage {
        val currentWindow = driver.windowHandle
        val firstHotel = wait.until { visibleHotels().firstOrNull() }
            ?: error("No visible hotel previews found")
        scrollTo(firstHotel)
        jsClick(firstHotel)
        wait.until { driver.windowHandles.size > 1 }
        driver.switchTo().window(driver.windowHandles.first { it != currentWindow })
        return HotelDetailsPage(driver)
    }

    fun sortByPriceAscending(): HotelsResultsPage {
        click(sortDropdown)
        visible(sortMenu)
        click(sortPriceAsc)
        waitFor { driver.findElements(loader).isEmpty() }
        return this
    }

    fun applyFreeCancellationFilter(): HotelsResultsPage {
        click(freeCancellationFilter)
        waitFor { visibleHotels().isNotEmpty() }
        return this
    }

    fun applyFiveStarsFilter(): HotelsResultsPage {
        click(starsGroup)
        waitFor { driver.findElements(starsSet).isNotEmpty() }
        click(fiveStarsFilter)
        waitFor { visibleHotels().isNotEmpty() }
        return this
    }

    fun isResultsVisible(): Boolean = waitFor(seconds = 60) { visibleHotels().isNotEmpty() }

    fun areHotelPricesSortedAscending(): Boolean = waitFor {
        val prices = extractHotelPrices()
        prices.size >= 2 && prices.zipWithNext().all { (a, b) -> a <= b }
    }

    fun allHotelsHaveFreeCancellation(): Boolean = waitFor {
        val hotels = visibleHotels()
        hotels.isNotEmpty() && hotels.all { hotelText(it).contains("Бесплатная отмена") }
    }

    fun allHotelsHaveFiveStars(): Boolean = waitFor {
        val hotels = visibleHotels()
        hotels.isNotEmpty() && hotels.all { hotel ->
            runCatching {
                hotel.findElements(By.xpath(".//*[@data-test-id='text' and text()='5']"))
                    .any { it.isDisplayed }
            }.getOrDefault(false)
        }
    }

    private fun visibleHotels(): List<WebElement> =
        driver.findElements(hotelPreview).filter { runCatching { it.isDisplayed }.getOrDefault(false) }

    private fun hotelText(hotel: WebElement): String =
        runCatching { hotel.text }.getOrDefault("")

    private fun extractHotelPrices(): List<Int> =
        visibleHotels().mapNotNull { hotel ->
            Regex("\\d[\\d\\s\\u202F]*\\s*₽")
                .find(hotelText(hotel))
                ?.value
                ?.filter { it.isDigit() }
                ?.toIntOrNull()
        }
}
