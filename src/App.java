// Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
// Фамилия Имя Отчество датарождения номертелефона пол
// Форматы данных:
// фамилия, имя, отчество - строки
// датарождения - строка формата dd.mm.yyyy
// номертелефона - целое беззнаковое число без форматирования
// пол - символ латиницей f или m.

// Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.

// Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.

// Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
// <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
// Однофамильцы должны записаться в один и тот же файл, в отдельные строки.

// Не забудьте закрыть соединение с файлом.
// При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import Exceptions.CountDateException;
import Exceptions.DateOfBirthException;
import Exceptions.GenderException;

public class App {
    public static void main(String[] args) throws Exception {
        String surname = null; // Переменная для записи фамилии
        String name = null; // Переменная для записи имени
        String patronymic = null; // Переменная для записи отчества
        Date date = null; // Переменная для записи дня рождения
        Long number = null; // Переменная для записи номера телефона
        Character gender = null; // Переменная для записи пола

        String personData = input(
                "Введите, через пробел, ваши данные\n1 - Фамилия Имя Отчество\n2 - дата_рождения\n3 - номер_телефона\n4 - пол (m или f)\n(Данные можно вводить в произвольном порядке)"); // Переменная для хранения введенных данных
        String[] personArray = personData.split(" "); // Массив строк с введенными данными
        try {
            errorCountData(personArray.length); // Проверяется, что количество данных совпадает с необходимым
            for (String data : personArray) { // Цикл проходит по всему массиву с данными
                if (convertToNumber(data) != null) // Проверяется может ли элемент конвертироваться в число
                    number = convertToNumber(data); // Если элемент конвертируется в число, то это указан номер телефона человека
                else if (convertToDate(data) != null) { // Проверяется, может ли элемент конвертироваться в дату
                    date = convertToDate(data); // Если элемент конвертируется в дату, то это указана дата рождения человека
                    try {
                        checkDateOfBirth(date); // Проверяется, корректна ли введенная дата рождения (Человек не может быть старше 120 лет и не может быть еще не рожден)
                    } catch (DateOfBirthException e) {
                        throw new DateOfBirthException(e); // Если дата рождения некорректна выбрасывается исключение
                    }
                } else if (convertToChar(data) != null) { // Проверяется, может ли элемент конвертироваться в символ
                    gender = convertToChar(data); // Если элемент конвертируется в символ, то это указан пол человека
                    try {
                        checkInputGenderChar(gender); // Проверяется, корректно ли указан пол человека (Должен быть указан символ "m" или "f")
                    } catch (GenderException e) {
                        throw new GenderException(e); // Если пол указан некорректно выбрасывается исключение
                    }
                } else { // Если не срабатывает ни одно условие выше, то элемент может быть фамилией, именем или отчеством
                    if (surname == null) { // Проверяется, что переменная для хранения фамилии равняется null
                        surname = data; // Переменной присваивается значение элемента
                    } else if (name == null) { // Проверяется, что переменная для хранения имени равняется null
                        name = data; // Переменной присваивается значение элемента
                    } else if (patronymic == null) { // Проверяется, что переменная для хранения отчества равняется null
                        patronymic = data; // Переменной присваивается значение элемента
                    }
                }
            }
            String missingData = new String(); // Переменная для хранения сообщения о недостающих данных. (В случае, если количество введенных данных совпадает с необходимым, а их типы нет)
            if (surname == null) // Если переменная для хранения записи фамилии равняется null, записывается сообщение об этом
                missingData += "фамилии;";
            if (name == null)
                missingData += "имени;"; // Если переменная для хранения записи имени равняется null, записывается сообщение об этом
            if (patronymic == null)
                missingData += "отчества;"; // Если переменная для хранения записи отчества равняется null, записывается сообщение об этом
            if (date == null)
                missingData += "даты рождения;"; // Если переменная для хранения записи даты рождения равняется null, записывается сообщение об этом
            if (number == null)
                missingData += "номера телефона;"; // Если переменная для хранения записи номера телефона равняется null, записывается сообщение об этом
            if (gender == null) // Если переменная для хранения записи пола равняется null, записывается сообщение об этом
                missingData += "пола";
            String[] missingArray = missingData.split(";"); // Массив, хранящий недостающие данные
            missingData = new String(); // Переменная для хранения недостающих данных обнуляется
            for (int i = 0; i < missingArray.length; i++) { // Циклп роходит по всему массиву
                missingData += missingArray[i]; // В переменную записываются элементы
                if (i < missingArray.length - 2) // После каждого элемента ставится запятая, если это не предпоследний и не последний элемент
                    missingData += ", ";
                else if (i < missingArray.length - 1) // Если это предпоследний элемент, то после него ставится " и "
                    missingData += " и ";
            }
            if (!missingData.isEmpty()) // Если имеются недостающие данные, выбрасывается исключение
                throw new CountDateException(String.format("Не хватает %s", missingData));
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy"); // Экземпляр класса SimpleDateFormat для форматирования даты, согласно шаблону
            String person = String.format("<%s><%s><%s><%s> <%d><%s>", surname, name, patronymic,
                    formatDate.format(date), number, gender); // Переменная для хранения всех данных о человеке
            try (FileReader reader = new FileReader(String.format("save_data\\%s.txt", surname))) { // Экземпляр класса FileReader для чтения файла. Таким образом происходит проверка, имеется ли файл с нужным именем (именем служит фамилия человека). Если имеется, то происходит запись в него.
                try (FileWriter writer = new FileWriter(String.format("save_data\\%s.txt", surname), true)) { // Экземпляр класса FileWriter для записи в файл в режиме добавления.
                    writer.write(person + "\n"); // В файл записывается строка person и добавляется символ перехода на новую строку
                } catch (IOException e) { // Если не удалось записать данные в файл, выбрасывается исключение
                    throw new IOException(e); 
                }
            } catch (Exception notFindFile) { // Если файла с нужным именем нет, создается новый
                try (FileWriter writer = new FileWriter(String.format("save_data\\%s.txt", surname), false)) { // Экземпляр класса FileWriter для записи в файл в режиме перезаписи.
                    writer.write(person + "\n"); // В файл записывается строка person и добавляется символ перехода на новую строку
                } catch (IOException e) { // Если не удалось записать данные в файл, выбрасывается исключение
                    throw new IOException(e);
                }
            }
        } catch (CountDateException e) { // Если количество введенных данных недостаточно, либо превышает необходимое, выбрасывается исключение
            throw new CountDateException(e);
        }
    }

