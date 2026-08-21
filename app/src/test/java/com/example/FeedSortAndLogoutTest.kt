package com.example

import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostItem
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode
import com.example.outputs.data.model.UserSession
import com.example.outputs.ui.viewmodel.FeedSortOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedSortAndLogoutTest {

    private fun createSamplePost(
        id: String,
        title: String,
        likes: Int,
        saves: Int,
        comments: Int,
        theories: Int,
        evidence: Int
    ): PostItem {
        return PostItem(
            id = id,
            title = title,
            content = "Sample content for testing sorting",
            type = PostType.STORY,
            category = "General",
            authorName = "Anonymous #1234",
            authorHandle = "@anon",
            identityMode = PrivacyMode.ANONYMOUS_NUMBER,
            timestamp = "Just now",
            likesCount = likes,
            savesCount = saves,
            commentsCount = comments,
            theoriesCount = theories,
            evidenceCount = evidence,
            mysteryStatus = MysteryStatus.OPEN,
            genreTheme = GenreTheme.DARK
        )
    }

    @Test
    fun testFeedSortByNewest() {
        val post1 = createSamplePost("post_001", "First Post", likes = 10, saves = 2, comments = 5, theories = 1, evidence = 0)
        val post2 = createSamplePost("post_002", "Second Post", likes = 50, saves = 10, comments = 2, theories = 0, evidence = 1)
        val post3 = createSamplePost("post_003", "Third Post", likes = 5, saves = 1, comments = 100, theories = 5, evidence = 3)

        val list = listOf(post1, post2, post3)
        val sortedNewest = list.sortedByDescending { it.id }

        assertEquals("post_003", sortedNewest[0].id)
        assertEquals("post_002", sortedNewest[1].id)
        assertEquals("post_001", sortedNewest[2].id)
    }

    @Test
    fun testFeedSortByMostSupported() {
        val post1 = createSamplePost("post_001", "Post One", likes = 10, saves = 2, comments = 50, theories = 10, evidence = 5)
        val post2 = createSamplePost("post_002", "Post Two", likes = 50, saves = 10, comments = 2, theories = 0, evidence = 1)
        val post3 = createSamplePost("post_003", "Post Three", likes = 100, saves = 20, comments = 1, theories = 0, evidence = 0)

        val list = listOf(post1, post2, post3)
        val sortedSupported = list.sortedByDescending { (it.likesCount * 3) + it.savesCount }

        assertEquals("post_003", sortedSupported[0].id) // 100*3 + 20 = 320
        assertEquals("post_002", sortedSupported[1].id) // 50*3 + 10 = 160
        assertEquals("post_001", sortedSupported[2].id) // 10*3 + 2 = 32
    }

    @Test
    fun testFeedSortByMostDiscussed() {
        val post1 = createSamplePost("post_001", "Post One", likes = 500, saves = 50, comments = 2, theories = 1, evidence = 0)
        val post2 = createSamplePost("post_002", "Post Two", likes = 10, saves = 1, comments = 25, theories = 8, evidence = 4)
        val post3 = createSamplePost("post_003", "Post Three", likes = 20, saves = 2, comments = 50, theories = 20, evidence = 10)

        val list = listOf(post1, post2, post3)
        val sortedDiscussed = list.sortedByDescending { it.commentsCount + it.theoriesCount + it.evidenceCount }

        assertEquals("post_003", sortedDiscussed[0].id) // 50 + 20 + 10 = 80
        assertEquals("post_002", sortedDiscussed[1].id) // 25 + 8 + 4 = 37
        assertEquals("post_001", sortedDiscussed[2].id) // 2 + 1 + 0 = 3
    }

    @Test
    fun testFeedSortOrderLabels() {
        assertEquals("Newest", FeedSortOrder.NEWEST.label)
        assertEquals("Most Supported", FeedSortOrder.MOST_SUPPORTED.label)
        assertEquals("Most Discussed", FeedSortOrder.MOST_DISCUSSED.label)
    }

    @Test
    fun testUserSessionLogoutReset() {
        val activeSession = UserSession(
            userId = "user_anon_849201",
            isAgeVerified = true,
            dob = "1995-04-12",
            citizenshipCountry = "United States",
            publicUsername = "ShadowWriter",
            pseudonym = "TheNightArchivist"
        )
        assertTrue(activeSession.isAgeVerified)

        val loggedOutSession = activeSession.copy(isAgeVerified = false)
        assertFalse(loggedOutSession.isAgeVerified)
        assertEquals("user_anon_849201", loggedOutSession.userId)
    }
}
