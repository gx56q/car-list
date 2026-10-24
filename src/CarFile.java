import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CarFile {
    public static List<Car> load(Path path, PrintStream out) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (line.isBlank()) {
                    continue;
                }
                try {
                    String[] fields = line.split(";", -1);
                    if (fields.length != 5) {
                        throw new IllegalArgumentException("Нужно 5 полей: модель;мощность;год;цвет;пробег");
                    }
                    cars.add(new Car(fields[0], Integer.parseInt(fields[1].strip()),
                            Integer.parseInt(fields[2].strip()), fields[3], Integer.parseInt(fields[4].strip())));
                } catch (NumberFormatException e) {
                    out.println("Строка " + lineNumber + ": мощность, год и пробег должны быть целыми числами");
                } catch (IllegalArgumentException e) {
                    out.println("Строка " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return cars;
    }
}
