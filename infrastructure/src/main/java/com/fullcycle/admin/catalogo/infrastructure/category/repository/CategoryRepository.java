package com.fullcycle.admin.catalogo.infrastructure.category.repository;

import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {
}
