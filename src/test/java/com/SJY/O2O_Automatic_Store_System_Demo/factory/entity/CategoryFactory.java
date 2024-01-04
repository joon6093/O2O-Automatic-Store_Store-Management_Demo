package com.SJY.O2O_Automatic_Store_System_Demo.factory.entity;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;

public class CategoryFactory {

    public static Category createCategory() {
        return new Category("name", null);
    }

    public static Category createCategory(String name, Category parent) {
        Category child = new Category(name, parent);
        parent.addChildCategory(child);
        return child;

    }

    public static Category createCategoryWithName(String name) {
        return new Category(name, null);
    }
}