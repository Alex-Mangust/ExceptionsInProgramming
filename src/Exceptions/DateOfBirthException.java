package Exceptions;

/** Класс, описывающий исключение при неккоретно введенной дате рождения */
public class DateOfBirthException extends Exception {

    /**
     * Конструктор класса
     * @param message - сообщение пользователю
     */
    public DateOfBirthException(String message) {
        super(String.format("Ошибка даты рождения! %s", message));
    }
    
    /**
     * Конктруктор класса
     * @param e - ошибка
     */
    public DateOfBirthException(Exception e) {
        super(e.getMessage());
    }
}
