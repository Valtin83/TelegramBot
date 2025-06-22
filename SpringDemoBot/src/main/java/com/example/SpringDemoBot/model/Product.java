package com.example.SpringDemoBot.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private List<Double> lengths;

    public Product() {
        this.lengths = new ArrayList<>();
    }

    public void addLength(double length) {
        lengths.add(length);
    }

    public double calculateTotalLengthWithWaste() {
        double total = 0;
        double waste = 40; // отходы в мм
        for (double length : lengths) {
            total += (length + waste); // добавляем отходы к длине
        }
        return total;
    }

    public double getTotalLengthInMeters() {
        return calculateTotalLengthWithWaste() / 1000; // преобразуем в метры
    }
}