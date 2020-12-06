package ATMpackage;

public enum ErrorCodes {
    OK(0, "Успешно"),
    ERROR(1, "Ошибка"),
    FATAL(2, "Фатальная ошибка");

    ErrorCodes(int code, String Message) {

    }
}
