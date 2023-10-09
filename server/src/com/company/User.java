package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class User{
    String username;
    String password;
    String bio = "Say something...";
    ArrayList<User> friends = new ArrayList<>();
    byte[] Image= null;
    Game game = new Game();

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", friends=" + friends +
                ", Image=" + Arrays.toString(Image) +
                ", game=" + game +
                '}';
    }
}
class Game{
    public int scores[];
    Game(){
        scores = new int[4];
    }
}