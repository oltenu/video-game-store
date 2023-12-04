package model.builder;

import model.JoinedOrder;

public class JointOrderBuilder {
    private final JoinedOrder joinedOrder;

    public JointOrderBuilder(){
        joinedOrder = new JoinedOrder();
    }

    public JointOrderBuilder setId(Long id){
        joinedOrder.setId(id);

        return this;
    }

    public JointOrderBuilder setGameName(String gameName){
        joinedOrder.setGameName(gameName);

        return this;
    }

    public JointOrderBuilder setCustomerUsername(String customerUsername){
        joinedOrder.setCustomerUsername(customerUsername);

        return this;
    }

    public JointOrderBuilder setEmployeeUsername(String employeeUsername){
        joinedOrder.setEmployeeUsername(employeeUsername);

        return this;
    }

    public JointOrderBuilder setAmount(Integer amount){
        joinedOrder.setAmount(amount);

        return this;
    }

    public JointOrderBuilder setTotalPrice(Double totalPrice){
        joinedOrder.setTotalPrice(totalPrice);

        return this;
    }

    public JoinedOrder build(){
        return joinedOrder;
    }
}
