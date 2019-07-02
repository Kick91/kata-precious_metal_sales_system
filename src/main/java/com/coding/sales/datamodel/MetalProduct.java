package com.coding.sales.datamodel;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
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
    private BigDecimal metalProductPrice;
    private String metalProductOffere;
    private String metalProductFullReduction;

    public BigDecimal getMetalProductPrice() {
		return metalProductPrice;
	}

	public void setMetalProductPrice(BigDecimal metalProductPrice) {
		this.metalProductPrice = metalProductPrice;
	}

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

        String jsonmetalProducts = "[{'metalProductName': '世园会五十国钱币册','metalProductId': '001001','metalProductUnit': '册','metalProductPrice': '998.00','metalProductOffere': '','metalProductFullReduction': ''},{'metalProductName': '2019北京世园会纪念银章大全40g','metalProductId': '001002','metalProductUnit': '盒','metalProductPrice': '1380.00','metalProductOffere': '可使用9折打折券','metalProductFullReduction': ''},{'metalProductName': '招财进宝','metalProductId': '003001','metalProductUnit': '条','metalProductPrice': '1580.00','metalProductOffere': '可使用95折打折券','metalProductFullReduction': ''},{'metalProductName': '水晶之恋','metalProductId': '003002','metalProductUnit': '册','metalProductPrice': '998.00','metalProductOffere': '','metalProductFullReduction': '第3件半价，满3送1'},{'metalProductName': '中国经典钱币套装','metalProductId': '002002','metalProductUnit': '套','metalProductPrice': '998.00','metalProductOffere': '','metalProductFullReduction': '每满2000减30，每满1000减10'},{'metalProductName': '守扩之羽比翼双飞4.8g','metalProductId': '002001','metalProductUnit': '条','metalProductPrice': '1080.00','metalProductOffere': '可使用95折打折券','metalProductFullReduction': '第3件半价，满3送1'},{'metalProductName': '中国银象棋12g','metalProductId': '002003','metalProductUnit': '套','metalProductPrice': '698.00','metalProductOffere': '可使用9折打折券','metalProductFullReduction': '每满3000元减350, 每满2000减30，每满1000减10'}]";



        List<MetalProduct> metalProducts = getMetalProducts(jsonmetalProducts, MetalProduct.class);

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
        List<MetalProduct> list = new ArrayList<MetalProduct>();
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
