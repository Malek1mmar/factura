package com.example.fatoura.domain;

import lombok.Data;

@Data
public class CreateOrganizationRequest {
  private String name;
  private String address;
}