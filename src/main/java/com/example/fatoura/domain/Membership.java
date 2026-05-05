package com.example.fatoura.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "membership")
@Getter
@Setter
public class Membership {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Organization organization;

  private String role;
}