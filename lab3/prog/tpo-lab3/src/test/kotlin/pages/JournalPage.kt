package pages

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class JournalPage(
    driver: WebDriver
) : BasePage(driver) {

    private val searchIconBtn = By.xpath("//button[contains(@class,'SearchButton_searchButton')]")
    private val searchInput = By.xpath("//input[@placeholder='Поиск']")
    private val anyHeading = By.xpath("//h1 | //h2 | //h3")

    private val shortWait = WebDriverWait(driver, Duration.ofSeconds(4))

    private var lastQuery = ""

    fun isLoaded(): Boolean = waitFor {
        driver.currentUrl.contains("/psgr") && driver.findElements(anyHeading).any { it.isDisplayed }
    }

    fun search(query: String): JournalPage {
        lastQuery = query
        jsClick(searchIconBtn)
        val input = shortWait.until(
            ExpectedConditions.visibilityOfElementLocated(searchInput)
        )
        input.clear()
        input.sendKeys(query)
        input.sendKeys(Keys.ENTER)
        return this
    }

    fun hasRelevantSearchResults(): Boolean {
        val stem = lastQuery.take(4)
        return waitFor {
            driver.findElements(By.xpath("//h1 | //h2 | //h3 | //a[contains(@href,'/psgr/')]"))
                .any { el ->
                    runCatching { el.isDisplayed && el.text.contains(stem, ignoreCase = true) }
                        .getOrDefault(false)
                }
        }
    }
}
