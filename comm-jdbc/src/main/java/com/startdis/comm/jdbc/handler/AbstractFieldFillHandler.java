package com.startdis.comm.jdbc.handler;

import com.startdis.comm.jdbc.config.InterceptConfig;
import com.startdis.comm.jdbc.enums.FieldFillStrategyEnum;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author Startdis
 * @email startdis@dianjiu.cc
 * @desc 字段填充拦截器抽象类
 */
@SuppressWarnings("checkstyle:Indentation")
public abstract class AbstractFieldFillHandler extends AbstractSqlHandler implements FieldFillHandler {


    private FieldFillStrategyEnum fieldFillStrategyEnum;

    public AbstractFieldFillHandler(FieldFillStrategyEnum fieldFillStrategyEnum, InterceptConfig interceptConfig) {
        super(interceptConfig);
        this.fieldFillStrategyEnum = fieldFillStrategyEnum;
    }


    protected abstract Expression doGetFieldFillValue();

    @Override
    public Expression getFieldFillValue() {
        return doGetFieldFillValue();
    }

    /**
     * 优先级配置填充字段--》子类实现getDefaultFieldFillColumn
     * 如果都为指定则抛出异常
     */
    @Override
    public String getFieldFillColumn() {
        return handleColumn(interceptConfig.getColumn());
    }





    
    @Override
    public boolean createIgnore(String tableName) {
        if (FieldFillStrategyEnum.UPDATE==fieldFillStrategyEnum){
            return true;
        }
        return defaultIgnoreStrategy(tableName,interceptConfig);
    }


    protected  boolean customIgnore(){
        return false;
    }

    @Override
    public boolean updateIgnore(String tableName) {
        if (FieldFillStrategyEnum.INSERT==fieldFillStrategyEnum){
            return true;
        }
        return defaultIgnoreStrategy(tableName,interceptConfig);
    }
}
