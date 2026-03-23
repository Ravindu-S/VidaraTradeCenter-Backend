package com.vidara.tradecenter.cart.mapper;

import com.vidara.tradecenter.cart.dto.response.CartItemResponse;
import com.vidara.tradecenter.cart.dto.response.CartResponse;
import com.vidara.tradecenter.cart.model.Cart;
import com.vidara.tradecenter.cart.model.CartItem;
import com.vidara.tradecenter.product.model.Product;
import com.vidara.tradecenter.product.model.ProductImage;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

  public CartResponse toCartResponse(Cart cart) {
    CartResponse response = new CartResponse();
    response.setId(cart.getId());
    response.setUserId(cart.getUser().getId());
    response.setStatus(cart.getStatus().name());
    response.setItems(cart.getItems().stream()
        .map(this::toCartItemResponse)
        .collect(Collectors.toList()));
    response.setTotalAmount(cart.getTotalAmount());
    response.setTotalItems(cart.getTotalItems());
    return response;
  }

  public CartItemResponse toCartItemResponse(CartItem cartItem) {
    CartItemResponse response = new CartItemResponse();
    response.setId(cartItem.getId());

    Product product = cartItem.getProduct();
    response.setProductId(product.getId());
    response.setProductName(product.getName());
    response.setProductSlug(product.getSlug());

    // Get primary image if available
    if (product.getImages() != null && !product.getImages().isEmpty()) {
      ProductImage primaryImage = product.getImages().stream()
          .filter(ProductImage::isPrimary)
          .findFirst()
          .orElse(product.getImages().get(0));
      response.setProductImage(primaryImage.getImageUrl());
    }

    response.setPrice(cartItem.getPrice());
    response.setQuantity(cartItem.getQuantity());
    response.setSubtotal(cartItem.getSubtotal());
    response.setPriceAtAddition(cartItem.getPriceAtAddition());
    response.setPriceChanged(cartItem.hasPriceChanged());

    return response;
  }
}
