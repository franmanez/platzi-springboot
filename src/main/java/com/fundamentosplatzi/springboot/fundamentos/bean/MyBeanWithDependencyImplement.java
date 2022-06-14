package com.fundamentosplatzi.springboot.fundamentos.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyBeanWithDependencyImplement implements MyBeanWithDependency{

    Log LOGGER = LogFactory.getLog(MyBeanWithDependencyImplement.class);

    MyOperation myOperation;

    public MyBeanWithDependencyImplement(MyOperation myOperation){
        this.myOperation = myOperation;
    }

    @Override
    public void printWithDependency() {
        LOGGER.info("Estamos dentro del método printWithDependency");
        int n = 22;
        System.out.println(myOperation.sum(n));
        LOGGER.debug("número enviado como parámetro: " + n);
        System.out.println("Hola desde la implementación de un bean con dependencia");
    }
}
