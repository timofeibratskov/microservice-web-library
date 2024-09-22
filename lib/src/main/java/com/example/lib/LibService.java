package com.example.lib;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibService {
    private final LibRepository libRepository;

    public void saveLib(Lib lib){
        libRepository.save(lib);
    }
    public List<Lib> findAllLibs(){
        return libRepository.findAll();
    }

}
