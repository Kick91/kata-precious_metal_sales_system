package com.coding.sales.dataModel;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hehuikang
 * @description 贵金属产品
 * @date 2019-07-02 17:10
 */
public class MetalProduct {

    private String metalProductId;
    private String metalProductName;
    private String metalProductUnit;
    private Double metalProductPrice;
    private String metalProductOffere;
    private String metalProductFullReduction;

    public MetalProduct() {
    }

    public MetalProduct(String metalProductId, String metalProductName, String metalProductUnit, Double metalProductPrice, String metalProductOffere, String metalProductFullReduction) {
        metalProductId = metalProductId;
        metalProductName = metalProductName;
        metalProductUnit = metalProductUnit;
        metalProductPrice = metalProductPrice;
        metalProductOffere = metalProductOffere;
        metalProductFullReduction = metalProductFullReduction;
    }


    public static MetalProduct getMetalProductById(String metalProductId) throws Exception {
        MetalProduct metalProduct = new MetalProduct();

        String jsonMembers = "[{'memberNo':'6236609999','memberName':'马丁','memberType':'普卡','memberPoints':'9860'},{'memberNo':'6630009999','memberName':'王立','memberType':'金卡','memberPoints':'48860'},{'memberNo':'8230009999','memberName':'李想','memberType':'白金卡','memberPoints':'98860'},{'memberNo':'9230009999','memberName':'张三','memberType':'钻石卡','memberPoints':'198860'}]";

        List<MetalProduct> metalProducts = getMetalProducts(jsonMembers, MetalProduct.class);

        if (metalProducts.size() > 0) {
            for (int i = 0; i < metalProducts.size(); i++) {
                if (metalProducts.get(i).getMetalProductId().equals(metalProductId)) {
                    metalProduct = metalProducts.get(i);
                    break;
                }
            }

        } else {
            throw new Exception("产品信息不存在");
        }

        if (metalProduct == null) {
            throw new Exception("产品信息不存在");
        }

        return metalProduct;
    }

    public static MetalProduct getMember(String jsonString, Class cls) {
        MetalProduct metalProduct = null;
        try {
            metalProduct = (MetalProduct) JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return metalProduct;
    }

    public static List<MetalProduct> getMetalProducts(String jsonString, Class cls) {
        List<MetalProduct> list = new ArrayList<>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
        }
        return list;
    }

    public String getMetalProductId() {
        return metalProductId;
    }

    public void setMetalProductId(String metalProductId) {
        this.metalProductId = metalProductId;
    }

    public String getMetalProductName() {
        return metalProductName;
    }

    public void setMetalProductName(String metalProductName) {
        this.metalProductName = metalProductName;
    }

    public String getMetalProductUnit() {
        return metalProductUnit;
    }

    public void setMetalProductUnit(String metalProductUnit) {
        this.metalProductUnit = metalProductUnit;
    }

    public Double getMetalProductPrice() {
        return metalProductPrice;
    }

    public void setMetalProductPrice(Double metalProductPrice) {
        this.metalProductPrice = metalProductPrice;
    }

    public String getMetalProductOffere() {
        return metalProductOffere;
    }

    public void setMetalProductOffere(String metalProductOffere) {
        this.metalProductOffere = metalProductOffere;
    }

    public String getMetalProductFullReduction() {
        return metalProductFullReduction;
    }

    public void setMetalProductFullReduction(String metalProductFullReduction) {
        this.metalProductFullReduction = metalProductFullReduction;
    }
}
