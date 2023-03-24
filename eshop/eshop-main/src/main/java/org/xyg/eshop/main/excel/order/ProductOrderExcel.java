package org.xyg.eshop.main.excel.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ColumnWidth(25)
@HeadRowHeight(25)
@ContentRowHeight(18)
public class ProductOrderExcel {

    @ApiModelProperty("订单类型")
    @ExcelProperty("订单类型")
    private String orderType;

    @ApiModelProperty("订单日期")
    @ExcelProperty("订单日期")
    private Date orderDate;

    @ApiModelProperty("平台订单号")
    @ExcelProperty("平台订单号")
    private String orderNo;

    @ApiModelProperty("订单金额")
    @ExcelProperty("订单金额")
    private BigDecimal price;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    private String remark;

    @ApiModelProperty("收货人")
    @ExcelProperty("收货人")
    private String consignee;

    @ApiModelProperty("联系方式")
    @ExcelProperty("联系方式")
    private String contact;

    @ApiModelProperty("地址")
    @ExcelProperty("地址")
    private String address;

    @ApiModelProperty("距离信义门店")
    @ExcelProperty("距离信义门店")
    private String distance;

    @ApiModelProperty("安装时间")
    @ExcelProperty("安装时间")
    private Date installDate;

    @ApiModelProperty("安装人员")
    @ExcelProperty("安装人员")
    private String installer;

    @ApiModelProperty("派单人")
    @ExcelProperty("派单人")
    private String dispatcher;

    @ApiModelProperty("发票类型")
    @ExcelProperty("发票类型")
    private String billingType;

    @ApiModelProperty("抬头名称")
    @ExcelProperty("抬头名称")
    private String headName;

    @ApiModelProperty("纳税人识别号")
    @ExcelProperty("纳税人识别号")
    private String taxpayerNum;

}
