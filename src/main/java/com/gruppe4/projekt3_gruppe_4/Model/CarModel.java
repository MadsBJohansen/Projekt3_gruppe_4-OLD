package com.gruppe4.projekt3_gruppe_4.Model;

public class CarModel {

    private int carModelId;
    private String name;
    private int modelYear;
    private int carBrandId;
    private String brandName;

    public CarModel(int carModelId, String name,
                    int modelYear, int carBrandId, String brandName) {
        this.carModelId = carModelId;
        this.name = name;
        this.modelYear = modelYear;
        this.carBrandId = carBrandId;
        this.brandName = brandName;
    }

    public int getCarModelId() {
        return carModelId;
    }

    public String getName() {
        return name;
    }

    public int getModelYear() {
        return modelYear;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getCarBrandId() {
        return carBrandId;
    }
}
