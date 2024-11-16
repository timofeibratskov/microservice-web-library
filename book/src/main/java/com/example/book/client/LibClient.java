package com.example.book.client;

import com.example.book.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "library-service", url = "http://localhost:8070/library",configuration = ClientConfig.class)
public interface LibClient {

    @PostMapping("/book")
    void addBookToLibrary(@RequestParam("bookId") Long bookId);

    @DeleteMapping("/book/{bookId}")
    void deleteBookFromLibrary(@RequestParam("bookId") Long bookId);
}