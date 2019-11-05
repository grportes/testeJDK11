package infra.functions;

@FunctionalInterface
public interface ThrowingSupplier<T> {

    T get() throws Throwable;
}
