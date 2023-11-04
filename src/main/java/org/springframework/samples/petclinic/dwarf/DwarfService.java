package org.springframework.samples.petclinic.dwarf;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class DwarfService {

    DwarfRepository repo;

    @Autowired
    public DwarfService(DwarfRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    List<Dwarf> getDwarfs() {
        return repo.findAll();
    }

    @Transactional
    public Dwarf saveDwarf(@Valid Dwarf newDwarf) {
        return repo.save(newDwarf);
    }

    @Transactional(readOnly = true)
    public Dwarf getById(int id) {
        Optional<Dwarf> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional(readOnly = true)
    public Dwarf getDwarfsByColor(String color) {
        return repo.findByColor(color);
    }

    
}
