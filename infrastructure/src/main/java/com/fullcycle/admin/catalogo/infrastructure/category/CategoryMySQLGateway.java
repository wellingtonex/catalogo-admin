package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.category.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        return save(category);
    }

    @Override
    public void deleteById(final CategoryID id) {
        final String idValue = id.getValue();
        if (categoryRepository.existsById(idValue)) {
            categoryRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return categoryRepository.findById(id.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(Category category) {
        return save(category);
    }


    @Override
    public Pagination<Category> findAll(SearchQuery query) {
        final var page = PageRequest.of(query.page(), query.perPage(), Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        Specification<CategoryJpaEntity> specification = Optional.ofNullable(query.terms())
                .filter(str -> !str.isEmpty()).map(str ->
                        SpecificationUtils.<CategoryJpaEntity>like("name", str)
                                .or(SpecificationUtils.like("description", str))
                )
                .orElse(null);

        final var pageResult = this.categoryRepository.findAll(Specification.where(specification), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIDs) {
        final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();
        return this.categoryRepository.existsByIds(ids).stream()
                .map(CategoryID::from)
                .toList();
    }

    private Category save(Category category) {
        return categoryRepository.save(CategoryJpaEntity.from(category)).toAggregate();
    }
}
