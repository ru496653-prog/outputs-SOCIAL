package com.example

import com.example.outputs.data.local.CommentEntity
import com.example.outputs.data.model.PrivacyMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OutputsCommentTest {

    @Test
    fun testCommentEntityAndRepliesHierarchy() {
        val rootEntity = CommentEntity(
            id = "c1",
            postId = "p1",
            author = "Investigator",
            identityMode = PrivacyMode.PSEUDONYM,
            text = "Root comment regarding evidence",
            likes = 3,
            timestamp = "10m ago",
            isAcceptedAnswer = false,
            isTheory = true,
            parentCommentId = null,
            replyToAuthor = null,
            isSaved = true,
            isEdited = false
        )

        val replyEntity = CommentEntity(
            id = "c2",
            postId = "p1",
            author = "Cipher#402",
            identityMode = PrivacyMode.ANONYMOUS_NUMBER,
            text = "I concur with your theory",
            likes = 1,
            timestamp = "5m ago",
            isAcceptedAnswer = false,
            isTheory = false,
            parentCommentId = "c1",
            replyToAuthor = "Investigator",
            isSaved = false,
            isEdited = true
        )

        assertEquals("c1", rootEntity.id)
        assertTrue(rootEntity.isSaved)
        assertTrue(rootEntity.isTheory)
        assertFalse(rootEntity.isEdited)

        assertEquals("c1", replyEntity.parentCommentId)
        assertEquals("Investigator", replyEntity.replyToAuthor)
        assertTrue(replyEntity.isEdited)
        assertFalse(replyEntity.isSaved)
    }
}
