package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.entity.BookEntity;
import com.example.book.exceptions.ResourceNotFoundException;
import com.example.book.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final String libUrl = "http://localhost:8070/library/book";

    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDto.class))
                .collect(Collectors.toList());
    }

    public BookDto findBookById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Transactional
    public BookDto findByIsbn(String isbn) {
        BookEntity bookEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ISBN: " + isbn));
        return modelMapper.map(bookEntity, BookDto.class);
    }

    public BookDto addBook(BookDto bookDto) {
        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            throw new EntityExistsException("Книга с таким ISBN уже существует: " + bookDto.getIsbn());
        }
        BookEntity bookEntity = modelMapper.map(bookDto, BookEntity.class);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        restTemplate.postForObject(libUrl + "?bookId=" + savedBookEntity.getId(), null, String.class);
        return modelMapper.map(savedBookEntity, BookDto.class);

    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        modelMapper.map(bookDto, bookEntity);  // Обновляем существующую сущность данными из DTO
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return modelMapper.map(updatedBookEntity, BookDto.class);
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.deleteById(id);
        String url ="http://localhost:8070/library/book/" + id;
        restTemplate.delete(url);
    }
}

