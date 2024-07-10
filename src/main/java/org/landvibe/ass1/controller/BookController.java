package org.landvibe.ass1.controller;

import lombok.RequiredArgsConstructor;
import org.landvibe.ass1.domain.Book;
import org.landvibe.ass1.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public Long orderInsertBook(@RequestParam String title) {
        return bookService.insertBook(title).getId();
    }

    @GetMapping("/{id}")
    public Book orderGetBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<Book> orderGetAllBooks() {
        return bookService.getAllBooks();
    }
}
