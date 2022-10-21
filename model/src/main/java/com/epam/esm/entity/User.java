package com.epam.esm.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user")
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Order> orders = new ArrayList<>();
}
