package com.example.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateOrUpdateDto {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String description;
}