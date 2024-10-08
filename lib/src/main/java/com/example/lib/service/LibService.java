package com.example.lib.service;

import com.example.lib.dto.BookStatusDto;
import com.example.lib.entity.BookStatusEntity;
import com.example.lib.exceptions.BookNotAvailableException;
import com.example.lib.exceptions.ResourceNotFoundException;
import com.example.lib.repository.LibRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // Удаление статуса книги
    public void deleteBookStatus(Long bookId) {
        BookStatusEntity bookStatusEntity = libRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found in Library with ID: " + bookId));
        libRepository.delete(bookStatusEntity);
    }

    // Взять книгу
    public BookStatusDto borrowBook(Long id) {
        BookStatusEntity bookStatus = libRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        if (!bookStatus.isAvailable()) {
            throw new BookNotAvailableException("Book with ID " + id + " is not available for borrowing.");
        }

        bookStatus.setBorrowedAt(LocalDateTime.now());
        bookStatus.setReturnedAt(LocalDateTime.now().plusDays(14));
        bookStatus.setAvailable(false);

        return modelMapper.map(libRepository.save(bookStatus), BookStatusDto.class);
    }

    // Вернуть книгу
    public BookStatusDto returnBook(Long bookId) {
        BookStatusEntity bookStatusEntity = libRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        if (bookStatusEntity.isAvailable()) {
            throw new BookNotAvailableException("Book with ID " + bookId + " is not currently borrowed.");
        }

        bookStatusEntity.setBorrowedAt(null);
        bookStatusEntity.setReturnedAt(null);
        bookStatusEntity.setAvailable(true);

        return modelMapper.map(libRepository.save(bookStatusEntity), BookStatusDto.class);
    }

    @Transactional(readOnly = true)
    public BookStatusDto getBookStatus(Long bookId) {
        BookStatusEntity bookStatusEntity = libRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        return modelMapper.map(bookStatusEntity, BookStatusDto.class);
    }

    @Transactional(readOnly = true)
    public List<BookStatusDto> getAvailableBooks() {
        List<BookStatusEntity> availableBooks = libRepository.findAllByIsAvailableTrue();
        return availableBooks.stream()
                .map(bookStatusEntity -> modelMapper.map(bookStatusEntity, BookStatusDto.class))
                .collect(Collectors.toList());
    }
}
