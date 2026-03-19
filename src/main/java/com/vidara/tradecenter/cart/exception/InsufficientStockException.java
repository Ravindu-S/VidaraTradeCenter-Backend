package com.vidara.tradecenter.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

  private String productName;
  private Integer requestedQuantity;
  private Integer availableStock;

  public InsufficientStockException(String message) {
    super(message);
  }

  public InsufficientStockException(String productName, Integer requestedQuantity, Integer availableStock) {
    super(String.format("Insufficient stock for product '%s'. Requested: %d, Available: %d",
        productName, requestedQuantity, availableStock));
    this.productName = productName;
    this.requestedQuantity = requestedQuantity;
    this.availableStock = availableStock;
  }

  public String getProductName() {
    return productName;
  }

  public Integer getRequestedQuantity() {
    return requestedQuantity;
  }

  public Integer getAvailableStock() {
    return availableStock;
  }
}
