package me.v1ctu.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private final UUID uuid;
    private final String name;
    private double balance;


}
