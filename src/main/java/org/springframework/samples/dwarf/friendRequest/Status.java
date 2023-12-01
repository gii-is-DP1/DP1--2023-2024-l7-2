package org.springframework.samples.dwarf.friendsRequest;

import org.springframework.samples.dwarf.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "status")
public class Status  extends NamedEntity {

}