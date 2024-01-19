package infrastructure.library

enum class PrintType {
    PRINT,
    PRINTLN
}

object OutputHandler {
    fun colorCoatedMessage(message: String, color: TextColor) {
        println("$color$message${TextColor.RESET}")
    }

    fun displayPrompt(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.WHITE, printType)
    }

    fun displaySuccess(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.GREEN, printType)
    }

    fun displayWarning(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.YELLOW, printType)
    }

    fun displayError(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.RED, printType)
    }

    fun displayMenu(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.PEACH, printType)
    }

    fun displayMap(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.MAGENTA, printType)
    }

    fun disPlayPlain(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.BLUE, printType)
    }

    fun displayHeader(message: String, printType: PrintType = PrintType.PRINTLN) {
        messagePrinter(message, TextColor.CYAN, printType)
    }

    private fun messagePrinter(message: String, color: TextColor, printType: PrintType) {
        if (message.isNotEmpty()) {
            when (printType) {
                PrintType.PRINTLN -> println("$color$message${TextColor.RESET}")
                PrintType.PRINT -> print("$color$message${TextColor.RESET}")
            }
        }
    }
}