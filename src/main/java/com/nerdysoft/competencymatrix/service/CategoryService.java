package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final TopicService topicService;

    @Autowired
    public CategoryService(CategoryRepository repository, TopicService topicService) {
        this.repository = repository;
        this.topicService = topicService;
    }

    public Category createCategory(Category category){
        return repository.save(category);
    }

    public Optional<Category> findCategoryById(Long id){
        return repository.findById(id);
    }

    public List<Category> findAllCategories(){
        return repository.findAll();
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryData){
        Optional<Category> maybeCategory = repository.findById(id);

        if (maybeCategory.isPresent()){
            Category category = maybeCategory.get();

            category.setName(categoryData.getName());
            category.setDescription(categoryData.getDescription());
            category.setType(categoryData.getType());
            category.setPriority(categoryData.getPriority());

            return category;
        }else
            return new Category();
    }

    public boolean deleteCategory(Long id){
        Optional<Category> byId = repository.findById(id);

        if (byId.isPresent()){
            Category category = byId.get();
            repository.delete(category);
        }
        Optional<Category> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public Category addTopicToCategory(Long categoryId, Long topicId){
        Optional<Category> maybeCategory = findCategoryById(categoryId);
        Optional<Topic> maybeTopic = topicService.findTopicById(topicId);
        if (maybeCategory.isPresent() && maybeTopic.isPresent()){
            Category category = maybeCategory.get();
            Topic topic = maybeTopic.get();

            category.addTopic(topic);

            return category;
        }else
            return new Category();
    }

    @Transactional
    public Category removeTopicFromCategory(Long categoryId, Long topicId){
        Optional<Category> maybeCategory = findCategoryById(categoryId);
        Optional<Topic> maybeTopic = topicService.findTopicById(topicId);
        if (maybeCategory.isPresent() && maybeTopic.isPresent()){
            Category category = maybeCategory.get();
            Topic topic = maybeTopic.get();

            category.removeTopic(topic);

            return category;
        }else
            return new Category();
    }
}
