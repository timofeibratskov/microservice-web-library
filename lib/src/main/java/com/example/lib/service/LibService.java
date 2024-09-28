package com.example.lib.service;


import com.example.lib.dto.BookStatusDto;
import com.example.lib.entity.BookStatusEntity;
import com.example.lib.repository.LibRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LibService {
    private final LibRepository libRepository;
    private final ModelMapper modelMapper;


    public BookStatusDto addBook(Long id) {
        BookStatusEntity bookstatus = new BookStatusEntity(id);
        BookStatusEntity savedBookstatus = libRepository.save(bookstatus);
        return modelMapper.map(savedBookstatus, BookStatusDto.class);
    }

    public BookStatusDto borrowBook(Long id) {
        BookStatusEntity bookstatus = libRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!bookstatus.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        bookstatus.setBorrowedAt(LocalDateTime.now());
        bookstatus.setReturnedAt(LocalDateTime.now().plusDays(14));
        bookstatus.setAvailable(false);

        return modelMapper.map(libRepository.save(bookstatus), BookStatusDto.class);
    }

    public BookStatusDto returnBook(Long bookId) {
        BookStatusEntity BookStatusEntity = libRepository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (BookStatusEntity.isAvailable()) {
            throw new RuntimeException("Book is not borrowed");
        }

        BookStatusEntity.setBorrowedAt(null);
        BookStatusEntity.setReturnedAt(null);
        BookStatusEntity.setAvailable(true);
        return modelMapper.map(libRepository.save(BookStatusEntity), BookStatusDto.class);
    }

    // Получение статуса книги
    public BookStatusDto getBookStatus(Long bookId) {
        BookStatusEntity BookStatusEntity = libRepository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return modelMapper.map(BookStatusEntity, BookStatusDto.class);
    }

    // Получение списка свободных книг
    public List<BookStatusDto> getAvailableBooks() {
        List<BookStatusEntity> availableBooks = libRepository.findAllByIsAvailableTrue();
        return availableBooks.stream()
                .map(bookStatusEntity -> modelMapper.map(bookStatusEntity, BookStatusDto.class))
                .collect(Collectors.toList());
    }
}
