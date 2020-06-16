class B extends A {
    @Override
    B getThis() {
        return this;
    }

    int z;
    B setZ(int z) {
        this.z = z;
        return getThis();
    }
}
