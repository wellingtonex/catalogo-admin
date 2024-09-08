package com.fullcycle.admin.catalogo.infrastructure.category;


import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(category);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.isActive(), actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());

        final var savedCategoryOptional = categoryRepository.findById(category.getId().getValue());
        assertTrue(savedCategoryOptional.isPresent());
        final var savedCategory = savedCategoryOptional.get();
        assertEquals(1, categoryRepository.count());
        assertEquals(expectedName, savedCategory.getName());
        assertEquals(expectedDescription, savedCategory.getDescription());
        assertEquals(category.getId().getValue(), savedCategory.getId());
        assertEquals(category.isActive(), savedCategory.isActive());
        assertEquals(category.getCreatedAt(), savedCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), savedCategory.getUpdatedAt());
        assertNull(savedCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnNewCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Film", null, expectedIsActive);
        assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryJpaEntity.from(category));
        assertEquals(1, categoryRepository.count());
        final var actualInvalidCategory = categoryRepository.findById(category.getId().getValue()).get();
        assertEquals(category.getName(), actualInvalidCategory.getName());
        assertEquals(category.getDescription(), actualInvalidCategory.getDescription());
        assertEquals(category.getId().getValue(), actualInvalidCategory.getId());
        assertEquals(category.isActive(), actualInvalidCategory.isActive());
        assertEquals(category.getCreatedAt(), actualInvalidCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualInvalidCategory.getUpdatedAt());
        assertNull(actualInvalidCategory.getDeletedAt());

        final var updatedCategory = category.clone().update(expectedName, expectedDescription);
        final var actualCategory = categoryGateway.update(updatedCategory);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.isActive(), actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());

        final var savedCategoryOptional = categoryRepository.findById(category.getId().getValue());
        assertTrue(savedCategoryOptional.isPresent());
        final var savedCategory = savedCategoryOptional.get();
        assertEquals(1, categoryRepository.count());
        assertEquals(expectedName, savedCategory.getName());
        assertEquals(expectedDescription, savedCategory.getDescription());
        assertEquals(category.getId().getValue(), savedCategory.getId());
        assertEquals(category.isActive(), savedCategory.isActive());
        assertEquals(category.getCreatedAt(), savedCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(savedCategory.getUpdatedAt()));
        assertNull(savedCategory.getDeletedAt());
    }


    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("Filmes", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("invalid"));

        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryJpaEntity.from(category));
        assertEquals(1, categoryRepository.count());

        final var savedCategoryOptional = categoryGateway.findById(category.getId());
        assertTrue(savedCategoryOptional.isPresent());
        final var savedCategory = savedCategoryOptional.get();
        assertEquals(1, categoryRepository.count());
        assertEquals(expectedName, savedCategory.getName());
        assertEquals(expectedDescription, savedCategory.getDescription());
        assertEquals(category.getId(), savedCategory.getId());
        assertEquals(category.isActive(), savedCategory.isActive());
        assertEquals(category.getCreatedAt(), savedCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), savedCategory.getUpdatedAt());
        assertNull(savedCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        final var savedCategoryOptional = categoryGateway.findById(CategoryID.from("123"));
        assertFalse(savedCategoryOptional.isPresent());

    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;


        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPagePaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoryRepository.count());

        final var queryPageZero = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResultPageZero = categoryGateway.findAll(queryPageZero);

        assertEquals(expectedPage, actualResultPageZero.currentPage());
        assertEquals(expectedPerPage, actualResultPageZero.perPage());
        assertEquals(expectedTotal, actualResultPageZero.total());
        assertEquals(expectedPerPage, actualResultPageZero.items().size());
        assertEquals(documentarios.getId(), actualResultPageZero.items().get(0).getId());

        final var queryPageOne = new CategorySearchQuery(1, 1, "", "name", "asc");
        final var actualResultPageOne = categoryGateway.findAll(queryPageOne);

        assertEquals(1, actualResultPageOne.currentPage());
        assertEquals(expectedPerPage, actualResultPageOne.perPage());
        assertEquals(expectedTotal, actualResultPageOne.total());
        assertEquals(expectedPerPage, actualResultPageOne.items().size());
        assertEquals(filmes.getId(), actualResultPageOne.items().get(0).getId());

        final var queryPageTwo = new CategorySearchQuery(2, 1, "", "name", "asc");
        final var actualResultPageTwo = categoryGateway.findAll(queryPageTwo);

        assertEquals(2, actualResultPageTwo.currentPage());
        assertEquals(expectedPerPage, actualResultPageTwo.perPage());
        assertEquals(expectedTotal, actualResultPageTwo.total());
        assertEquals(expectedPerPage, actualResultPageTwo.items().size());
        assertEquals(series.getId(), actualResultPageTwo.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(filmes.getId(), actualResult.items().get(0).getId());
    }
}
