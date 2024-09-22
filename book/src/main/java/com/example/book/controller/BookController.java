package com.example.book.controller;

import com.example.book.dto.BookDto;
import com.example.book.repository.BookRepository;
import com.example.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")

public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;


    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга найдена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по указанному ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(
            @PathVariable String isbn) {
        BookDto book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Добавить новую книгу", description = "Создает новую книгу")
    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Обновить книгу", description = "Обновляет информацию о книге по указанному ID")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
            @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
