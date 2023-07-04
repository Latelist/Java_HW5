import java.util.Random;
import java.util.Scanner;

// 2) Реализовать алгоритм пирамидальной сортировки (HeapSort)(найти метод в Интернете и включить в проект)


public class Task2 {

    Task2() {
        int l = lengthAsker();
        int[] array = arrayGenerator(l);
        printer(array);
        for (int n = l; n > 1; --n) {
            for (int i = n / 2 - 1; i >= 0; --i) {
                heapifier(array, n, i);
            }
            firstToLastChanger(array, n-1);
        }
        printer(array);
        System.out.println();
    }

    // Просто печатаем массив
    public static void printer(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    // Запрашиваем у пользователя длину массива для генерации
    public static int lengthAsker() throws NumberFormatException{
        Scanner in = new Scanner(System.in);
        System.out.print("Enter array length: ");
        System.out.println();
        int length = in.nextInt();
        in.close();
        return length;
    }

    // Генерируем массив с заданной длиной, заполняем случайными числами
    public static int[] arrayGenerator(int length) {
        int[] array = new int[length];
        Random rnd = new Random();
        for (int i = 0; i < length; ++i) {
            array[i] = rnd.nextInt(100);
        }
        return array;
    }

    // Преобразуем массив в кучу
    public static void heapifier(int[] array, int n, int i) {
        int core = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[core]) {
            core = left;
        }

        if (right < n && array[right] > array[core]) {
            core = right;
        }

        if (!(core == i)) {
            int temp = array[i];
            array[i] = array[core];
            array[core] = temp;

            heapifier(array, n, core);
        }
    }

    // Меняем первый элемент с элементом с нужным индексом
    public static void firstToLastChanger(int[] array, int j) {
        int temp = array[0];
        array[0] = array[j];
        array[j] = temp;
    }

    

}
