package com.Test.JSON;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Data
@NoArgsConstructor
public class Player {
    private String name;


    public Player(String name) {
        this.name = name;
    }
}
