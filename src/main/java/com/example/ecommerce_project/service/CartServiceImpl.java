package com.example.ecommerce_project.service;

import com.example.ecommerce_project.exceptions.APIException;
import com.example.ecommerce_project.exceptions.ResourceNotFoundException;
import com.example.ecommerce_project.model.Cart;
import com.example.ecommerce_project.model.CartItem;
import com.example.ecommerce_project.model.Product;
import com.example.ecommerce_project.payload.CartDTO;
import com.example.ecommerce_project.payload.ProductDTO;
import com.example.ecommerce_project.repositories.CartItemRepository;
import com.example.ecommerce_project.repositories.CartRepository;
import com.example.ecommerce_project.repositories.ProductRepository;
import com.example.ecommerce_project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();
        Product products = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem != null) {
            throw new APIException("Product already exists");
        }
        if (products.getQuantity() == 0){
            throw new APIException("Product quantity is 0");
        }
        if (products.getQuantity() < quantity){
            throw new APIException("Product quantity less than quantity");
        }
        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(products);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(products.getDiscount());
        newCartItem.setProductPrice(products.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (products.getSpecialPrice()*quantity));

        cartRepository.save(cart);

        products.setQuantity(products.getQuantity());

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItemList = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice((0.00));
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
