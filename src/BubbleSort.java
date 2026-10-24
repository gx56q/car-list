import java.util.Comparator;
import java.util.List;

public class BubbleSort<T> implements SortAlgorithm<T> {
    @Override
    public void sort(List<T> items, Comparator<T> comparator) {
        for (int end = items.size() - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (comparator.compare(items.get(i), items.get(i + 1)) > 0) {
                    T value = items.get(i);
                    items.set(i, items.get(i + 1));
                    items.set(i + 1, value);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }
}
