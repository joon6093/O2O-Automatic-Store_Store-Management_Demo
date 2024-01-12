package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {
    @NotNull(message = "{postReadCondition.page.notNull}")
    @PositiveOrZero(message = "{postReadCondition.page.positiveOrZero}")
    private Integer page;

    @NotNull(message = "{postReadCondition.size.notNull}")
    @Positive(message = "{postReadCondition.size.positive}")
    private Integer size;

    private List<Long> categoryId = new ArrayList<>();
    private List<Long> memberId = new ArrayList<>();
}