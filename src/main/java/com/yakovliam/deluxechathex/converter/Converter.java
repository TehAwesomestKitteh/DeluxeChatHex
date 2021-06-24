package com.yakovliam.deluxechathex.converter;

public interface Converter<K, V> {

    /**
     * Converts a k into a v
     *
     * @param k k
     * @return v
     */
    V convert(K k);
}
