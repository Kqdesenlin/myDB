package com.domain.Entity.result;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SelectResult{
    public List<String> rules;
    public List<List<String>> items;
}
