package com.SJY.O2O_Automatic_Store_System_Demo.entity.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private final Set<Category> children = new HashSet<>();

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChildCategory(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChildCategory(Category child) {
        children.remove(child);
        child.setParent(null);
    }
}
