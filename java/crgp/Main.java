public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // Both setX and setY return A so no problems
        A a = new A().setX(5).setY(10);

        // setY still returns A so setZ cannot be called without a downcast
        B b = ((B)new B().setX(5).setY(10)).setZ(15);

        // Using covariant return types all methods return C
        // but every method has to be overridden in C
        C c = new C().setX(5).setY(10).setZ(15);
    }
}
