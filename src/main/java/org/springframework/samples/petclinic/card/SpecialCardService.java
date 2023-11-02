package org.springframework.samples.petclinic.card;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpecialCardService {
        
    SpecialCardRepository repo;

    @Autowired
    public SpecialCardService(SpecialCardRepository repo){
        this.repo=repo;
    }

    @Transactional(readOnly = true)    
    List<SpecialCard> getSpecialCards(){
        return repo.findAll();
    }
    
    @Transactional(readOnly = true)    
    public SpecialCard getById(int id){
        Optional<SpecialCard> result=repo.findById(id);
        return result.isPresent()?result.get():null;
    }

    @Transactional
    public SpecialCard saveSpecialCard(@Valid SpecialCard newCard) {
        return repo.save(newCard);
    }

    
    @Transactional
    public void deleteSpecialCardById(int id){
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SpecialCard getSpecialCardByName(String name){
        return repo.findByName(name);
    }
}
