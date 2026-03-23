package com.vidara.tradecenter.cart.model;

import com.vidara.tradecenter.common.base.BaseEntity;
import com.vidara.tradecenter.product.model.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
    @Index(name = "idx_cart_item_product", columnList = "product_id")
})
public class CartItem extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "price_at_addition", precision = 10, scale = 2)
  private BigDecimal priceAtAddition;

  // CONSTRUCTORS

  public CartItem() {
  }

  public CartItem(Cart cart, Product product, Integer quantity, BigDecimal price) {
    this.cart = cart;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
    this.priceAtAddition = price;
  }

  // HELPER METHODS

  public BigDecimal getSubtotal() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }

  public boolean hasPriceChanged() {
    return priceAtAddition != null && !price.equals(priceAtAddition);
  }

  public BigDecimal getPriceDifference() {
    if (priceAtAddition == null) {
      return BigDecimal.ZERO;
    }
    return price.subtract(priceAtAddition);
  }

  // GETTERS AND SETTERS

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getPriceAtAddition() {
    return priceAtAddition;
  }

  public void setPriceAtAddition(BigDecimal priceAtAddition) {
    this.priceAtAddition = priceAtAddition;
  }
}
