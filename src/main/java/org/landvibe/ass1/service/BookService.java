package org.landvibe.ass1.service;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.cache.annotation.CacheInLandvibe;
import org.landvibe.ass1.cache.annotation.CacheOutLandvibe;
import org.landvibe.ass1.domain.Book;
import org.landvibe.ass1.cache.annotation.CachingLandvibe;
import org.landvibe.ass1.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @CacheInLandvibe(tableName = "bookCache", key = "#id")
    public Book insertBook(String title) {
        return bookRepository.save(new Book(title));
    }

    @CachingLandvibe(tableName = "bookCache", key = "#id")
    public Book getBookById(Long id) {
        return bookRepository.findBookById(id).orElseThrow();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    @CacheOutLandvibe(tableName = "bookCache", key = "#id")
    public Long deleteBookById(Long id) {
        return bookRepository.deleteBookById(id);
    }

    @CacheOutLandvibe(tableName = "bookCache", key = "#id")
    public void deleteBookFromCacheById(Long id) {
        System.out.println("delete from cache");
    }
}
