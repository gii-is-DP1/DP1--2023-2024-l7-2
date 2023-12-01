package org.springframework.samples.dwarf.friendRequest;

import org.springframework.samples.dwarf.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "friendRequestStatus")
public class Status  extends NamedEntity {

}