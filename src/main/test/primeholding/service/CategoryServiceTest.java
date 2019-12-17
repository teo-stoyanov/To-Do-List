package primeholding.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import primeholding.entities.Category;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class CategoryServiceTest {

    private CategoryService categoryService;
    private Category category;

    @Autowired
    CategoryServiceTest(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.category = new Category();
    }

    @BeforeEach
    public void setCategory() {
        this.category.setName("Test Category");
    }

    @Test
    public void serviceShouldPersistItemAndReturnCorrect() {
        Category registered = this.categoryService.register(this.category);
        Optional<Category> optionalItem = this.categoryService.findByProp(registered.getName());
        if (!optionalItem.isPresent()) {
            throw new NoSuchElementException();
        }

        Assertions.assertEquals(this.category.getName(), optionalItem.get().getName());
    }

    @Test
    public void serviceGetByIdShouldReturnCorrectList() {
        Category registered = this.categoryService.register(this.category);
        Optional<Category> optionalToDoList = this.categoryService.getById(registered.getId());
        if (!optionalToDoList.isPresent()) {
            throw new NoSuchElementException();
        }

        Assertions.assertEquals(this.category.getName(), optionalToDoList.get().getName());
    }

    @Test
    public void serviceDeleteByIdShouldRemoveEntity() {
        Category register = this.categoryService.register(this.category);

        this.categoryService.deleteById(register.getId());

        Assertions.assertFalse(this.categoryService.getById(register.getId()).isPresent());
    }
}