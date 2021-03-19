package com.domain.Entity.bTree;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xumg
 * @create 2020-10-15 9:12
 */
@Data
@AllArgsConstructor
public class Entry<K,V> {
    private K key;
    private V value;

    @Override
    public String toString() {
        return  "key=" + key +
                ", value=" + value ;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Entry() {
    }

}
