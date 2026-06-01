package config

object TestConfig {
    val baseUrl: String = System.getProperty("baseUrl") ?: "https://www.aviasales.ru/"
    val browser: String = System.getProperty("browser")?.lowercase() ?: "chrome"
}