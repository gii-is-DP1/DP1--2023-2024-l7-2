package org.springframework.samples.dwarf.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Integer> {

    public List<Invitation> findAll();


    public Optional<Invitation> findById(Integer id);

    @Query("SELECT fr FROM Invitation fr WHERE fr.sender = :u OR fr.receiver = :u")
    public List<Invitation> findByUser(User u);

}
   
