package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdate_shouldReturnCategoryId() {

        final var category = Category.newCategory("Film", null, true);
        var expectedId = category.getId();

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var command =
                UpdateCategoryCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdate_shouldReturnDomainException() {

        final var category = Category.newCategory("Film", null, true);
        var expectedId = category.getId();

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var command =
                UpdateCategoryCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        expectedIsActive);


        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertNotNull(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, never()).update(any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {

        final var category = Category.newCategory("Film", null, true);
        var expectedId = category.getId();

        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;


        final var command =
                UpdateCategoryCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertNotNull(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidID_whenCallsUpdate_shouldReturnDomainException() {

        var expectedId = "123";

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var command =
                UpdateCategoryCommand.with(
                        expectedId,
                        expectedName,
                        expectedDescription,
                        expectedIsActive);


        final var expectedErrorMessage ="Category with id %s was not found.".formatted(expectedId);
        final var expectedErrorCount = 1;

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var domainException = assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, domainException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, domainException.getErrors().size());

        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(categoryGateway, never()).update(any());
    }
}
