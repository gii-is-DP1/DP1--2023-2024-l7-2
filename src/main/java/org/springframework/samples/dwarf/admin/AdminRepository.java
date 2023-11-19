package org.springframework.samples.dwarf.admin;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {

    List<Admin> findAll();

    public Admin findByName(String name);

}
