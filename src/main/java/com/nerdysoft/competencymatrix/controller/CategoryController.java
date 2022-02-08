package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.dto.CategoryDto;
import com.nerdysoft.competencymatrix.entity.dto.LevelDto;
import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import com.nerdysoft.competencymatrix.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/category")
@PreAuthorize("hasAuthority('CAN_ALL')")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        Category category = categoryService.createCategory(Category.from(categoryDto));
        return new ResponseEntity<>(CategoryDto.from(category), OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        List<Category> allCategories = categoryService.findAllCategories();
        List<CategoryDto> categoryDtoList = allCategories.stream()
                .map(CategoryDto::from).collect(Collectors.toList());

        if (categoryDtoList.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOneCategory(@PathVariable Long id){
        Optional<Category> maybeCategory = categoryService.findCategoryById(id);

        if (maybeCategory.isPresent()){
            Category category = maybeCategory.get();

            return new ResponseEntity<>(CategoryDto.from(category), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto){
        Category category = categoryService.updateCategory(id, Category.from(categoryDto));

        if(Objects.nonNull(category.getId())){
            return new ResponseEntity<>(CategoryDto.from(category), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> removeCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{categoryId}/add/{topicId}")
    public ResponseEntity<CategoryDto> addTopicToCategory(@PathVariable Long categoryId, @PathVariable Long topicId){
        Category category = categoryService.addTopicToCategory(categoryId, topicId);

        if(Objects.nonNull(category.getId())){
            return new ResponseEntity<>(CategoryDto.from(category), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{categoryId}/delete/{topicId}")
    public ResponseEntity<CategoryDto> deleteTopicFromCategory(@PathVariable Long categoryId, @PathVariable Long topicId){
        Category category = categoryService.removeTopicFromCategory(categoryId, topicId);

        if(Objects.nonNull(category.getId())){
            return new ResponseEntity<>(CategoryDto.from(category), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
