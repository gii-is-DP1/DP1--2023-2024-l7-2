package org.springframework.samples.dwarf.chat;

import java.time.LocalDateTime;

import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message extends BaseEntity {

    @NotNull
    @NotBlank
    String text;

    @NotNull
    LocalDateTime sentTime;

    @ManyToOne(optional = false)
    User sender;
}
