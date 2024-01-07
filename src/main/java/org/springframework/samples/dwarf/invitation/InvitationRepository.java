package org.springframework.samples.dwarf.invitation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Integer>{
    
    public List<Invitation> findAll();
    /*
     * public Invitation findById();
     */
    
   
}
   
