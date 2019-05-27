package liao.code.generator.back.factory;

import liao.code.generator.back.javacode.*;
import liao.code.generator.back.sql.SqlGenerator;

/**
 * Created by ao on 2017/10/24.
 */
public enum GeneratorTypeEnum {
    PO(1,new BeanClassGenerator.BeanFactory()),
    PO2(1,new BeanClassGenerator4Swagger2.BeanFactory()),
    VO(2,new VoModelClassGenerator.VoFactory()),
    DAO(3,new DaoClassGenerator.DaoFactory()),
    DAO_IMPL(4,new DaoImplClassGenerator.DaoImplFactory()),
    SERVICE(5,new ServiceClassGenerator.ServiceFactory()),
    SERVICE_IMPL(6,new ServiceImplClassGenerator.ServiceImplFactory()),
    CONTROLLER(7,new ControllerClassGenerator.ControllerFactory()),
    SQL(8,new SqlGenerator.SqlFactory());
    private int type;
    private Factory factory;

    GeneratorTypeEnum(int i, Factory factory) {
        this.type = type;
        this.factory = factory;
    }

    public Factory getFactory() {
        return factory;
    }
}
