package org.springframework.samples.petclinic.mainboard;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainBoardRepository extends CrudRepository <MainBoard, Integer>{

    List <MainBoard> findAll();
    
}
