package com.example.lib.controller;

import com.example.lib.dto.BookStatusDto;
import com.example.lib.service.LibService;
import com.example.lib.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibController {

    private final LibService libraryService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Получить список свободных книг", description = "Возвращает список книг, доступных для взятия")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список свободных книг успешно получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/books/available")
    public ResponseEntity<List<BookStatusDto>> getAvailableBooks(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        List<BookStatusDto> availableBooks = libraryService.getAvailableBooks();
        return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBookStatus(@PathVariable Long bookId, HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        // Проверка, что токен валиден и пользователь имеет роль администратора
        if (token == null || !jwtProvider.validateToken(token) ||
                !jwtProvider.getRoleFromToken(token).contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если проверка не пройдена, вернется 403 Forbidden
        }

        // Если проверка пройдена, удалить книгу
        libraryService.deleteBookStatus(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Добавление книги", description = "Добавляет книгу в систему на основе bookId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Книга успешно добавлена"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/book")
    public ResponseEntity<BookStatusDto> addBook(@RequestParam Long bookId, HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        // Проверка, что токен валиден и пользователь имеет роль администратора
        if (token == null || !jwtProvider.validateToken(token) ||
                !jwtProvider.getRoleFromToken(token).contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если проверка не пройдена, вернется 403 Forbidden
        }
        BookStatusDto bookStatusDto = libraryService.addBook(bookId);
        return new ResponseEntity<>(bookStatusDto, HttpStatus.CREATED);
    }


    @Operation(summary = "Взять книгу", description = "Помечает книгу как взятую и устанавливает дату возврата через 14 дней")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно взята"),
            @ApiResponse(responseCode = "400", description = "Книга не доступна для взятия"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/book/borrow")
    public ResponseEntity<BookStatusDto> borrowBook(@RequestParam Long bookId,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        // Проверка, что токен валиден и пользователь имеет роль администратора
        if (token == null || !jwtProvider.validateToken(token) ||
                !jwtProvider.getRoleFromToken(token).contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если проверка не пройдена, вернется 403 Forbidden
        }
        BookStatusDto bookStatusDto = libraryService.borrowBook(bookId);
        return new ResponseEntity<>(bookStatusDto, HttpStatus.OK);
    }

    @Operation(summary = "Вернуть книгу", description = "Возвращает книгу в библиотеку, делая ее доступной для других пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно возвращена"),
            @ApiResponse(responseCode = "400", description = "Книга не была взята"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/book/return")
    public ResponseEntity<BookStatusDto> returnBook(@RequestParam Long bookId,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        // Проверка, что токен валиден и пользователь имеет роль администратора
        if (token == null || !jwtProvider.validateToken(token) ||
                !jwtProvider.getRoleFromToken(token).contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если проверка не пройдена, вернется 403 Forbidden
        }
        BookStatusDto bookStatusDto = libraryService.returnBook(bookId);
        return new ResponseEntity<>(bookStatusDto, HttpStatus.OK);
    }

    @Operation(summary = "Получить статус книги", description = "Возвращает информацию о текущем статусе книги (доступна или занята)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус книги успешно получен"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/book/status")
    public ResponseEntity<BookStatusDto> getBookStatus(@RequestParam Long bookId,HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);

        // Проверка, что токен валиден и пользователь имеет роль администратора
        if (token == null || !jwtProvider.validateToken(token) ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Если проверка не пройдена, вернется 403 Forbidden
        }
        BookStatusDto bookStatusDto = libraryService.getBookStatus(bookId);
        return new ResponseEntity<>(bookStatusDto, HttpStatus.OK);
    }
}
