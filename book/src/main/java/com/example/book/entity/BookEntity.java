package com.example.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Entity
@Data
@Slf4j
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class BookEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String description;
}

