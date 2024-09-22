package com.example.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public void saveBook(Book book){
        bookRepository.save(book);
    }
    public List<Book> findAllBooks(){
        return bookRepository.findAll();
    }

}
