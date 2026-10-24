import java.time.Year;

public class Car {
    private final String model;
    private final int power;
    private final int year;
    private final String color;
    private final int mileage;

    public Car(String model, int power, int year, String color, int mileage) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Введите модель");
        }
        if (model.contains(";")) {
            throw new IllegalArgumentException("В модели нельзя использовать ;");
        }
        if (model.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("В модели есть скрытый символ");
        }
        if (power <= 0) {
            throw new IllegalArgumentException("Мощность должна быть больше 0");
        }
        if (year < 1886 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Год выпуска: от 1886 до " + Year.now().getValue());
        }
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Введите цвет");
        }
        if (color.contains(";")) {
            throw new IllegalArgumentException("В цвете нельзя использовать ;");
        }
        if (color.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("В цвете есть скрытый символ");
        }
        if (mileage < 0) {
            throw new IllegalArgumentException("Пробег не может быть меньше 0");
        }
        this.model = model.strip();
        this.power = power;
        this.year = year;
        this.color = color.strip();
        this.mileage = mileage;
    }

    public String getModel() {
        return model;
    }

    public int getPower() {
        return power;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public int getMileage() {
        return mileage;
    }

    @Override
    public String toString() {
        return model + " | " + power + " л.с. | " + year + " | " + color + " | " + mileage + " км";
    }
}
