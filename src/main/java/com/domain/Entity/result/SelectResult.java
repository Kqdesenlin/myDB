package com.domain.Entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectResult{
    public List<String> rules;
    public List<List<String>> items;
}
