package infrastructure.library.input_handler

import infrastructure.library.LocalRegex
import infrastructure.library.OutputHandler.displayError
import infrastructure.library.OutputHandler.displayPrompt
import infrastructure.library.OutputHandler.displayWarning
import infrastructure.library.PrintType
import presentationlayer.Constants.DEFAULT_ERROR_INVALID_INPUT
import presentationlayer.Constants.DEFAULT_WARNING_GO_BACK
import presentationlayer.Constants.DEFAULT_WARNING_LIST_VALUE_NOT_MATCH
import presentationlayer.Constants.DEFAULT_WARNING_NOT_IN_RANGE
import presentationlayer.Constants.DEFAULT_WARNING_NULL_OR_EMPTY
import presentationlayer.Constants.DEFAULT_WARNING_REGEX
import presentationlayer.Constants.DEFAULT_WARNING_TRY_AGAIN
import presentationlayer.Constants.MINUS_ONE_STRING

data class Messages(
    val minusOneNotAllowed: String = DEFAULT_WARNING_GO_BACK,
    val regexNotMatch: String = DEFAULT_WARNING_REGEX,
    val nullOrEmpty: String = DEFAULT_WARNING_NULL_OR_EMPTY,
    val listValueNotMatches: String = DEFAULT_WARNING_LIST_VALUE_NOT_MATCH,
    val tryAgain: String = DEFAULT_WARNING_TRY_AGAIN,
    val notInRange: String = DEFAULT_WARNING_NOT_IN_RANGE
)


data class InputHandler(
    private val prompt: String = "",
    private val message: Messages = Messages(),
    private val allowedValues: List<String>? = null,
    private val allowEmpty: Boolean = false,
    private val allowMinusOne: Boolean = false,
    private val caseSensitive: Boolean = true,
    private val sizeRange: IntRange? = null,
    private val regexPattern: LocalRegex? = null,
    ) {
    fun getString(): String {

        var input: String?

        while (true) {
            displayPrompt(if (prompt == "") "" else "$prompt :", PrintType.PRINT)
            input = readLine()?.trim()

            if (input == MINUS_ONE_STRING) {
                if (allowMinusOne) return input
                else {
                    displayWarning(message.minusOneNotAllowed); continue
                }
            }

            if (!inputValidation(input)) continue
            return input.orEmpty()
        }
    }

    fun getLong(): Long {
        return getNumericInput(String::toLong) as Long
    }

    fun getInt(rangeBetween: IntRange = Int.MIN_VALUE..Int.MAX_VALUE): Int {
        var num: Int
        do {
            num = getNumericInput(String::toInt) as Int
        } while ((num !in rangeBetween).also { if (it) displayWarning(DEFAULT_WARNING_NOT_IN_RANGE) })
        return num
    }

    private fun inputValidation(input: String?): Boolean {
        if (allowedValues != null) {
            val inputToCheck = if (input is String && !caseSensitive) input.uppercase() else input
            if (inputToCheck !in allowedValues) {
                displayWarning("${message.listValueNotMatches} ${message.tryAgain} "); return false
            }
        }

        if (!allowEmpty && input.isNullOrEmpty()) {
            displayWarning(message.nullOrEmpty); return false
        }

        if (sizeRange != null && !isSizeInRange(input)) {
            displayWarning("${message.notInRange} ${message.tryAgain}"); return false
        }

        if (regexPattern != null && !matchesRegexPattern(input)) {
            displayWarning("${message.regexNotMatch} ${message.tryAgain}"); return false
        }

        return true
    }


    private fun isSizeInRange(input: Any?): Boolean {
        return when (input) {
            is String? -> sizeRange?.contains(input?.length ?: 0) ?: true
            is Long? -> sizeRange?.contains(input?.getSize() ?: 0) ?: true
            is Int? -> sizeRange?.contains(input?.getSize()) ?: true
            else -> false
        }
    }

    private fun matchesRegexPattern(input: String?): Boolean {
        return regexPattern?.let { input?.matches(it.code) } ?: false
    }

    private fun getNumericInput(parseFunction: (String) -> Number): Number {
        var num: Number
        while (true) {
            try {
                num = parseFunction(getString())
                break
            } catch (e: NumberFormatException) {
                displayError(DEFAULT_ERROR_INVALID_INPUT)
            }
        }
        return num
    }

    private fun Long.getSize(): Long {
        var count = 0L
        var temp = this
        while (temp > 0) {
            count++
            temp /= 10
        }
        return count
    }

    private fun Int.getSize(): Int {
        var count = 0
        var temp = this
        while (temp > 0) {
            count++
            temp /= 10
        }
        return count
    }
}