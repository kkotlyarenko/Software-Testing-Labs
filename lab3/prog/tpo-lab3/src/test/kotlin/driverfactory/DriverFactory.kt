package driverfactory

import config.TestConfig
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.util.logging.Level
import java.util.logging.Logger

object DriverFactory {

    private val mutedLoggers = listOf(
        "org.openqa.selenium.devtools",
        "org.openqa.selenium.chromium",
        "org.openqa.selenium.devtools.CdpVersionFinder"
    ).map { Logger.getLogger(it).apply { level = Level.OFF } }

    fun create(
        browser: String = TestConfig.browser
    ): WebDriver {
        return when (browser) {
            "firefox" -> {
                WebDriverManager.firefoxdriver().setup()
                FirefoxDriver()
            }
            else -> {
                WebDriverManager.chromedriver().setup()
                ChromeDriver()
            }
        }
    }
}