package com.example.fatoura.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "organization")
public class Organization {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  private String address;
}