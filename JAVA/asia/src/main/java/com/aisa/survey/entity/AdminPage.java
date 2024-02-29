package com.aisa.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AdminPage {

    @Id
    private String date;

    private int visitCount;

    private int submitCount;



}
