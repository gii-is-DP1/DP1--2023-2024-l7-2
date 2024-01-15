package org.springframework.samples.dwarf.chat;

import java.util.List;

import org.springframework.samples.dwarf.model.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Chat extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Message> messages;

}
