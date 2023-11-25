package model;

import model.annotations.Id;
import model.annotations.MapToDatabase;

import java.util.Objects;

public class Order {
    @Id
    private Long id;
    @MapToDatabase(columnName = "customer_id")
    private Long customerId;
    @MapToDatabase(columnName = "employee_id")
    private Long employeeId;
    private Long gameId;
    private Integer amount;
    @MapToDatabase(columnName = "total_price")
    private Double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customerId, order.customerId) && Objects.equals(employeeId, order.employeeId) && Objects.equals(gameId, order.gameId) && Objects.equals(amount, order.amount) && Objects.equals(totalPrice, order.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, employeeId, gameId, amount, totalPrice);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                ", gameId=" + gameId +
                ", amount=" + amount +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
