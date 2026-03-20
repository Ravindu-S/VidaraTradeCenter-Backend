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
    User user = getUserById(userId);
    Cart cart = getOrCreateActiveCart(user);
    return cartMapper.toCartResponse(cart);
  }

  @Override
  public CartResponse addToCart(Long userId, AddToCartRequest request) {
    User user = getUserById(userId);
    Product product = getProductById(request.getProductId());

    validateStockAvailability(product, request.getQuantity());

    Cart cart = getOrCreateActiveCart(user);
    Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

    if (existingItem.isPresent()) {
      updateExistingCartItem(existingItem.get(), product, request.getQuantity());
    } else {
      addNewCartItem(cart, product, request.getQuantity());
    }

    return cartMapper.toCartResponse(cartRepository.save(cart));
  }

  @Override
  public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
    CartItem cartItem = getCartItemById(cartItemId);
    verifyCartOwnership(cartItem, userId);

    validateStockAvailability(cartItem.getProduct(), request.getQuantity());

    cartItem.setQuantity(request.getQuantity());
    cartItemRepository.save(cartItem);

    return cartMapper.toCartResponse(cartItem.getCart());
  }

  @Override
  public CartResponse removeCartItem(Long userId, Long cartItemId) {
    CartItem cartItem = getCartItemById(cartItemId);
    verifyCartOwnership(cartItem, userId);

    Cart cart = cartItem.getCart();
    cart.removeItem(cartItem);
    cartItemRepository.delete(cartItem);

    return cartMapper.toCartResponse(cart);
  }

  @Override
  public void clearCart(Long userId) {
    User user = getUserById(userId);
    Cart cart = getActiveCart(user);

    cartItemRepository.deleteByCartId(cart.getId());
    cart.getItems().clear();
    cartRepository.save(cart);
  }

  @Override
  @Transactional(readOnly = true)
  public CartResponse getActiveCart(Long userId) {
    User user = getUserById(userId);
    Cart cart = getActiveCart(user);
    return cartMapper.toCartResponse(cart);
  }

  // PRIVATE HELPER METHODS

  private User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
  }

  private Product getProductById(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
  }

  private CartItem getCartItemById(Long cartItemId) {
    return cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));
  }

  private Cart getOrCreateActiveCart(User user) {
    return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseGet(() -> cartRepository.save(new Cart(user)));
  }

  private Cart getActiveCart(User user) {
    return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
        .orElseThrow(() -> new CartNotFoundException(user.getId()));
  }

  private void validateStockAvailability(Product product, Integer requestedQuantity) {
    Integer availableStock = product.getStock() != null ? product.getStock() : 0;

    if (availableStock < requestedQuantity) {
      throw new InsufficientStockException(
          product.getName(),
          requestedQuantity,
          availableStock);
    }
  }

  private void verifyCartOwnership(CartItem cartItem, Long userId) {
    if (!cartItem.getCart().getUser().getId().equals(userId)) {
      throw new CartNotFoundException("Cart item does not belong to user");
    }
  }

  private void updateExistingCartItem(CartItem item, Product product, Integer additionalQuantity) {
    int newQuantity = item.getQuantity() + additionalQuantity;
    validateStockAvailability(product, newQuantity);
    item.setQuantity(newQuantity);
    cartItemRepository.save(item);
  }

  private void addNewCartItem(Cart cart, Product product, Integer quantity) {
    BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getBasePrice();
    CartItem newItem = new CartItem(cart, product, quantity, price);
    cart.addItem(newItem);
    cartItemRepository.save(newItem);
  }
}
