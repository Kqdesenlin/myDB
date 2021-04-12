package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/9
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FakeNode {
    String name;

    List<FakeNode> children ;


}
