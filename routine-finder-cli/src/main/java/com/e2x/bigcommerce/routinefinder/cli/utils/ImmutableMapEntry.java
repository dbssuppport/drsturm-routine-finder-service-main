package com.e2x.bigcommerce.routinefinder.cli.utils;

import java.util.Map;

public class ImmutableMapEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public ImmutableMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return this.value;
    }
}
