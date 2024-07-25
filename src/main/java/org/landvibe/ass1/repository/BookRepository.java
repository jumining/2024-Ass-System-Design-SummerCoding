package org.landvibe.ass1.repository;

import org.landvibe.ass1.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);
    Optional<Book> findBookById(Long id);
    List<Book> findAllBooks();
    Long deleteBookById(Long id);
}
