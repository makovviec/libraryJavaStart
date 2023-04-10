package library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibraryUser extends User {
    private List<Publication> publicationHistory = new ArrayList<>();
    private List<Publication> borrowedPublication = new ArrayList<>();

    public LibraryUser(String firsName, String lastName, String pesel) {
        super(firsName, lastName, pesel);
    }

    public List<Publication> getPublicationHistory() {
        return publicationHistory;
    }

    public List<Publication> getBorrowedPublication() {
        return borrowedPublication;
    }

    public void addPublicationToHistory(Publication publication) {
        publicationHistory.add(publication);
    }

    public void borrowPublication(Publication publication) {
        borrowedPublication.add(publication);
    }

    public boolean returnPublication(Publication publication) {
        boolean ret = false;
        if (borrowedPublication.contains(publication)) {
            borrowedPublication.remove(publication);
            addPublicationToHistory(publication);
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LibraryUser that = (LibraryUser) o;
        return Objects.equals(publicationHistory, that.publicationHistory) && Objects.equals(borrowedPublication, that.borrowedPublication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), publicationHistory, borrowedPublication);
    }

    @Override
    public String toCsv() {
        return getFirsName() + ";" + getLastName() + ";" + getPesel();
    }


}
