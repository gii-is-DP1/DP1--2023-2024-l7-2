package org.springframework.samples.petclinic.object;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class ObjectService {
    
    ObjectRepository repo;

    @Autowired
    public ObjectService(ObjectRepository repo){
        this.repo=repo;
    }

@Transactional(readOnly = true)    
    List<Object> getObjects(){
        return repo.findAll();
    }
    
    @Transactional(readOnly = true)    
    public Object getById(int id){
        Optional<Object> result=repo.findById(id);
        return result.isPresent()?result.get():null;
    }

    @Transactional
    public Object saveObject(@Valid Object newObject) {
        return repo.save(newObject);
    }

    
    @Transactional
    public void deleteObjectById(int id){
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Object getObjectByName(String name){
        return repo.findByName(name);
    }

}