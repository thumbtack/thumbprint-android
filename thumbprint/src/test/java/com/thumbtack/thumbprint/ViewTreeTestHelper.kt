package com.thumbtack.thumbprint

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.fail

/**
 * Helper class for finding views and checking information in views, primarily for Robolectric
 * tests.
 */
object ViewTreeTestHelper {
    fun assertTextVisible(rootView: View, expectedText: String) {
        assertNotNull(findViewWithText(rootView, expectedText)) {
            "Expected text \"$expectedText\" not found. View hierarchy:\n" +
                    walkViewHierarchy(rootView)
        }
    }

    fun assertTextNotVisible(rootView: View, expectedText: String) {
        assertNull(findViewWithText(rootView, expectedText)) {
            "Expected text \"$expectedText\" was found. View hierarchy:\n" +
                    walkViewHierarchy(rootView)
        }
    }

    fun findViewWithTextAndAssert(rootView: View, expectedText: String): View {
        return findViewWithText(rootView, expectedText) ?: fail {
            "No view found with \"$expectedText\". View hierarchy:\n" +
                    walkViewHierarchy(rootView)
        }
    }

    private fun findViewWithText(
        rootView: View,
        expectedText: String,
        isRootVisible: Boolean = true
    ): View? {
        val isVisible = isRootVisible && rootView.visibility == View.VISIBLE
        return when (rootView) {
            is TextView -> rootView.takeIf { isVisible && expectedText == rootView.text.toString() }
            is ViewGroup -> {
                for (i in 0 until rootView.childCount) {
                    findViewWithText(rootView.getChildAt(i), expectedText, isVisible)
                        ?.let { return it }
                }
                null
            }
            else -> null
        }
    }

    private fun walkViewHierarchy(
        rootView: View?,
        stringBuilder: StringBuilder = StringBuilder(),
        level: Int = 0
    ): String {
        val indent = " ".repeat(level * 2)
        if (rootView is TextView) {
            stringBuilder.append(
                "$indent${rootView::class.java} ID=${rootView.id}, " +
                        "text=${rootView.text} visibility=${rootView.visibility}\n"
            )
        }
        if (rootView is ViewGroup) {
            stringBuilder.append(
                "$indent${rootView::class.java} ID=${rootView.id} " +
                        "visibility=${rootView.visibility} ->\n"
            )
            for (i in 0 until rootView.childCount) {
                walkViewHierarchy(rootView.getChildAt(i), stringBuilder, level + 1)
            }
        }

        return stringBuilder.toString()
    }
}
