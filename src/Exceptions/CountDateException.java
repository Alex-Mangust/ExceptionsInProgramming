package Exceptions;

/** Класс, описывающий исключение при недостаточном или же превышенном количестве введенных данных */
public class CountDateException extends Exception {

    /**
     * Конструктор класса
     * @param message - сообщение пользователю
     */
    public CountDateException(String message) {
        super(String.format("Количество данных не совпадает с необходимым! %s", message));
    }

    /**
     * Конктруктор класса
     * @param e - ошибка
     */
    public CountDateException(Exception e) {
        super(e.getMessage());
    }
}
