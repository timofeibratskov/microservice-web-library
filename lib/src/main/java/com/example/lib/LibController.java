package com.example.lib;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libs")
@RequiredArgsConstructor
public class LibController {

    private final BookStatusService bookStatusService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLib(@RequestBody Lib lib) {
        bookStatusService.saveLib(lib);
    }

    @GetMapping
    public ResponseEntity<List<Lib>> findAllLibs() {
        return ResponseEntity.ok(bookStatusService.findAllLibs());
    }
}
