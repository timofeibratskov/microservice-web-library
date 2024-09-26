package com.example.lib.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookStatusEntity {
    @Id
    private Long bookId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private boolean isAvailable = true;


    public BookStatusEntity(Long id) {
        this.bookId = id;
    }
}
