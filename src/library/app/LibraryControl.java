package library.app;

import library.Exception.*;
import library.io.ConsolePrinter;
import library.io.DataReader;
import library.io.file.FileManager;
import library.io.file.FileManagerBuilder;
import library.model.*;

import java.util.InputMismatchException;

public class LibraryControl {
    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;
    private Library library;

    LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowano dane z pliku.");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę.");
            library = new Library();
        }
    }

    void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case EXIT -> exitOption();
                case ADD_BOOK -> addBook();
                case ADD_MAGAZINE -> addMagazine();
                case SHOW_BOOKS -> printBooks();
                case SHOW_MAGAZINES -> printMagazines();
                case DELETE_BOOK -> removeBook();
                case DELETE_MAGAZINE -> removeMagazine();
                case ADD_USER -> addUser();
                case SHOW_USERS -> printUsers();
                default -> printer.printLine("Wybrałeś błędną opcję. Wprowadź liczbę ponownie.");
            };
        } while (option != Option.EXIT);
    }

    private void printUsers() {
        printer.printUsers(library.getUsers().values());
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", podaj ponownie:");
            } catch (InputMismatchException ignored) {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie:");
            }
        }

        return option;
    }

    private void exitOption() {
        try {
            fileManager.exportData(library);
            printer.printLine("Export danych do pliku zakończony powodzeniem.");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Koniec programu, papa!");
        dataReader.close();
    }

    private void printBooks() {
        printer.printBooks(library.getPublications().values());
    }


    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć ksiązki.");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Limit publikacji został osiągnięty.");
        }
    }

    private void removeBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                printer.printLine("Usunięto książkę.");
            } else {
                printer.printLine("Brak wskazanej książki.");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć książki, niepoprawne dane.");
        }
    }

    private void printMagazines() {
        printer.printMagazines(library.getPublications().values());
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć czasopisma.");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Limit publikacji został osiągnięty.");
        }
    }

    private void removeMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine)) {
                printer.printLine("Usunięto magazyn.");
            } else {
                printer.printLine("Brak wskazanego magazynu.");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć ksiązki, niepoprawne dane.");
        }
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję:");
        for (Option value : Option.values()) {
            printer.printLine(value.toString());
        }
    }

    private enum Option {
        EXIT(0, "wyjście z programu"),
        ADD_BOOK(1, "dodanie nowej książki"),
        ADD_MAGAZINE(2, "dodanie nowego czasopisma"),
        SHOW_BOOKS(3, "wyświetl dostępne ksiązki"),
        SHOW_MAGAZINES(4, "wyświetl dostępne czasopisma"),
        DELETE_BOOK(5, "Usuń książkę"),
        DELETE_MAGAZINE(6, "Usuń czasopismo"),
        ADD_USER(7, "Dodaj czytelnika"),
        SHOW_USERS(8, "Wyświetl czytelników");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch(ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }
    }

}
