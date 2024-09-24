package net.azisaba.namechange.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHolder {
    List<String> playerrequest = new ArrayList<>();
    Map<Player,List<String>> requestmap = new HashMap<>();

    public static void addRequestMap(Player p,String requestname){
        
    }
}
