package Exceptions;

public class CountDateException extends Exception {
    public CountDateException(String message) {
        super(String.format("Количество данных не совпадает с необходимым! %s", message));
    }

    public CountDateException() {
        super("Количество данных не совпадает с необходимым!");
    }
}
