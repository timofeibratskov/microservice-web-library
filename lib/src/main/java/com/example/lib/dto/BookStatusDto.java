package com.example.lib.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookStatusDto {
    private Long bookId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private boolean isAvailable = true;
}
