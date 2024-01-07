package org.springframework.samples.dwarf.chat;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {

    List<Chat> findAll();

    Optional<Chat> findById(Integer id);

}
