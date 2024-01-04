package com.SJY.O2O_Automatic_Store_System_Demo.entity.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Category> children = new HashSet<>();

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }
    public void addChildCategory(Category child) {
        children.add(child);
    }

    public void removeChildCategory(Category child) {
        children.remove(child);
    }
}
