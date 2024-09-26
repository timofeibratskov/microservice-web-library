package com.example.lib.repository;

import com.example.lib.entity.BookStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibRepository extends JpaRepository<BookStatusEntity, Long> {

    Optional<BookStatusEntity> findByBookId(Long bookId);
}
