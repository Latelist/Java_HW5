import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// 1)Реализуйте структуру телефонной книги с помощью HashMap. 
// Программа также должна учитывать, что во входной структуре будут повторяющиеся имена с разными телефонами, 
// их необходимо считать, как одного человека с разными телефонами. 
// Вывод должен быть отсортирован по убыванию числа телефонов.
// (можно выводить без сортировки, но обязательно в отдельном методе)


public class Task1 {
    Task1() throws IOException {
        System.out.println("Glad to see you!");
        String filename = "phonebook.txt";
        File file = fileCreator(filename);
        HashMap<String, ArrayList<Integer>> phoneBook = parcer(reader(filename));
        Scanner in = new Scanner(System.in);
        boolean move = true;
        while (move == true) {
            System.out.println("Enter command. Type «help» to see commands available: ");
            String command = in.nextLine();
            switch (command) {
                case "exit":
                    System.out.println("Have a nice day!");
                    move = false;
                    break;
                case "generate":
                    System.out.println();
                    deleter(file, filename);
                    phoneBook = phonebookGenerator();
                    writer(phoneBook, filename);
                    bookPrinter(phoneBook);
                    System.out.println();
                    break;
                case "help":
                    System.out.println();
                    helpShower();
                    break;
                case "add":
                    System.out.println();
                    scanner(in, phoneBook);
                    writer(phoneBook, filename);

                    System.out.println();
                    break;
                case "show":
                    System.out.println();
                    bookPrinter(phoneBook);
                    System.out.println();
                    break;
                case "clear":
                    System.out.println();
                    deleter(file, filename);
                    phoneBook.clear();
                    System.out.println("I deleted your phonebook");
                    System.out.println();
                    break;
                default:
                    System.out.println("I can't follow this command :c");
                    break;
            }
        }
        in.close();    
    }

    
    // Генерируем пример телефонной книги, если не хочется заполнять все с нуля
    public static HashMap<String, ArrayList<Integer>> phonebookGenerator() {
        HashMap<String, ArrayList<Integer>> phoneBook = new HashMap<>();
        phoneBook.put("Lare Croft", new ArrayList<Integer>());
        phoneBook.get("Lare Croft").add(91231);
        phoneBook.put("Vincent Vega", new ArrayList<Integer>());
        phoneBook.get("Vincent Vega").add(912314);
        phoneBook.put("Jack Sparrow", new ArrayList<Integer>());
        phoneBook.get("Jack Sparrow").add(912315);
        return phoneBook;
    }

    // Проверяем, является ли строка целым числом
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Записываем в файл либо всю книгу целиком, либо отдельный контакт
    public static void writer(HashMap<String, ArrayList<Integer>> phoneBook, String filename) throws IOException{
        FileWriter fw = new FileWriter(filename,  false);

        StringBuilder sb = new StringBuilder();
        for (String key : phoneBook.keySet()) {
            sb.append(key + ": " );
            for (Integer i : phoneBook.get(key)) {
                sb.append(i + " ");
            }
            sb.append("\n");
        }
        String res = sb.toString();
        fw.write(res);
        fw.close();
    }

    // Просим пользователя ввести новый контакт, считываем и заносим в мап. Если формат номера некорректный, выбрасываем исключение
    public static void scanner(Scanner in, HashMap<String, ArrayList<Integer>> phoneBook) {
        HashMap<String, ArrayList<Integer>> newPair =new HashMap<>();
        try {
            System.out.println("Enter name of contact: ");
            String keyName = in.nextLine();
            System.out.println("Enter phone number. You can enter several using space to split: ");
            String valueNumber = in.nextLine();
            String[] numbers = valueNumber.split(" ");
            ArrayList<Integer> nums = new ArrayList<>();
            for (String str : numbers) {
                nums.add(Integer.parseInt(str));
            }
            newPair.put(keyName, nums);
            integrator(newPair, phoneBook);
            System.out.println("I added contact successfully!");
        } catch (NumberFormatException e) {
            System.out.println("I can't parse this number...");
        }
            
    }

    // Добавляем в мап новый контакт или дописываем новые номера к старому контакту
    public static void integrator(HashMap<String, ArrayList<Integer>> pair, HashMap<String, ArrayList<Integer>> phoneBook) {
        for (String key : pair.keySet()) {
                if (phoneBook.containsKey(key)) {
                    for (Integer i : pair.get(key)) {
                        phoneBook.get(key).add(i);
                    }
                } else {
                    phoneBook.putIfAbsent(key, pair.get(key));
                }
            }
        }

    // Считываем телефонную книгу из файла — делаем при запуске приложения
    public static ArrayList<String> reader(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        return lines;
    }

    // Парсим считанные строки и заносим в хэшмэп
    public static HashMap<String, ArrayList<Integer>> parcer(ArrayList<String> lines) {
        HashMap<String, ArrayList<Integer>> phoneBook = new HashMap<>();
        for (String string : lines) {
            String[] pair = string.split(":");
            String[] numbers = pair[1].split(" ");
            ArrayList<Integer> nums = new ArrayList<>();
            for (String str : numbers) {
                if (isInteger(str)) {
                    nums.add(Integer.parseInt(str));
                }
            }
            phoneBook.put(pair[0], nums);
        }
        return phoneBook;
    }

    // Очищаем файл с телефонной книгой, если нужно
    public static void deleter(File file, String filename) throws IOException{
        if (file.exists() && !file.isDirectory()) {
            try (BufferedWriter bf = Files.newBufferedWriter(Path.of(filename), StandardOpenOption.TRUNCATE_EXISTING)) {
            } catch (IOException e) {}
        }
    }

    // Создаем файл для записи контактов, если его нет
    public static File fileCreator(String filename) throws IOException {
        File file = new File(filename);
        if (file.createNewFile()) {
            System.out.println("Phonebook created");
        } else {
            System.out.println("Phonebook opened");
        }
        return file;
    }

    // Выводим возможные команды для пользователя
    public static void helpShower() {
        System.out.println("I understand these commands:");
        System.out.println("«generate» — generates the example of phonebook for testing");
        System.out.println("«add» — adds new contact");
        System.out.println("«show» — displays all contact in console");
        System.out.println("«clear» — clears the phonebook");
        System.out.println("«exit» — stops the programm");
    }

    // Сортируем книгу через TreeMap для вывода по убыванию количества номеров. Сохраняем записи с одинаковым кол-вом номеров
    public static TreeMap<Integer, HashMap<String, ArrayList<Integer>>> sorter(HashMap<String, ArrayList<Integer>> phoneBook) {
        TreeMap<Integer, HashMap<String, ArrayList<Integer>>> treeMap = new TreeMap<>(
            Comparator.reverseOrder()
        );
        for (Map.Entry<String, ArrayList<Integer>> entry : phoneBook.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            int size = value.size();
            if (!treeMap.containsKey(size)) {
                treeMap.put(size, new HashMap<String, ArrayList<Integer>>());
                treeMap.get(size).put(key, value);
            } else {
                treeMap.get(size).put(key, value);
            }   
        }
        return treeMap;
    }

    // Красиво выводим список контактов в консоль
    public static void bookPrinter(HashMap<String, ArrayList<Integer>> phoneBook) {
        TreeMap<Integer, HashMap<String, ArrayList<Integer>>> sortedBook = sorter(phoneBook);
        for (Integer key : sortedBook.keySet()) {
            for (String name : sortedBook.get(key).keySet()){
                System.out.print(name + ": ");
                for (Integer number : sortedBook.get(key).get(name)) {
                    System.out.print(number + " ");
                }
                System.out.println(); 
            }            
        }
    }

}




