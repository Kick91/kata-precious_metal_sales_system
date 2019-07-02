package com.coding.sales;

import com.coding.sales.datamodel.Member;
import com.coding.sales.datamodel.MetalProduct;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.output.PaymentRepresentation;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {
    private static final BigDecimal THREE_THOUSAND = new BigDecimal(3000);
    private static final BigDecimal THREE_HUNDRED_AND_FIFTY  = new BigDecimal(350);
    private static final BigDecimal TWO_THOUSAND = new BigDecimal(2000);
    private static final BigDecimal THIRTY  = new BigDecimal(30);
    private static final BigDecimal ONE_THOUSAND = new BigDecimal(1000);




    public static void main(String[] args) {
//        if (args.length != 2) {
//            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
//        }

        String jsonFileName = "/Users/hhk/Desktop/GitProjects/kata-precious_metal_sales_system/src/test/resources/sample_command.json";
        String txtFileName = "/Users/hhk/Desktop/GitProjects/kata-precious_metal_sales_system/src/test/resources/sample_result_new.txt";

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
        List<OrderItemCommand> orderItemCommandList = command.getItems();
        List<String> discountsList = command.getDiscounts();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscountPrice = BigDecimal.ZERO;
        int memberPointsIncreased = 0;
        List<OrderItemRepresentation> items = new ArrayList<>();
        List<DiscountItemRepresentation> discounts = new ArrayList<>();
        List<PaymentRepresentation> payments = new ArrayList<>();
        for (OrderItemCommand orderItemCommand : orderItemCommandList){
            MetalProduct metalProduct = null;
            try {
                metalProduct = MetalProduct.getMetalProductById(orderItemCommand.getProduct());
            } catch (Exception e) {
                continue;
            }
            BigDecimal amount = orderItemCommand.getAmount();
            BigDecimal itemTotalPrice = amount.multiply(metalProduct.getMetalProductPrice());
            totalPrice = totalPrice.add(itemTotalPrice);

            DiscountItemRepresentation discountItemRepresentation = calculateByPreferentialWay(discountsList, metalProduct,amount);
            totalDiscountPrice = totalDiscountPrice.add(discountItemRepresentation.getDiscount());
            discounts.add(discountItemRepresentation);

            OrderItemRepresentation orderItemRepresentation = new OrderItemRepresentation(metalProduct.getMetalProductId(),
                    metalProduct.getMetalProductName(),metalProduct.getMetalProductPrice(),amount,itemTotalPrice);
            items.add(orderItemRepresentation);
        }
        for (PaymentCommand paymentCommand : command.getPayments()){
            PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(),paymentCommand.getAmount());
            payments.add(paymentRepresentation);
        }

        Member member = new Member();
        try {
            member = Member.getMemberByNo(command.getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String oldMemberType = member.getOldMemberType();
        BigDecimal receivables = totalPrice.subtract(totalDiscountPrice);
        memberPointsIncreased = receivables.intValue();
        if("金卡".equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(new BigDecimal(1.50)).intValue();
        }else if("白金卡".equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(new BigDecimal(1.80)).intValue();
        }else if("钻石卡".equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(new BigDecimal(2.00)).intValue();
        }
        member.setMemberPointsIncreased(memberPointsIncreased);
        member.setNewMemberType(Member.getMemberType(memberPointsIncreased));


        result = new OrderRepresentation(
                    command.getOrderId(),new Date(command.getCreateTime()),command.getMemberId(),member.getMemberName(),member.getOldMemberType(),
                member.getNewMemberType(),member.getMemberPointsIncreased(),member.getMemberPoints(),items,totalPrice,
                discounts,totalDiscountPrice,receivables,payments,command.getDiscounts());

        return result;
    }

    private DiscountItemRepresentation calculateByPreferentialWay(List<String> discountsList,MetalProduct metalProduct,BigDecimal amount){
        BigDecimal fullReductionPrice = BigDecimal.ZERO;
        BigDecimal offerePrice = BigDecimal.ZERO;
        DiscountItemRepresentation discountItemRepresentation = null;
        for(String discount : discountsList){
            BigDecimal totalPrice = metalProduct.getMetalProductPrice().multiply(amount);
            if(StringUtils.isNotEmpty(metalProduct.getMetalProductFullReduction())){
               if("每满3000元减350".equals(discount)){
                   fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(THREE_THOUSAND).multiply(THREE_HUNDRED_AND_FIFTY));
               }else if("每满2000元减30".equals(discount)){
                   fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(TWO_THOUSAND).multiply(THIRTY));
               }else if("每满1000元减10".equals(discount)){
                   fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(ONE_THOUSAND).multiply(BigDecimal.TEN));
               }
            }
            if(StringUtils.isNotEmpty(metalProduct.getMetalProductOffere())){
                if("95折券".equals(discount)){
                    offerePrice = totalPrice.multiply(new BigDecimal(0.05));
                }else if("9折券".equals(discount)){
                    offerePrice = totalPrice.multiply(new BigDecimal(0.10));
                }
            }
            discountItemRepresentation = new DiscountItemRepresentation(metalProduct.getMetalProductId(),metalProduct.getMetalProductName(),fullReductionPrice.max(offerePrice));
        }
        return discountItemRepresentation;
    }
}