    /**
     * Приватный статический метод для проверки корректности указанного пола человека
     * @param gender - пол человека
     */
    private static void checkInputGenderChar(Character gender) throws GenderException {
        if (gender != 'f' && gender != 'm') { // Если пол указан не в виде символа 'f' или 'm', выбрасывается исключение
            throw new GenderException("Пол должен быть указан либо символом \"m\", либо символом \"f\"");
        }
    }

    /**
     * Приватный метод для проверки корректности указанной даты рождения
     * @param date - дата рождения
     */
    private static void checkDateOfBirth(Date date) throws DateOfBirthException {
        Calendar calendarPeople = Calendar.getInstance(); // Создается экземпляр класса календарь для человека, данные о котором ввели
        Calendar calendar = Calendar.getInstance(); // Создается экземпляр класса календарь
        calendarPeople.setTime(date); // Календарю задается время даты рождения человека
        calendar.setTime(new Date()); // Календарю задается время текущей даты
        if (calendarPeople.get(Calendar.YEAR) <= calendar.get(Calendar.YEAR) - 120) { // Если год рождения человека меньше или равен году сто двадцатилетней давности от текущей даты, выбрасывается исключение
            throw new DateOfBirthException("Человек слишком стар!");
        } else {
            if (calendarPeople.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) { // Если год рождения человека больше текущего года, выбрасывается исключение
                throw new DateOfBirthException("Человек еще не родился!");
            } else if (calendarPeople.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                if (calendarPeople.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) { // Если год рождения человека равен текущему году, но месяц больше месяца текущего года, выбрасывается исключение
                    throw new DateOfBirthException("Человек еще не родился!");
                } else if (calendarPeople.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    if (calendarPeople.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)) { // Если год рождения человека и месяц рождения равен текущему году и текущему месяцу, но день рождения больше сегодняшнего дня, выбрасывается исключение
                        throw new DateOfBirthException("Человек еще не родился!");
                    }
                }
            }
        }
    }

    /**
     * Приватный статический метод для проверки количества введенных данных
     * @param length - количество введенных данных
     */
    private static void errorCountData(int length) throws CountDateException {
        if (length < 6) // Если данных недостаточно, выбрасывается исключение
            throw new CountDateException("Данных недостаточно!");
        else if (length > 6) // Если данных больше, чем нужно, выбрасывается исключение
            throw new CountDateException("Лишние данные!");
    }

    /**
     * Приватный статический метод для конвертации строки в символ
     * @param charaster - строка для конвертации
     */
    private static Character convertToChar(String charaster) {
        if (charaster.length() == 1)
            return charaster.charAt(0);
        else
            return null;
    }

    /**
     * Приватный статический метод для конвертации строки в число
     * @param number - строка для конвертации
     */
    private static Long convertToNumber(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Приватный статический метод для конвертации строки в дату
     * @param date - строка для конвертации
     */
    private static Date convertToDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date d = format.parse(date);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Приватный статический метод для ввода данных
     * @param message - сообщение пользователю
     */
    private static String input(String message) throws IOException {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println(message);
            return in.nextLine();
        }
    }
}
