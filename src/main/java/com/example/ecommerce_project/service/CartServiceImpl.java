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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
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

        products.setQuantity(products.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (products.getSpecialPrice()*quantity));

        cartRepository.save(cart);

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

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.size() == 0){
            throw new ResourceNotFoundException("Cart", "id", carts.get(0).getCartId());
        }
        List<CartDTO> cartDTOList = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream().map(p -> modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOList;
    }

    @Override
    public CartDTO getCarts(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null){
            throw new ResourceNotFoundException("Cart", "id", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(cartItem -> cartItem.getProduct().setQuantity(cartItem.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart", "id", cartId));
        Product products = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        if (products.getQuantity() == 0){
            throw new APIException("Product quantity is 0");
        }
        if (products.getQuantity() < quantity){
            throw new APIException("Product quantity less than quantity");
        }

        CartItem newCartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (newCartItem == null) {
            throw new APIException("Product not exists");
        }

        int newQuantity = newCartItem.getQuantity() + quantity;
        if (newQuantity < 0){
            throw new APIException("Product quantity less than 0");
        }
        if (newQuantity == 0){
            deleteProductFromCart(productId, cartId);
        } else {
            newCartItem.setProductPrice(products.getSpecialPrice());
            newCartItem.setQuantity(newCartItem.getQuantity() + quantity);
            newCartItem.setDiscount(products.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (newCartItem.getProductPrice()*quantity));
            cartRepository.save(cart);
        }
        CartItem updatedCartItem = cartItemRepository.save(newCartItem);
        if (updatedCartItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItemList = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart", "id", cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null){
            throw new ResourceNotFoundException("Cart", "id", cartId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        return "Product " + productId + " has been deleted";
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart", "id", cartId));
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null){
            throw new APIException("Product not exists " + product.getProductName());
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getProductPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.save(cartItem);
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
