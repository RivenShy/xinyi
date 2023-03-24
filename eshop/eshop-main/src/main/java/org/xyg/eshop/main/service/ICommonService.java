package org.xyg.eshop.main.service;

public interface ICommonService {

    /**
     * 根据字典编码与字典键值获取字典value
     * @param dictCode 字典编码
     * @param dictKey 字典键值
     * @return
     */
    String getDictValue(String dictCode,String dictKey);

    /**
     * 查询省市区名称
     * @param provincesCode 省编码
     * @param cityCode 市编码
     * @param areasCode 区/县编码
     * @return
     */
    String getPcaName(String provincesCode,String cityCode,String areasCode);

}
