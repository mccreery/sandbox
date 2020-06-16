abstract class Fluent<T extends Fluent<T>> {
    abstract T getThis();

    int x;
    T setX(int x) {
        this.x = x;
        return getThis();
    }
}
