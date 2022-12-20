import java.util.Arrays;
import java.util.Optional;

public enum Status {
        
    NEW      ("Нов"),
    PROGRESS ("В процес"),
    FINISHED ("Завършен");

    private final String name;

    Status(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /*
     * Обратно търсене за потвърждение на валидността на дадения като
     * параметър String, т.е. един от зададените в инициализацията на класа
     */
    public static Optional<Status> get(String status) {
        return Arrays.stream(Status.values())
            .filter(st -> st.name.equals(status))
            .findFirst();
    }
};