import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final Scanner scanner;
    private final PrintStream out;
    private final List<Car> cars = new ArrayList<>();

    public Main(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            new Main(scanner, out).run();
        }
    }

    public void run() {
        try {
            while (true) {
                out.println("""

                        Список автомобилей
                        1. Добавить автомобиль
                        2. Загрузить автомобили из файла
                        3. Отфильтровать и отсортировать (Stream API)
                        4. Отсортировать весь список
                        5. Показать весь список
                        0. Выйти""");
                try {
                    int command = readInt("Выберите пункт: ");
                    switch (command) {
                        case 1 -> addCar();
                        case 2 -> loadCars();
                        case 3 -> processCars();
                        case 4 -> sortCars();
                        case 5 -> showCars(cars);
                        case 0 -> {
                            return;
                        }
                        default -> out.println("Нет такого пункта меню");
                    }
                } catch (NumberFormatException e) {
                    out.println("Введите целое число");
                } catch (IllegalArgumentException e) {
                    out.println(e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            return;
        }
    }

    private int readInt(String prompt) {
        out.print(prompt);
        return Integer.parseInt(scanner.nextLine().strip());
    }

    private void addCar() {
        out.print("Модель: ");
        String model = scanner.nextLine();
        int power = readInt("Мощность, л.с.: ");
        int year = readInt("Год выпуска: ");
        out.print("Цвет: ");
        String color = scanner.nextLine();
        int mileage = readInt("Пробег, км: ");
        cars.add(new Car(model, power, year, color, mileage));
        out.println("Автомобиль добавлен");
    }

    private void loadCars() {
        out.print("Путь к файлу: ");
        String path = scanner.nextLine().strip();
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Введите путь к файлу");
        }
        try {
            List<Car> loaded = CarFile.load(Path.of(path), out);
            cars.addAll(loaded);
            out.println("Загружено автомобилей: " + loaded.size());
        } catch (InvalidPathException e) {
            out.println("Некорректный путь к файлу");
        } catch (NoSuchFileException e) {
            out.println("Файл не найден: " + path);
        } catch (AccessDeniedException e) {
            out.println("Нет доступа к файлу: " + path);
        } catch (CharacterCodingException e) {
            out.println("Сохраните файл в UTF-8");
        } catch (IOException e) {
            out.println("Не удалось прочитать файл: " + path);
        }
    }

    private void processCars() {
        if (cars.isEmpty()) {
            out.println("Список пуст");
            return;
        }
        int minPower = readInt("Минимальная мощность, л.с.: ");
        if (minPower <= 0) {
            throw new IllegalArgumentException("Мощность должна быть больше 0");
        }
        List<String> result = cars.stream()
                .filter(car -> car.getPower() >= minPower)
                .sorted(Comparator.comparingInt(Car::getPower))
                .map(Car::toString)
                .toList();
        out.println();
        if (result.isEmpty()) {
            out.println("Нет автомобилей мощностью от " + minPower + " л.с.");
        } else {
            out.println("Мощность от " + minPower + " л.с., по возрастанию:");
            result.forEach(out::println);
        }
    }

    private void sortCars() {
        if (cars.isEmpty()) {
            out.println("Список пуст");
            return;
        }
        out.println("Алгоритм сортировки:\n1. Пузырьком\n2. Вставками");
        int choice = readInt("Выберите алгоритм: ");
        SortAlgorithm<Car> algorithm;
        String name;
        switch (choice) {
            case 1 -> {
                algorithm = new BubbleSort<>();
                name = "Пузырьком";
            }
            case 2 -> {
                algorithm = new InsertionSort<>();
                name = "Вставками";
            }
            default -> throw new IllegalArgumentException("Выберите алгоритм 1 или 2");
        }
        int direction = readInt("Направление: 1 - по возрастанию, 2 - по убыванию: ");
        if (direction != 1 && direction != 2) {
            throw new IllegalArgumentException("Выберите направление 1 или 2");
        }
        Comparator<Car> comparator = Comparator.comparingInt(Car::getPower);
        if (direction == 2) {
            comparator = comparator.reversed();
        }
        List<Car> sorted = new ArrayList<>(cars);
        algorithm.sort(sorted, comparator);
        out.println(name + ": мощность "
                + (direction == 1 ? "по возрастанию" : "по убыванию"));
        showCars(sorted);
    }

    private void showCars(List<Car> values) {
        out.println();
        if (values.isEmpty()) {
            out.println("Список пуст");
        } else {
            values.forEach(out::println);
        }
    }
}
