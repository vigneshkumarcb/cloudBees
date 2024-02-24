package com.train.services.ticketing.utils;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class SizeLimitedLinkedHashSet<T> extends LinkedHashSet<T> {
    private final int maxSize;
    private final List<T> orderList;
    private final int sectionSize;
    private final LinkedHashSet<T> sectionA;
    private final LinkedHashSet<T> sectionB;
    private int flag;


    public SizeLimitedLinkedHashSet(int maxSize, int sectionSize) {
        super();
        this.maxSize = maxSize;
        this.orderList = new LinkedList<>();
        this.sectionSize = sectionSize;

        // Initialize sections
        this.sectionA = new LinkedHashSet<>();
        this.sectionB = new LinkedHashSet<>();
    }

    @Override
    public boolean add(T element) {
        if (size() >= maxSize) {
            // If the size exceeds the limit, do not add the element
            return false;
        }

        boolean added = super.add(element);
        if (added) {
            orderList.add(element);
            if (orderList.size() < sectionSize) {
                sectionA.add(element);
            } else {
                sectionB.add(element);

            }
        } else {
            setListFlag(1);
        }

        return added;
    }

    public int getListFlag() {
        return flag;
    }

    public void setListFlag(int flag) {
        this.flag = flag;
    }

    public List<T> getOrderList() {
        return new LinkedList<>(orderList);
    }

    public LinkedHashSet<T> getSectionA() {
        return new LinkedHashSet<>(sectionA);
    }

    public LinkedHashSet<T> getSectionB() {
        return new LinkedHashSet<>(sectionB);
    }

    public LinkedList<T> getListOfSectionA() {
        return new LinkedList<>(sectionA);
    }

    public LinkedList<T> getListOfSectionB() {
        return new LinkedList<>(sectionB);
    }

    public void removeFromSectionA(T user) {
        sectionA.remove(user);
        orderList.remove(user);
    }

    public void removeFromSectionB(T user) {
        sectionB.remove(user);
        orderList.remove(user);
    }

    public void modifySeatInOrderedList(T user) {
        orderList.remove(user);
        orderList.add(user);
    }

    public void modifySeatInSectionA(T user) {
        sectionA.remove(user);
        sectionA.add(user);
    }

    public void modifySeatInSectionB(T user) {
        sectionB.remove(user);
        sectionB.add(user);
    }
}