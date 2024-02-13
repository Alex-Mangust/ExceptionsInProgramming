package Exceptions;

/** Класс, описывающий исключение при неккоретном указании пола */
public class GenderException extends Exception {

    /**
     * Конструктор класса
     * @param message - сообщение пользователю
     */
    public GenderException(String message) {
        super(String.format("Ошибка указания пола! %s", message));
    }

    /**
     * Конктруктор класса
     * @param e - ошибка
     */
    public GenderException(Exception e) {
        super(e.getMessage());
    }
}
