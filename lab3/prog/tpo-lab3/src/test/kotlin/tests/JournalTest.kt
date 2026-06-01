package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pages.HomePage

class JournalTest : BaseTest() {

    @Test
    fun journal_searchReturnsRelevantResults() {
        val journal = HomePage(driver)
            .open()
            .openJournal()
        assertTrue(journal.isLoaded())

        journal.search("Санкт-Петербург")
        assertTrue(journal.hasRelevantSearchResults())
    }
}
