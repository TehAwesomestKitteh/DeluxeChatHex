package com.yakovliam.deluxechathex.util;

public class Triple<L, C, R> {

    L left;

    C center;

    R right;

    public Triple(L left, C center, R right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public Triple<L, C, R> setLeft(L left) {
        this.left = left;
        return this;
    }

    public C getCenter() {
        return center;
    }

    public Triple<L, C, R> setCenter(C center) {
        this.center = center;
        return this;
    }

    public R getRight() {
        return right;
    }

    public Triple<L, C, R> setRight(R right) {
        this.right = right;
        return this;
    }
}
