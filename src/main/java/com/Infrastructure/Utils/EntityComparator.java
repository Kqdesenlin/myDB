package com.Infrastructure.Utils;

import com.domain.Entity.bTree.Entry;

import java.util.Comparator;
import java.util.List;

public class EntityComparator implements Comparator<Entry<Integer, List<String>>> {
    @Override
    public int compare(Entry<Integer, List<String>> o1, Entry<Integer, List<String>> o2) {
        return o1.getKey()-o2.getKey();
    }
}
