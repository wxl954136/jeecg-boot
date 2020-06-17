package org.jeecg.modules.utils;


/**
 * 系统状态字典
 * @author luke
 * @date 2018/08/01 16:45:43
 */
public enum  SysStatusEnum {

    /**
     * 大类成本方式，日末/月末
     */
    COST_SYS_DAYEND("日末权加平均", "DAYEND"),
    COST_SYS_MONTHEND("月末权加平均", "MONTHEND"),

    /** 成本讲价方式 */
    COST_AVER("加权平均", 0),
    COST_SINGLE("个别计价",1),
    /** 采购/批发 - > 付款方式**/
    SYS_PAYABLE_CASH("现金","0"),
    SYS_PAYABLE_DEBT("应付款","1"),


    SYS_RECEIVABLE_CASH("现金","0"),
    SYS_RECEIVABLE_DEBT("应收款","1"),


    NOTE_PO_IN("采购入库单","CGRK"),
    NOTE_PO_BACK("采购退货单","CGTH"),
    NOTE_SALES_OUT("销售出库","CKD"),
    NOTE_SALES_BACK("销售退货单","THD"),


    NOTE_PAYABLE("应付款","YFK"),
    NOTE_RECEIVABLE("应收款","YSK"),

    NOTE_SETTLE_IN("收款单","SKD"),
    NOTE_SETTLE_OUT("付收款","FKD"),


    NOTE_SOURCE_AUTO("单据来源-系统自动产生","AUTO"),
    NOTE_SOURCE_SYS("单据来源-系统","SYS"),


    NOTE_NEW("新单据", "NEW");


    private String value;
    private int index;


    SysStatusEnum(String name, String value) {
        this.value = value;
    }
    SysStatusEnum(String name, int index) {
        this.index = index;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
