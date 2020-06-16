class A extends Fluent<A> {
    @Override
    A getThis() {
        return this;
    }

    int y;
    A setY(int y) {
        this.y = y;
        return getThis();
    }
}
