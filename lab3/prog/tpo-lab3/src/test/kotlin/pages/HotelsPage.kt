package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HotelsPage(
    driver: WebDriver
) : BasePage(driver) {

    private val hotelForm = By.xpath("//*[@data-test-id='hotel-form']")
    private val cityInput = By.xpath("//*[@data-test-id='hotel-autocomplete-input']")
    private val autocompleteOption = By.xpath("//*[@role='option']")
    private val checkInField = By.xpath("//*[@data-test-id='start-date-field']")
    private val searchButton = By.xpath("//*[@data-test-id='form-submit']")
    private val popupClose = By.xpath("//*[@data-test-id='hotels-informer-modal-close-button']")
    private val anyDateCell = By.xpath("//*[starts-with(@data-test-id,'date-')]")

    fun isLoaded(): Boolean {
        val loaded = waitFor {
            driver.findElements(hotelForm).any { it.isDisplayed } ||
                driver.findElements(cityInput).any { it.isDisplayed }
        }
        dismissPopup()
        return loaded
    }

    fun setCity(city: String): HotelsPage {
        val field = clickable(cityInput)
        field.clear()
        field.sendKeys(city)
        val cityOption = wait.until {
            driver.findElements(autocompleteOption).firstOrNull { opt ->
                opt.isDisplayed && opt.text.lineSequence().firstOrNull()
                    ?.startsWith(city, ignoreCase = true) == true
            }
        }!!
        cityOption.click()
        return this
    }

    fun selectDates(checkInDays: Long = 7, checkOutDays: Long = 12): HotelsPage {
        val field = clickable(checkInField)
        Actions(driver).moveToElement(field).pause(Duration.ofMillis(150)).click().perform()
        wait.until {
            driver.findElements(anyDateCell).any { it.isDisplayed }
        }
        pickDay(checkInDays)
        pickDay(checkOutDays)
        return this
    }

    fun search(): HotelsResultsPage {
        val originalWindow = driver.windowHandles.first()
        val urlBefore = driver.currentUrl

        jsClick(searchButton)

        wait.until {
            driver.currentUrl != urlBefore ||
                    driver.windowHandles.size > 1
        }

        if (driver.windowHandles.size > 1) {
            driver.switchTo().window(
                driver.windowHandles.first { it != originalWindow }
            )
        }

        dismissPopup()

        return HotelsResultsPage(driver)
    }

    private fun pickDay(daysFromNow: Long) {
        val date = LocalDate.now().plusDays(daysFromNow)
        val dateId = "date-${date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
        val day = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@data-test-id='$dateId']")
            )
        )
        jsClick(day)
    }

    private fun dismissPopup() {
        driver.findElements(popupClose)
            .firstOrNull { it.isDisplayed }
            ?.let { jsClick(it) }
    }
}
