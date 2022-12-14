package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookItem;
import com.example.demo.entity.BookStatus;
import com.example.demo.repository.BookItemRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookItemRepository bookItemRepository) {
        this.bookRepository = bookRepository;
        this.bookItemRepository = bookItemRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        boolean exist = bookRepository.existsById(book.getIsbn());
        if (exist) {
            throw new IllegalStateException("Book entry with isbn " + book.getIsbn() + " already exists!");
        }
        bookRepository.save(book);
    }

    public void addBookItem(String isbn, String barcode) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new IllegalStateException("Book entry with isbn " + isbn + " already exists!"));

        int currentItemsNumber = bookItemRepository.numberOfBookItemsByIsbn(isbn);
        if (currentItemsNumber == book.getNumOfCopies()) {
            throw new IllegalStateException("The number of book items have matched the data!");
        }
        BookItem bookItem = new BookItem(barcode, book, BookStatus.AVAILABLE);
        bookItemRepository.save(bookItem);
    }

    public void editBook(String isbn, Book book) {
        Book foundBook = bookRepository.findById(isbn).orElseThrow(
                () -> new IllegalStateException("Book entry with isbn " + book.getIsbn() + " does not exist!"));

        if (book.getTitle() != null && !Objects.equals(book.getTitle(), "")) {
            foundBook.setTitle(book.getTitle());
        }
        if (book.getNumOfCopies() > 0) {
            foundBook.setNumOfCopies(book.getNumOfCopies());
        }
        if (book.getAuthor() != null && !Objects.equals(book.getAuthor(), "")) {
            foundBook.setAuthor(book.getAuthor());
        }
        if (book.getPrice() > 0) {
            foundBook.setPrice(book.getPrice());
        }
        if (book.getPublisher() != null && !Objects.equals(book.getPublisher(), "")) {
            foundBook.setPublisher(book.getPublisher());
        }
        if (book.getCategory() != null) {
            foundBook.setCategory(book.getCategory());
        }
        if (book.getNumOfPages() > 0) {
            foundBook.setNumOfPages(book.getNumOfPages());
        }
        if (book.getShelfNum() != null && !Objects.equals(book.getShelfNum(), "")) {
            foundBook.setShelfNum(book.getShelfNum());
        }
        bookRepository.save(foundBook);
    }

    public void removeBook(String isbn) {
        boolean exist = bookRepository.existsById(isbn);
        if (!exist) {
            throw new IllegalStateException("Book entry with isbn " + isbn + " does not exist!");
        }
        List<String> bookItems = bookItemRepository.selectAllBarcodeByIsbn(isbn);
        bookItemRepository.deleteAllById(bookItems);
    }

    public List<Book> searchBooksByTitleOrAuthor(String keyword) {
        return bookRepository.selectBooksByTitleOrAuthor(keyword);
    }

    public List<Book> searchBooksByCategory(String category) {
        return bookRepository.selectBooksByCategory(category);
    }

}
