package swt.hse.de;

public class Book {
    private String title;
    private String author;
    private int year;
    private int edition;
    private String publisher;
    private int inStock;
    private int bookAvailable;
    private int borrowCount;
    private double rating;

    public Book(String title, String author, int year, int edition, String publisher, int inStock) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.edition = edition;
        this.publisher = publisher;
        this.inStock = inStock;
        this.bookAvailable = 0;
        this.borrowCount = 0;
        this.rating = 0;
    }

    // Only String for text fields
    public Book(String title, String author, String year, String edition, String publisher, String inStock) {
        this.title = title;
        this.author = author;
        this.year = Integer.parseInt(year);
        this.edition = Integer.parseInt(edition);
        this.publisher = publisher;
        this.inStock = Integer.parseInt(inStock);
        this.bookAvailable = 0;
        this.borrowCount = 0;
        this.rating = 0;
    }

    public Book(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.edition = book.getEdition();
        this.publisher = book.getPublisher();
        this.inStock = book.getInStock();
        this.bookAvailable = 0;
        this.borrowCount = 0;
        this.rating = 0;
    }

    // Don't know if necessary yet
    public Book getBook() {
        return this;
    }

    public void setBook(Book book) {
        new Book(book);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getBookAvailable() {
        return bookAvailable;
    }

    public void setBookAvailable(int bookAvailable) {
        this.bookAvailable = bookAvailable;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
