import java.io.*;
import java.util.*;

public class Application_3 {  
    public static void main(String args[]) {
        Author author_1 = new Author("Ryan Holiday");
        Author author_2 = new Author("Robert Greene"); 

        Book book_1 = new Book("The Obstacle Is the Way", author_1);
        Book book_2 = new Book("The 48 Laws of Power", author_2);

        Reader reader_1 = new Reader("Mike");
        Reader reader_2 = new Reader("Kyle");

        Library library = new Library();
            library.add_book_to_shelf(book_1);
            library.add_book_to_shelf(book_2);

            library.add_reader(reader_1);
            library.add_reader(reader_2);

            library.lend_book(book_1, reader_1);
            library.lend_book(book_2, reader_2);

        System.out.println(library);

        try {
            FileOutputStream file_out = new FileOutputStream("library.ser");
            ObjectOutputStream out = new ObjectOutputStream(file_out);
                out.writeObject(library);

            file_out.close();
            out.close();
        } catch (IOException e) { e.printStackTrace(); }

        Library deserialized_library = null;
        try {
            FileInputStream file_in = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(file_in);
                deserialized_library = (Library) in.readObject();

            file_in.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        if (deserialized_library != null) { System.out.println("\n~ DESERIALIZED STATE ~\n" + deserialized_library); }
    }
}

class Reader implements Externalizable {
    private String name;
    private List<Book> borrowed_books;

    public Reader() {}

    public Reader(String name) {
        this.name = name;
        this.borrowed_books = new ArrayList<>();
    }

    public String get_name() { return name; }
    public void set_name(String new_name) { this.name = new_name; }

    public void borrow_book(Book book) { borrowed_books.add(book); }

    public List<Book> get_borrowed_books() { return borrowed_books; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(borrowed_books);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        borrowed_books = (List<Book>) in.readObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb.append("reader name: ").append(name).append("\n");
            sb.append("borrowed books list:\n");
            for (Book book : borrowed_books)
                sb.append(book).append("\n");

        return sb.toString();
    }
}

class Author implements Externalizable {
    private String name;

    public Author() {}

    public Author(String name) { this.name = name; }

    public String get_name() { return name; }
    public void set_name(String new_name) { this.name = new_name; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException { out.writeObject(name); }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException { 
        name = (String) in.readObject(); 
    }

    @Override
    public String toString() { return name; }
}

class Book implements Externalizable {
    private String title;
    private Author author;

    public Book() {}

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public String get_title() { return title; }
    public void set_title(String new_title) { this.title = new_title; }

    public Author get_author() { return author; }
    public void set_author(Author new_author) { this.author = new_author; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeObject(author);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        author = (Author) in.readObject();
    }

    @Override
    public String toString() { return "- \"" + title + "\" by " + author.get_name(); }
}

class LibraryShelf implements Externalizable {
    private List<Book> books;

    public LibraryShelf() { books = new ArrayList<>(); }

    public void add_book(Book book) { books.add(book); }

    public List<Book> get_books() { return books; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException { out.writeObject(books); }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        books = (List<Book>) in.readObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb.append("library shelf:\n");
            for (Book book : books)
                sb.append(book).append("\n");

        return sb.toString();
    }
}

class Library implements Externalizable {
    private List<Reader> readers;
    private LibraryShelf library_shelf;

    public Library() {
        readers = new ArrayList<>();
        library_shelf = new LibraryShelf();
    }

    public void add_reader(Reader reader) { readers.add(reader); }

    public void add_book_to_shelf(Book book) { library_shelf.add_book(book); }

    public void lend_book(Book book, Reader reader) { reader.borrow_book(book); }

    public List<Reader> get_all_readers() { return readers; }

    public LibraryShelf get_library_shelf() { return library_shelf; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(readers);
        out.writeObject(library_shelf);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        readers = (List<Reader>) in.readObject();
        library_shelf = (LibraryShelf) in.readObject();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb.append("[ LIBRARY ]:\n");

            sb.append("library shelf: \n");
            for (Book book : library_shelf.get_books())
                sb.append("\t").append("- \"" + book.get_title() + "\" by " + book.get_author() + "\n");

            sb.append("readers: \n");
            for (Reader reader : readers) {
                sb.append("\t" + reader.get_name()).append("\n");
                for (Book borrowed_book : reader.get_borrowed_books()) {
                    sb.append("\tborrowed books: \n");
                    sb.append("\t\t").append("- \"" + borrowed_book.get_title() + "\" by " + borrowed_book.get_author() + "\n");
                }
            }

        return sb.toString();
    }
}
