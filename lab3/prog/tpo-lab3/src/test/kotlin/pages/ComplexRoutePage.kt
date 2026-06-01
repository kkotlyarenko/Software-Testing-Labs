package pages

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ComplexRoutePage(
    driver: WebDriver
) : BasePage(driver) {

    private val form = By.xpath("//*[@data-test-id='multiway-form']")
    private val originInputs = By.xpath("//*[@data-test-id='multiway-origin-input']")
    private val destinationInputs = By.xpath("//*[@data-test-id='multiway-destination-input']")
    private val dateButtons = By.xpath("//*[@data-test-id='multiway-date']")
    private val submitButton = By.xpath("//*[@data-test-id='form-submit']")
    private val autocompleteOption = By.xpath("//*[@role='option']")

    fun isLoaded(): Boolean = waitFor {
        driver.findElements(form).any { it.isDisplayed }
    }

    fun hasDirection(number: Int): Boolean = waitFor {
        driver.findElements(By.xpath("//*[@data-test-id='multiway-direction-$number']"))
            .any { it.isDisplayed }
    }

    fun setSegment(
        index: Int, origin: String, destination: String, daysFromNow: Long
    ): ComplexRoutePage {
        fillCity(driver.findElements(originInputs)[index], origin)
        fillCity(driver.findElements(destinationInputs)[index], destination)
        pickDate(index, daysFromNow)
        return this
    }

    fun submitSearch(): SearchResultsPage {
        val originalWindow = driver.windowHandles.first()
        val urlBefore = driver.currentUrl
        jsClick(submitButton)
        wait.until {
            driver.currentUrl != urlBefore || driver.windowHandles.size > 1
        }
        if (driver.windowHandles.size > 1) {
            driver.switchTo().window(
                driver.windowHandles.first { it != originalWindow })
        }
        return SearchResultsPage(driver)
    }

    private fun fillCity(input: WebElement, city: String) {
        input.click()
        input.sendKeys(Keys.CONTROL, "a")
        input.sendKeys(Keys.DELETE)
        input.sendKeys(city)
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

    private fun pickDate(segmentIndex: Int, daysFromNow: Long) {
        val target = LocalDate.now().plusDays(daysFromNow)

        wait.until {
            try {
                driver.findElements(dateButtons)[segmentIndex].click()
                driver.findElements(By.xpath("//*[starts-with(@data-test-id,'date-')]"))
                    .any { it.isDisplayed }
            } catch (_: Exception) {
                false
            }
        }

        driver.findElements(By.xpath("//*[@data-test-id='select-month']"))
            .firstOrNull()?.let { select ->
                try {
                    Select(select).selectByValue(
                        target.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    )
                } catch (_: Exception) { }
            }

        val dateId = "date-${target.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
        val day = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@data-test-id='$dateId']/ancestor::button[1]")
            )
        )
        jsClick(day)
    }
}
