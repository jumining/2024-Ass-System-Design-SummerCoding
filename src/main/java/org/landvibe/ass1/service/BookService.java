package org.landvibe.ass1.service;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.domain.Book;
import org.landvibe.ass1.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book insertBook(String title) {
        return bookRepository.save(new Book(title));
    }

    public Book getBookById(Long id) {
        return bookRepository.findBookById(id).orElseThrow();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }
    public Long deleteBookById(Long id) {
        return bookRepository.deleteBookById(id);
    }
}
