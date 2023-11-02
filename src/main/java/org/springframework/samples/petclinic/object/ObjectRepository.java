package org.springframework.samples.petclinic.object;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ObjectRepository extends CrudRepository<Object, Integer> {
    List<Object> findAll();

    public Object findByName(String name);
}
