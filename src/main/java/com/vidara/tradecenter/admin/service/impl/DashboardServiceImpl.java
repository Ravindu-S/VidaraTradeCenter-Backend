package com.vidara.tradecenter.admin.service.impl;

import com.vidara.tradecenter.admin.service.DashboardService;
<<<<<<< HEAD
import com.vidara.tradecenter.user.repository.UserRepository;
// TODO: Uncomment when ProductRepository is available
// import com.vidara.tradecenter.product.repository.ProductRepository;
// TODO: Uncomment when CategoryRepository is available
// import com.vidara.tradecenter.category.repository.CategoryRepository;
=======
import com.vidara.tradecenter.product.repository.CategoryRepository;
import com.vidara.tradecenter.product.repository.ProductRepository;
import com.vidara.tradecenter.user.repository.UserRepository;
>>>>>>> feature/product
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
<<<<<<< HEAD
    // TODO: Inject when ProductRepository is available
    // private final ProductRepository productRepository;
    // TODO: Inject when CategoryRepository is available
    // private final CategoryRepository categoryRepository;

    public DashboardServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        // TODO: Add ProductRepository and CategoryRepository to constructor
=======
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DashboardServiceImpl(UserRepository userRepository,
                                ProductRepository productRepository,
                                CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
>>>>>>> feature/product
    }


    // ==================== COUNT QUERIES ====================

    @Override
    public long getTotalProducts() {
<<<<<<< HEAD
        // TODO: Replace with productRepository.count() when ProductRepository is available
        return 0;
=======
        return productRepository.count();
>>>>>>> feature/product
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalCategories() {
<<<<<<< HEAD
        // TODO: Replace with categoryRepository.count() when CategoryRepository is available
        return 0;
=======
        return categoryRepository.count();
>>>>>>> feature/product
    }


    // ==================== AGGREGATED STATS ====================

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalProducts", getTotalProducts());
        stats.put("totalUsers", getTotalUsers());
        stats.put("totalCategories", getTotalCategories());

        // Placeholder for future stats
        stats.put("totalOrders", 0L);    // Coming Soon - Sprint 2
        stats.put("totalRevenue", 0.0);  // Coming Soon - Sprint 2

        return stats;
    }
}
