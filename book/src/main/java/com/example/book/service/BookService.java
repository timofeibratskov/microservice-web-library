package com.example.book.service;

import com.example.book.client.LibClient;
import com.example.book.dto.BookCreateOrUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import com.example.book.dto.BookDto;
import com.example.book.entity.BookEntity;
import com.example.book.exceptions.ResourceNotFoundException;
import com.example.book.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final LibClient libClient;

    @Transactional(readOnly = true)
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDto findBookById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Transactional(readOnly = true)
    public BookDto findByIsbn(String isbn) {
        BookEntity bookEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ISBN: " + isbn));
        return modelMapper.map(bookEntity, BookDto.class);
    }

    public BookDto addBook(BookCreateOrUpdateDto bookCreateDto) {
        if (bookRepository.findByIsbn(bookCreateDto.getIsbn()).isPresent()) {
            throw new EntityExistsException("Книга с таким ISBN уже существует: " + bookCreateDto.getIsbn());
        }
        BookEntity bookEntity = modelMapper.map(bookCreateDto, BookEntity.class);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        libClient.addBookToLibrary(savedBookEntity.getId());
        return modelMapper.map(savedBookEntity, BookDto.class);

    }

    public BookDto updateBook(Long id, BookCreateOrUpdateDto bookUpdateDto) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Проверка уникальности ISBN
        if (!bookEntity.getIsbn().equals(bookUpdateDto.getIsbn()) &&
                bookRepository.findByIsbn(bookUpdateDto.getIsbn()).isPresent()) {
            throw new EntityExistsException("Книга с таким ISBN уже существует. id: " + bookUpdateDto.getIsbn());
        }

        modelMapper.map(bookUpdateDto, bookEntity);  // Обновляем существующую сущность данными из DTO
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return modelMapper.map(updatedBookEntity, BookDto.class);
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.deleteById(id);
        libClient.deleteBookFromLibrary(id);
    }
}

