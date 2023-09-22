package com.renan.webstore.controller;

import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.StoreService;
import com.renan.webstore.dto.CategoryDTO;
import com.renan.webstore.dto.ProductAdDTO;
import com.renan.webstore.dto.ProductDTO;
import com.renan.webstore.dto.mapper.CategoryDTOMapper;
import com.renan.webstore.dto.mapper.ProductAdDTOMapper;
import com.renan.webstore.dto.mapper.ProductDTOMapper;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/public")
@RequiredArgsConstructor
public class StoreController {
    
    private final StoreService storeService;
    private final CategoryDTOMapper categoryDTOMapper;
    private final ProductAdDTOMapper productAdDTOMapper;
    private final ProductDTOMapper productDTOMapper;
    
    @GetMapping("/categories")
    public List<CategoryDTO> getCategories(){
        return storeService.getCategories();
    }
    
    @GetMapping("/ads")
    public List<ProductAdDTO> getAds(){
        return storeService.getProductAds().stream().map(productAdDTOMapper).toList();
    }
    
    @GetMapping("/products/initial")
    public List<ProductDTO> getProductsInitialPage(){
        return storeService.getProductsInitialPage().stream().map(productDTOMapper).toList();
    }
    
    @GetMapping("/image/{id}")
    public String getImage(@PathParam("id") Integer id){
        return storeService.getImage(id);
    }
    
    @GetMapping("/products/{category}")
    public List<ProductDTO> getProductsByCategory(@PathParam("category") Integer categoryId){
        return storeService.getProductsByCategory(categoryId).stream().map(productDTOMapper).toList();
    }
    
    @GetMapping("/products/related/{productId}")
    public List<ProductDTO> getRelatedProducts(@PathParam("productId") Integer productID) {
        return storeService.getRelatedProducts(productID).stream().map(productDTOMapper).toList();
    }
    
    @GetMapping("/products/{productId}")
    public List<Integer> getProductImages(@PathParam("productId") Integer productID) {
        return storeService.getProductImages(productID);
    }
    
}
