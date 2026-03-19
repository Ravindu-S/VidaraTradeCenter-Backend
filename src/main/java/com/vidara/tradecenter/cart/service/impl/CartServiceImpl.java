package com.vidara.tradecenter.cart.service.impl;

import com.vidara.tradecenter.cart.dto.request.AddToCartRequest;
import com.vidara.tradecenter.cart.dto.request.UpdateCartItemRequest;
import com.vidara.tradecenter.cart.dto.response.CartResponse;
import com.vidara.tradecenter.cart.exception.CartNotFoundException;
import com.vidara.tradecenter.cart.exception.InsufficientStockException;
import com.vidara.tradecenter.cart.mapper.CartMapper;
import com.vidara.tradecenter.cart.model.Cart;
import com.vidara.tradecenter.cart.model.CartItem;
import com.vidara.tradecenter.cart.model.enums.CartStatus;
import com.vidara.tradecenter.cart.repository.CartItemRepository;
import com.vidara.tradecenter.cart.repository.CartRepository;
import com.vidara.tradecenter.cart.service.CartService;
import com.vidara.tradecenter.common.exception.ResourceNotFoundException;
import com.vidara.tradecenter.product.model.Product;
import com.vidara.tradecenter.product.repository.ProductRepository;
import com.vidara.tradecenter.user.model.User;
import com.vidara.tradecenter.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final CartMapper cartMapper;

  public CartServiceImpl(CartRepository cartRepository,
      CartItemRepository cartItemRepository,
      ProductRepository productRepository,
      UserRepository userRepository,
      CartMapper cartMapper) {
    this.cartRepository = cartRepository;
    this.cartItemRepository = cartItemRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.cartMapper = cartMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public CartResponse getOrCreateCart(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseGet(() -> {
          Cart newCart = new Cart(user);
          return cartRepository.save(newCart);
        });

    return cartMapper.toCartResponse(cart);
  }

  @Override
  public CartResponse addToCart(Long userId, AddToCartRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

    // Check stock availability
    if (product.getStock() == null || product.getStock() < request.getQuantity()) {
      throw new InsufficientStockException(
          product.getName(),
          request.getQuantity(),
          product.getStock() != null ? product.getStock() : 0);
    }

    // Get or create active cart
    Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseGet(() -> {
          Cart newCart = new Cart(user);
          return cartRepository.save(newCart);
        });

    // Check if product already in cart
    Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

    if (existingItem.isPresent()) {
      // Update quantity
      CartItem item = existingItem.get();
      int newQuantity = item.getQuantity() + request.getQuantity();

      // Validate new quantity against stock
      if (product.getStock() < newQuantity) {
        throw new InsufficientStockException(
            product.getName(),
            newQuantity,
            product.getStock());
      }

      item.setQuantity(newQuantity);
      cartItemRepository.save(item);
    } else {
      // Create new cart item
      BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getBasePrice();
      CartItem newItem = new CartItem(cart, product, request.getQuantity(), price);
      cart.addItem(newItem);
      cartItemRepository.save(newItem);
    }

    cart = cartRepository.save(cart);
    return cartMapper.toCartResponse(cart);
  }

  @Override
  public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

    // Verify cart belongs to user
    if (!cartItem.getCart().getUser().getId().equals(userId)) {
      throw new CartNotFoundException("Cart item does not belong to user");
    }

    Product product = cartItem.getProduct();

    // Validate stock
    if (product.getStock() == null || product.getStock() < request.getQuantity()) {
      throw new InsufficientStockException(
          product.getName(),
          request.getQuantity(),
          product.getStock() != null ? product.getStock() : 0);
    }

    cartItem.setQuantity(request.getQuantity());
    cartItemRepository.save(cartItem);

    Cart cart = cartItem.getCart();
    return cartMapper.toCartResponse(cart);
  }

  @Override
  public CartResponse removeCartItem(Long userId, Long cartItemId) {
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

    // Verify cart belongs to user
    if (!cartItem.getCart().getUser().getId().equals(userId)) {
      throw new CartNotFoundException("Cart item does not belong to user");
    }

    Cart cart = cartItem.getCart();
    cart.removeItem(cartItem);
    cartItemRepository.delete(cartItem);

    return cartMapper.toCartResponse(cart);
  }

  @Override
  public void clearCart(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseThrow(() -> new CartNotFoundException(userId));

    cartItemRepository.deleteByCartId(cart.getId());
    cart.getItems().clear();
    cartRepository.save(cart);
  }

  @Override
  @Transactional(readOnly = true)
  public CartResponse getActiveCart(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseThrow(() -> new CartNotFoundException(userId));

    return cartMapper.toCartResponse(cart);
  }
}
