package com.startdis.comm.log.enums;

/**
 * 日志操作类型
 */
public enum BusinessType
{
    /**
     * 其它
     */
    OTHER,
    /**
     * 新增
     */
    INSERT,
    /**
     * 修改
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查询
     */
    SELECT,
    /**
     * 导出
     */
    EXPORT,
    /**
     * 导入
     */
    IMPORT;

    public static String getMsg(BusinessType type){
    	String result = null;
        switch (type) {
		case INSERT:
			result = "新增";
			break;
		case UPDATE:
			result = "修改";
			break;
		case DELETE:
			result = "删除";
			break;
		case SELECT:
			result = "查询";
			break;
		case EXPORT:
			result = "导出";
			break;
		case IMPORT:
			result = "导入";
			break;
		case OTHER:
			result = "其它";
			break;
		default:
			break;
		}
        return result;
    }
}

