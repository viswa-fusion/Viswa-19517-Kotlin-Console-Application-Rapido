package infrastructure.library

enum class TextColor(private val code: String) {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    RESET("\u001B[0m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    PEACH("\u001B[38;2;255;203;164m");
    override fun toString(): String {
        return this.code
    }
}