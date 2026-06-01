package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions

class SearchResultsPage(
    driver: WebDriver
) : BasePage(driver) {

    private val ticketPreview = By.xpath("//*[@data-test-id='ticket-preview']")
    private val ticketBadge = By.xpath("//*[starts-with(@data-test-id,'ticket-preview-badge')]")
    private val baggageFilter = By.xpath("//*[@data-test-id='boolean-filter-baggage']")
    private val oneTransferFlightFilter = By.xpath("//*[@data-test-id='set-filter-row-1']")
    private val priceFilterGroup = By.xpath("//*[@data-test-id='filter-group-price_side_group']")
    private val priceSlider = By.xpath("//*[@data-test-id='dynamic-filter-instance-price']//*[@role='slider']")
    private val ticketPrice = By.xpath(".//*[@data-test-id='price']")
    private val buyButton =
        By.xpath("//*[starts-with(@data-test-id,'proposal-') and contains(@data-test-id,'-button')]")

    fun applyBaggageFilter(): SearchResultsPage {
        click(baggageFilter)
        waitFor { ticketsVisible() }
        return this
    }

    fun applyOneTransferFlightFilter(): SearchResultsPage {
        click(oneTransferFlightFilter)
        waitFor { ticketsVisible() }
        return this
    }

    fun applyMaxPriceFilter(): SearchResultsPage {
        scrollTo(priceFilterGroup)
        click(priceFilterGroup)
        val slider = clickable(priceSlider)
        Actions(driver).clickAndHold(slider).moveByOffset(-205, 0).release().perform()
        return this
    }

    fun clickFirstTicket(): SearchResultsPage {
        click(ticketBadge)
        wait.until {
            runCatching {
                val btn = driver.findElements(buyButton).firstOrNull { it.isDisplayed } ?: return@runCatching false
                jsClick(btn)
                true
            }.getOrDefault(false)
        }
        return this
    }

    fun isResultsVisible(): Boolean = waitFor { ticketsVisible() }

    fun allTicketsHaveBaggage(): Boolean = waitFor {
        val tickets = driver.findElements(ticketPreview)
        tickets.isNotEmpty() && tickets.all { it.text.contains("багаж", ignoreCase = true) }
    }

    fun allTicketsHaveTransfers(): Boolean = waitFor {
        val tickets = driver.findElements(ticketPreview)
        tickets.isNotEmpty() && tickets.all { it.text.contains("пересад", ignoreCase = true) }
    }

    fun allTicketsWithinMaxPrice(): Boolean {
        val maxPrice = currentMaxPrice()
        return waitFor {
            val prices = extractTicketPrices()
            prices.isNotEmpty() && prices.all { it <= maxPrice }
        }
    }

    fun areRoundTripTicketsVisible(): Boolean = waitFor {
        val firstTicket = driver.findElements(ticketPreview).firstOrNull()
        firstTicket != null && Regex("в пути").findAll(firstTicket.text).count() >= 2
    }

    fun isAirlineSiteOpened(): Boolean {
        val urlBefore = driver.currentUrl
        val opened = waitFor { driver.windowHandles.size > 1 || driver.currentUrl != urlBefore }
        if (opened && driver.windowHandles.size > 1) {
            driver.switchTo().window(driver.windowHandles.last())
        }
        return opened
    }

    private fun ticketsVisible(): Boolean = driver.findElements(ticketPreview).any { it.isDisplayed }

    private fun currentMaxPrice(): Int = driver.findElement(priceSlider).getAttribute("aria-valuenow").toInt()

    private fun extractTicketPrices(): List<Int> =
        driver.findElements(ticketPreview).filter { it.isDisplayed }.mapNotNull { ticket ->
            ticket.findElement(ticketPrice).text.replace(Regex("[^\\d]"), "").toIntOrNull()
        }
}
