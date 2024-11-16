package com.example.book.client;

import com.example.book.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "lib",configuration = ClientConfig.class)
public interface LibClient {

    @PostMapping("/library/book")
    void addBookToLibrary(@RequestParam("bookId") Long bookId);

    @DeleteMapping("/library/book/{bookId}")
    void deleteBookFromLibrary(@PathVariable("bookId") Long bookId);
}