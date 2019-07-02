package com.coding.sales;

import com.coding.sales.dataModel.MetalProduct;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.*;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {
    private static Map<String, Double> initOffereMap = new HashMap<String, Double>();
    private static Map<String,String> initFullReductionMap = new HashMap<String, String>();
    private static List<MetalProduct> initMetalProdutslist = new ArrayList<MetalProduct>();
    static {

        MetalProduct metalProduct = new MetalProduct("001001","世园会五十国钱币册",
                "册","998.00","","");
        initMetalProdutslist.add(metalProduct);

        initOffereMap.put("9折券",0.90);
        initOffereMap.put("9折券",0.95);

        initFullReductionMap.put("每满3000元减350","");
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result = null;

        //TODO: 请完成需求指定的功能
//        List<PaymentCommand> paymentCommandList = command.getPayments();
//        PaymentCommand paymentCommand = paymentCommandList.get(0);
//        String orderId = command.getOrderId();
//
//        BigDecimal amount = paymentCommand.getAmount();
//        String memberId = command.getMemberId();
        List<OrderItemCommand> orderItemCommandList = command.getItems();
        double totalPrice = 0.00;
        for (OrderItemCommand orderItemCommand : orderItemCommandList){
            String metalProductId = orderItemCommand.getProduct();
            for (MetalProduct metalProduct : initMetalProdutslist){
                if (metalProduct.getMetalProductId().equals(metalProductId)){
                    String offere = metalProduct.getMetalProductOffere();
                    if (null != offere && !"".equals(offere)){
                       Double doubleOffere = initOffereMap.get(offere);
                       totalPrice = totalPrice + doubleOffere * metalProduct.getMetalProductPrice();
                    }else{
                        totalPrice = totalPrice + metalProduct.getMetalProductPrice();
                    }
                }
            }
        }
        return result;
    }
}
