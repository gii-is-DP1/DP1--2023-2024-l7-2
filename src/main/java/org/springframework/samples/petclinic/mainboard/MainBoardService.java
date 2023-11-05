package org.springframework.samples.petclinic.mainboard;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MainBoardService {
    
    MainBoardRepository repo;

    
    @Autowired
    public MainBoardService(MainBoardRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    List<MainBoard> getMainBoard() {
        return repo.findAll();
    }

    @Transactional
    public MainBoard saveMainBoard(@Valid MainBoard newMainBoard) {
        return repo.save(newMainBoard);
    }

    @Transactional(readOnly = true)
    public MainBoard getById(int id) {
        Optional<MainBoard> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }
}
