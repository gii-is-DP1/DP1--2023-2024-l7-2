package org.springframework.samples.petclinic.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerRepository;
import org.springframework.samples.petclinic.statistic.Achievement;
import org.springframework.samples.petclinic.statistic.AchievementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class AdminService {
    
    AdminRepository repo;

    @Autowired
    public AdminService(AdminRepository repo){
        this.repo=repo;
    }

    @Transactional(readOnly = true)
    List<Admin> getAdmin(){
        return repo.findAll();
    }
    
    @Transactional(readOnly = true)
    public Admin getById(int id){
        Optional<Admin> result=repo.findById(id);
        return result.isPresent()?result.get():null;
    }

    @Transactional
    public Admin saveAdmin(@Valid Admin newAdmin) {
        return repo.save(newAdmin);
    }

    
    @Transactional
    public void deleteAdminById(int id){
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Admin getAdminByName(String name){
        return repo.findByName(name);
    }
}
