package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import com.example.book.exceptions.ResourceNotFoundException;
import com.example.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDto.class);
    }

    @Transactional
    public BookDto findByIsbn(String isbn) {
        log.info("Fetching book with ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ISBN: " + isbn));
        return modelMapper.map(book, BookDto.class);
    }

    public BookDto addBook(BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(book);
        notifyBookStatusService(savedBook.getId());
        return modelMapper.map(savedBook, BookDto.class);

    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        modelMapper.map(bookDto, book);  // Обновляем существующую сущность данными из DTO
        Book updatedBook = bookRepository.save(book);
        return modelMapper.map(updatedBook, BookDto.class);
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.deleteById(id);
    }
}

