package com.example.lib;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookStatusService {
    private final LibRepository libRepository;

    public void saveLib(Lib lib){
        libRepository.save(lib);
    }
    public List<Lib> findAllLibs(){
        return libRepository.findAll();
    }
    private void notifyBookStatusService(Long bookId) {
        // Создаем объект запроса с нулями для borrowedTo и returnedAt
        BookStatusRequest request = new BookStatusRequest(bookId, null, null);

        try {
            // Отправляем POST-запрос в сервис bookstatus
            ResponseEntity<String> response = restTemplate.postForEntity(BOOK_STATUS_SERVICE_URL, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Book status created successfully for bookId: {}", bookId);
            } else {
                log.error("Failed to create book status for bookId: {}", bookId);
            }
        } catch (Exception e) {
            log.error("Error while notifying book status service for bookId: {}", bookId, e);
        }
    }


}
