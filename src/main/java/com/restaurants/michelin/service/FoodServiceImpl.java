package com.restaurants.michelin.service;

import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.FoodStatus;
import com.restaurants.michelin.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService<Food>{
    @Autowired
    FoodRepository foodRepository;
    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    @Override
    public void save(Food food) {
        food.setStatus(FoodStatus.Còn_bán);
        foodRepository.save(food);
    }
    @Override
    public Food findById(Integer id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        foodRepository.delete(findById(id));
    }

    @Override
    public List<Food> searchByName(String keyword) {
        return foodRepository.searchByKeyword(keyword);
    }

    @Override
    public List<Food> findAllByStatusOrderByIdFoodDesc(FoodStatus status) {
        return foodRepository.findAllByStatusOrderByIdFoodDesc(status);
    }
    @Override
    public void markAsSoldOut(Integer id) {
        Food food = findById(id); // hoặc foodRepository.findById(id).orElseThrow(...)
        food.setStatus(FoodStatus.Hết_bán); // nếu bạn dùng enum
        foodRepository.save(food); // cập nhật lại
    }

}
