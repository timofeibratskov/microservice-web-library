package com.example.lib;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lib {
    @Id
    private Long id;
    private String title;
    private String author;
}
