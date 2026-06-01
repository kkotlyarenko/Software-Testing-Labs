package pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

abstract class BasePage(
    protected val driver: WebDriver
) {

    companion object {
        private const val WAIT_SECONDS = 15L
        private const val WAIT_REFRESH_MILLIS = 500L
    }

    protected val wait = WebDriverWait(
        driver, Duration.ofSeconds(WAIT_SECONDS)
    ).pollingEvery(Duration.ofMillis(WAIT_REFRESH_MILLIS))
    protected fun waitFor(seconds: Long = WAIT_SECONDS, condition: () -> Boolean): Boolean =
        try {
            WebDriverWait(driver, Duration.ofSeconds(seconds))
                .pollingEvery(Duration.ofMillis(WAIT_REFRESH_MILLIS))
                .until { runCatching(condition).getOrDefault(false) }
            true
        } catch (_: TimeoutException) {
            false
        }

    protected fun visible(by: By): WebElement = wait.until(
        ExpectedConditions.visibilityOfElementLocated(by)
    )

    protected fun clickable(by: By): WebElement = wait.until(
        ExpectedConditions.elementToBeClickable(by)
    )

    protected fun click(by: By) {
        clickable(by).click()
    }

    protected fun jsClick(by: By) {
        jsClick(clickable(by))
    }

    protected fun jsClick(element: WebElement) {
        (driver as JavascriptExecutor).executeScript(
            "arguments[0].click();", element
        )
    }

    protected fun scrollTo(by: By) {
        (driver as JavascriptExecutor).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            driver.findElement(by)
        )
    }

    fun scrollTo(element: WebElement) {
        (driver as JavascriptExecutor).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            element
        )
    }

}
