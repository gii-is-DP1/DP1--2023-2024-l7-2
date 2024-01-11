package org.springframework.samples.dwarf.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService {
    private InvitationRepository repo;

    @Autowired
    public InvitationService(InvitationRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Invitation> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Invitation findById(Integer id) {
        Optional<Invitation> invi = repo.findById(id);

        if (invi.isPresent()) {
            return invi.get();
        }
        return null;
    }

    @Transactional
    public Invitation saveInvitation(Invitation invi) throws DataAccessException {
        return repo.save(invi);
    }

    @Transactional
    public void deleteInvitation(Integer id) throws DataAccessException {
        repo.deleteById(id);
    }

    @Transactional
    public List<Game> findGamesInvitedTo(User u) {
        return repo.findGamesInvitedTo(u);
    }
}
