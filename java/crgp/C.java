class C extends A {
    @Override
    C setX(int x) {
        super.setX(x);
        return this;
    }

    @Override
    C setY(int y) {
        super.setY(y);
        return this;
    }

    int z;
    C setZ(int z) {
        this.z = z;
        return this;
    }
}
