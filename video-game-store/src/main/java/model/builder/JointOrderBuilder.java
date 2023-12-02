package model.builder;

import model.JointOrder;

public class JointOrderBuilder {
    private final JointOrder jointOrder;

    public JointOrderBuilder(){
        jointOrder = new JointOrder();
    }

    public JointOrderBuilder setId(Long id){
        jointOrder.setId(id);

        return this;
    }

    public JointOrderBuilder setGameName(String gameName){
        jointOrder.setGameName(gameName);

        return this;
    }

    public JointOrderBuilder setCustomerUsername(String customerUsername){
        jointOrder.setCustomerUsername(customerUsername);

        return this;
    }

    public JointOrderBuilder setEmployeeUsername(String employeeUsername){
        jointOrder.setEmployeeUsername(employeeUsername);

        return this;
    }

    public JointOrderBuilder setAmount(Integer amount){
        jointOrder.setAmount(amount);

        return this;
    }

    public JointOrderBuilder setTotalPrice(Double totalPrice){
        jointOrder.setTotalPrice(totalPrice);

        return this;
    }

    public JointOrder build(){
        return jointOrder;
    }
}
