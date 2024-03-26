package pl.pja.s28201.exception;

public class ClassNumberIsNotSupported extends RuntimeException {
    public ClassNumberIsNotSupported() {
    }

    public ClassNumberIsNotSupported(String message) {
        super(message);
    }
}
