package com.Test.JSON;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Data
@NoArgsConstructor
public class Team {

    private String teamName;

    private List<Player> players;

    public Team(String name) {
        this.teamName = name;
        this.players = new ArrayList<>();
    }

    public Team addPlayer(Player player) {
        this.players.add(player);
        return this;
    }
}
