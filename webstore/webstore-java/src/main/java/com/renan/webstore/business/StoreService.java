package com.renan.webstore.business;

import com.renan.webstore.dto.CategoryDTO;
import com.renan.webstore.dto.ProductAdDTO;
import com.renan.webstore.dto.ProductDTO;
import com.renan.webstore.model.Category;
import com.renan.webstore.model.Product;
import com.renan.webstore.model.ProductAd;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface StoreService {
    
    List<CategoryDTO> getCategories() throws BusinessException;
    
    List<ProductAdDTO> getProductAds() throws BusinessException;
    
    List<ProductDTO> getProductsInitialPage() throws BusinessException;
    
    List<ProductDTO> getProductsByCategory(Integer categoryID) throws BusinessException;
    
    List<ProductDTO> getRelatedProducts(Integer productID) throws BusinessException;
    
    String getImage(Integer id) throws BusinessException;
    
    List<Integer> getProductImages(Integer productId) throws BusinessException;
    
    
}
