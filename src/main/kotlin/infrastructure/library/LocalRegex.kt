package infrastructure.library

enum class LocalRegex(val code: Regex) {
    EMAIL_PATTERN("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()),
    RC_PATTERN("^[a-zA-Z0-9]{10}$".toRegex()),
    LICENSE_PATTERN("^[a-zA-Z0-9]{6,12}$".toRegex()),
    DATE_PATTERN("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}\$".toRegex()),
    USERNAME_PATTERN("^[a-zA-Z_][a-zA-Z0-9_]{2,19}$".toRegex()),
    PASSWORD_PATTERN("""^(?=.*[!@#$%^&*()-=_+{};:'",.<>?/\\|[\\]`~])(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$""".toRegex()),
    NAME_PATTERN("^[a-zA-Z]+(?:\\s[a-zA-Z]+)+$".toRegex()),
    AADHAAR_NUMBER_PATTERN("^[0-9]{4}-[0-9]{4}-[0-9]{4}$".toRegex()),
    PHONE_PATTERN("""^(?:\+?\d{10,15}|(?:\d{3}[-._]?){2}\d{4}|(?:\(\d{3}\)[-_]?)?\d{10}|(?:\d{3}_?){3}\d{3})$""".toRegex()),
    VEHICLE_REG_NO_PATTERN("^[A-Z]{2}\\d{2}[A-Z]\\d{4}$".toRegex())
}