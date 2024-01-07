package org.springframework.samples.dwarf.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findAll();

    Optional<Message> findById(Integer id);

    @Query("SELECT m FROM Message m WHERE m.sentTime BETWEEN :a AND :b")
    List<Message> findBySentTimeRange(LocalDateTime a, LocalDateTime b);

}
