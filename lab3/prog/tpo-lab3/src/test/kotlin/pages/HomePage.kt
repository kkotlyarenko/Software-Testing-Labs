package pages

import config.TestConfig
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomePage(
    driver: WebDriver
) : BasePage(driver) {

    private val originInput = By.xpath("//*[@data-test-id='origin-input']")
    private val destinationInput = By.xpath("//*[@data-test-id='destination-input']")
    private val departureDateButton = By.xpath("//*[@data-test-id='start-date-field']")
    private val returnDateButton = By.xpath("//*[@data-test-id='end-date-field']")
    private val searchButton = By.xpath("//*[@data-test-id='form-submit']")
    private val swapBtn = By.xpath("//form[@data-test-id='avia-form']//*[@data-test-id='round-button']")
    private val autocompleteOption = By.xpath("//*[@role='option']")

    private val complexRouteBtn = By.xpath("//*[@data-test-id='switch-to-multiwayform']")
    private val priceMapBtn = By.xpath("//*[@data-test-id='price-map-banner-button']")
    private val journalButton = By.xpath("//*[@data-test-id='header-blog-button']")
    private val anyDateCell = By.xpath("//*[starts-with(@data-test-id,'date-')]")
    private val calendarActionButton = By.xpath("//*[@data-test-id='calendar-action-button']")

    fun open(): HomePage {
        driver.get(TestConfig.baseUrl)
        visible(originInput)
        return this
    }

    fun hasOriginValue(expected: String): Boolean = hasValue(originInput, expected)

    fun hasDestinationValue(expected: String): Boolean = hasValue(destinationInput, expected)

    fun setOrigin(city: String): HomePage {
        selectCity(originInput, city)
        return this
    }

    fun setDestination(city: String): HomePage {
        selectCity(destinationInput, city)
        return this
    }

    fun selectDepartureDate(daysFromNow: Long = 14): HomePage {
        openCalendar(departureDateButton)
        pickDay(daysFromNow)
        confirmCalendar()
        return this
    }

    fun selectRoundTripDates(departureDaysFromNow: Long = 14, returnDaysFromNow: Long = 21): HomePage {
        openCalendar(departureDateButton)
        pickDay(departureDaysFromNow)
        jsClick(returnDateButton)
        pickDay(returnDaysFromNow)
        confirmCalendar()
        return this
    }

    fun swapDirections(): HomePage {
        val originBefore = driver.findElement(originInput).getAttribute("value") ?: ""
        jsClick(swapBtn)
        wait.until {
            runCatching {
                val current = driver.findElement(originInput).getAttribute("value") ?: ""
                current.isNotEmpty() && current != originBefore
            }.getOrDefault(false)
        }
        return this
    }

    fun searchOneWay(): SearchResultsPage {
        val originalWindow = driver.windowHandles.first()
        val urlBefore = driver.currentUrl
        jsClick(searchButton)
        wait.until {
            driver.windowHandles.size > 1 || driver.currentUrl != urlBefore
        }
        if (driver.windowHandles.size > 1) {
            val newWindow = driver.windowHandles.first { it != originalWindow }
            driver.switchTo().window(newWindow)
        }
        return SearchResultsPage(driver)
    }

    fun openHotelsTab(): HotelsPage {
        driver.navigate().to("${TestConfig.baseUrl}hotels")
        return HotelsPage(driver)
    }

    fun openComplexRouteForm(): ComplexRoutePage {
        click(complexRouteBtn)
        return ComplexRoutePage(driver)
    }

    fun openPriceMap(): PriceMapPage {
        jsClick(priceMapBtn)
        return PriceMapPage(driver)
    }

    fun openJournal(): JournalPage {
        val currentWindow = driver.windowHandle
        click(journalButton)
        wait.until { driver.windowHandles.size > 1 }
        driver.switchTo().window(driver.windowHandles.first { it != currentWindow })
        return JournalPage(driver)
    }

    private fun selectCity(input: By, city: String) {
        val field = clickable(input)
        field.click()
        field.sendKeys(Keys.CONTROL, "a")
        field.sendKeys(Keys.DELETE)
        field.sendKeys(city)
        runCatching {
            WebDriverWait(driver, Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(100))
                .until {
                    driver.findElements(autocompleteOption)
                        .firstOrNull { it.isDisplayed && it.text.contains(city, ignoreCase = true) }
                        ?.let { it.click(); true } ?: false
                }
        }
    }

    private fun hasValue(by: By, expected: String): Boolean = waitFor {
        driver.findElement(by).getAttribute("value")
            ?.contains(expected, ignoreCase = true) == true
    }

    private fun openCalendar(button: By) {
        wait.until {
            runCatching {
                click(button)
                driver.findElements(anyDateCell).any { it.isDisplayed }
            }.getOrDefault(false)
        }
    }

    private fun pickDay(daysFromNow: Long) {
        val target = LocalDate.now().plusDays(daysFromNow)
        selectMonthIfPresent(target)
        val dateId = "date-${target.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
        val dayButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@data-test-id='$dateId']/ancestor::button[1]")
            )
        )
        jsClick(dayButton)
    }

    private fun selectMonthIfPresent(target: LocalDate) {
        driver.findElements(By.xpath("//*[@data-test-id='select-month']"))
            .firstOrNull()
            ?.let { runCatching { Select(it).selectByValue(target.format(DateTimeFormatter.ofPattern("yyyy-MM"))) } }
    }

    private fun confirmCalendar() {
        runCatching {
            WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(calendarActionButton))
                .click()
        }
    }
}
