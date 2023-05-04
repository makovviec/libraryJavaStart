package library.io;

import library.model.*;

import java.util.Collection;

public class ConsolePrinter {
    public void printBooks(Collection<Publication> publications) {
        long count = publications.stream()
                .filter(pub -> pub instanceof Book)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();

        if (count == 0)
            printLine("Brak książek w bibliotece");
    }

    public void printMagazines(Collection<Publication> publications) {
        long count = publications.stream()
                .filter(pub -> pub instanceof Magazine)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();

        if (count == 0)
            printLine("Brak czasopism w bibliotece");
    }

    public void printUsers(Collection<LibraryUser> users) {
        users.stream()
                .map(User::toString)
                .forEach(this::printLine);
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
