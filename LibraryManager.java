import java.io.*;
import java.util.*;

class Book {
    private String title, author, isbn;
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
    public String getIsbn() { return isbn; }
    public String toString() { return title + " by " + author + " (ISBN: " + isbn + ")"; }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private Map<String, String> loans = new HashMap<>(); // ISBN -> User

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
        saveData();
    }

    public void borrowBook(String isbn, String user) throws Exception {
        if (loans.containsKey(isbn)) throw new Exception("Book already borrowed!");
        if (books.stream().noneMatch(b -> b.getIsbn().equals(isbn))) throw new Exception("Book not found!");
        loans.put(isbn, user);
        saveData();
    }

    public void listBooks() {
        if (books.isEmpty()) System.out.println("No books available.");
        else books.forEach(System.out::println);
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("library.dat"))) {
            oos.writeObject(books);
            oos.writeObject(loans);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("library.dat"))) {
            books = (List<Book>) ois.readObject();
            loans = (Map<String, String>) ois.readObject();
        } catch (Exception e) {
            books = new ArrayList<>();
            loans = new HashMap<>();
        }
    }
}

public class LibraryManager {
    public static void main(String[] args) {
        Library lib = new Library();
        lib.loadData();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Book\n2. Borrow Book\n3. List Books\n4. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();
            try {
                if (choice.equals("1")) {
                    System.out.print("Title: "); String t = sc.nextLine();
                    System.out.print("Author: "); String a = sc.nextLine();
                    System.out.print("ISBN: "); String i = sc.nextLine();
                    lib.addBook(t, a, i);
                    System.out.println("Book added!");
                } else if (choice.equals("2")) {
                    System.out.print("ISBN: "); String i = sc.nextLine();
                    System.out.print("User: "); String u = sc.nextLine();
                    lib.borrowBook(i, u);
                    System.out.println("Book borrowed!");
                } else if (choice.equals("3")) {
                    lib.listBooks();
                } else if (choice.equals("4")) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}