package resources;

import java.util.Objects;

public class Pair<S, T> implements Comparable<Pair<S, T>> {

    private final S firstElement;
    private final T secondElement;

    public Pair(S firstElement, T secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public S getFirstElement() {
        return firstElement;
    }

    public T getSecondElement() {
        return secondElement;
    }

    @Override
    public int compareTo(Pair<S, T> o) {
        return this.firstElement.toString().compareTo(o.firstElement.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        return Objects.equals(getFirstElement(), pair.getSecondElement())
                && Objects.equals(getSecondElement(), pair.getSecondElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstElement(), getSecondElement());
    }
}
