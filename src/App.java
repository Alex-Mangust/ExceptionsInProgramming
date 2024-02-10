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
import java.util.Date;
import java.util.Scanner;

import Exceptions.CountDateException;

public class App {
    public static void main(String[] args) throws Exception {
        String surname = null;
        String name = null;
        String patronymic = null;
        Date date = null;
        Long number = null;
        Character gender = null;

        String personData = input(
                "Введите, через пробел, ваши данные\n1 - Фамилия Имя Отчество\n2 - дата_рождения\n3 - номер_телефона\n4 - пол (m или f)\n(Данные можно вводить в произвольном порядке)");
        String[] personArray = personData.split(" ");
        try {
            errorCountData(personArray.length);
            for (String data : personArray) {
                if (convertToDate(data) != null)
                    date = convertToDate(data);
                else if (convertToNumber(data) != null)
                    number = convertToNumber(data);
                else if (convertToChar(data) != null)
                    gender = convertToChar(data);
                else {
                    if (surname == null) {
                        surname = data;
                    } else if (name == null) {
                        name = data;
                    } else if (patronymic == null) {
                        patronymic = data;
                    }
                }
            }
            String missingData = new String();
            if (surname == null)
                missingData += "фамилии;";
            if (name == null)
                missingData += "имени;";
            if (patronymic == null)
                missingData += "отчества;";
            if (date == null)
                missingData += "даты рождения;";
            if (number == null)
                missingData += "номера телефона;";
            if (gender == null)
                missingData += "пола";
            String[] missingArray = missingData.split(";");
            missingData = new String();
            for (int i = 0; i < missingArray.length; i++) {
                missingData += missingArray[i];
                if (i < missingArray.length - 2)
                    missingData += ", ";
                else if (i < missingArray.length - 1)
                    missingData += " и ";
            }
            if (!missingData.isEmpty())
                throw new CountDateException(String.format("Не хватает %s", missingData));
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            String person = String.format("<%s><%s><%s><%s> <%d><%s>", surname, name, patronymic,
                    formatDate.format(date), number, gender);
            try (FileReader reader = new FileReader(String.format("save_data\\%s.txt", surname))) {
                try (FileWriter writer = new FileWriter(String.format("save_data\\%s.txt", surname), true)) {
                    writer.write(person + "\n");
                } catch (IOException e) {
                    throw new IOException(e);
                }
            } catch (Exception notFindFile) {
                try (FileWriter writer = new FileWriter(String.format("save_data\\%s.txt", surname), false)) {
                    writer.write(person + "\n");
                } catch (IOException e) {
                    throw new IOException(e);
                }
            }
        } catch (CountDateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void errorCountData(int length) throws CountDateException {
        if (length < 6)
            throw new CountDateException("Данных недостаточно!");
        else if (length > 6)
            throw new CountDateException("Лишние данные!");
    }

    private static Character convertToChar(String charaster) {
        if (charaster.length() == 1)
            return charaster.charAt(0);
        else
            return null;
    }

    private static Long convertToNumber(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return null;
        }
    }

    private static Date convertToDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date d = format.parse(date);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private static String input(String message) throws IOException {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println(message);
            return in.nextLine();
        }
    }
}
