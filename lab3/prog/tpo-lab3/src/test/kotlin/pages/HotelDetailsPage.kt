package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class HotelDetailsPage(
    driver: WebDriver
) : BasePage(driver) {

    private val firstRoomTariff = By.xpath("(//*[@data-test-id='room-tariff'])[1]")
    private val roomTariffs = By.xpath("//*[@data-test-id='room-tariff']")

    fun bookFirstAvailableRoom(): HotelDetailsPage {
        val windowsBefore = driver.windowHandles.toSet()
        scrollTo(firstRoomTariff)
        click(firstRoomTariff)
        wait.until { driver.windowHandles.size > windowsBefore.size }
        driver.switchTo().window(driver.windowHandles.first { it !in windowsBefore })
        return this
    }

    fun isLoaded(): Boolean = waitFor { driver.findElements(roomTariffs).isNotEmpty() }

    fun isRedirectedToPartnerSite(): Boolean = waitFor {
        !driver.currentUrl.contains("aviasales.ru", ignoreCase = true) &&
            !driver.currentUrl.contains("/hotels/", ignoreCase = true)
    }
}
