package com.renan.webstore.business.impl;

import com.renan.webstore.business.BusinessException;
import com.renan.webstore.business.StoreService;
import com.renan.webstore.dto.CategoryDTO;
import com.renan.webstore.dto.ProductAdDTO;
import com.renan.webstore.dto.ProductDTO;
import com.renan.webstore.dto.mapper.CategoryDTOMapper;
import com.renan.webstore.dto.mapper.ProductAdDTOMapper;
import com.renan.webstore.dto.mapper.ProductDTOMapper;
import com.renan.webstore.model.Category;
import com.renan.webstore.model.Product;
import com.renan.webstore.model.ProductAd;
import com.renan.webstore.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductAdRepository productAdRepository;
    private final ImageRepository imageRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRelatedRepository productRelatedRepository;
    private final CategoryDTOMapper categoryDTOMapper;
    private final ProductDTOMapper productDTOMapper;
    private final ProductAdDTOMapper productAdDTOMapper;
    
    public List<CategoryDTO> getCategories() throws BusinessException {
        return categoryDTOMapper.convertCategoriesToDTO(categoryRepository.findAll());
    }
    
    public List<ProductAdDTO> getProductAds() throws BusinessException {
        return productAdRepository.getActiveAds().stream().map(productAdDTOMapper).toList();
    }
    
    public List<ProductDTO> getProductsInitialPage() throws BusinessException {
        return productRepository.getProductsInitialPage().stream().map(productDTOMapper).toList();
    }
    
    public List<ProductDTO> getProductsByCategory(Integer categoryID) throws BusinessException {
        //TODO Validate categoryID
        return productCategoryRepository.getProductsByCategory(categoryID).stream().map(productDTOMapper).toList();
    }
    
    public List<ProductDTO> getRelatedProducts(Integer productID) throws BusinessException {
        //TODO Validate productID
        return productRelatedRepository.getRelatedProducts(productID).stream().map(productDTOMapper).toList();
    }
    
    public String getImage(Integer id) throws BusinessException {
        //TODO Validate id
        //TODO Throw Exception if not found
        return imageRepository.getImage(id);
    }
    
    public List<Integer> getProductImages(Integer productId) throws BusinessException {
        //TODO Validate id
        //TODO Throw Exception if not found
        return productImageRepository.getImagesFromProduct(productId);
    }
    
}
