package tests

import driverfactory.DriverFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver

abstract class BaseTest {

    protected lateinit var driver: WebDriver

    @BeforeEach
    fun setUp() {
        driver = DriverFactory.create()
        driver.manage().window().maximize()
    }

    @AfterEach
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }
}