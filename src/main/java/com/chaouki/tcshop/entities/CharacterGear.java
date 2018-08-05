package com.chaouki.tcshop.entities;

import javax.persistence.*;

@Entity
public class CharacterGear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Character character;

    private Integer slotId;

    @ManyToOne
    private ItemTemplate itemTemplate;
}
