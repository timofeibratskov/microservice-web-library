package com.example.book.controller;

import com.example.book.dto.BookCreateOrUpdateDto;
import com.example.book.dto.BookDto;
import com.example.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.book.jwt.JwtProvider;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")

public class BookController {
    private final JwtProvider jwtProvider;
    private final BookService bookService;

    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг")

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<BookDto> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по указанному ID")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга найдена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        BookDto book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по указанному ISBN")

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(
            @PathVariable String isbn,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        BookDto book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Добавить новую книгу", description = "Создает новую книгу")

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookCreateOrUpdateDto bookDto, HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token) || !jwtProvider.getRoleFromToken(token).contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Возвращаем 403, если нет прав
        }
        BookDto savedBook = bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Обновить книгу", description = "Обновляет информацию о книге по указанному ID")

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
            @RequestBody BookCreateOrUpdateDto bookUpdateDto,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)||!jwtProvider.getRoleFromToken(token).get(0).contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        BookDto updatedBook = bookService.updateBook(id, bookUpdateDto);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)||!jwtProvider.getRoleFromToken(token).get(0).contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
