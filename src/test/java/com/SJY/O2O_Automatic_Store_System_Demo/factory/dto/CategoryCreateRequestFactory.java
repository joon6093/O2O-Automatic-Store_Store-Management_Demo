package com.SJY.O2O_Automatic_Store_System_Demo.factory.dto;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateRequest;
public class CategoryCreateRequestFactory {
    public static CategoryCreateRequest createCategoryCreateRequest() {
        return new CategoryCreateRequest("category", null);
    }

    public static CategoryCreateRequest createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequest(name, null);
    }

    public static CategoryCreateRequest createCategoryCreateRequestWithParentId(Long parentId) {
        return new CategoryCreateRequest("category", parentId);
    }
}