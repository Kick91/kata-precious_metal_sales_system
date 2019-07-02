package com.coding.sales;

import com.coding.sales.datamodel.Constant;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {





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

        BigDecimal receivables = totalPrice.subtract(totalDiscountPrice);
        Member member = getMemberInfo(command,receivables);


        Date date = getDate(command);
        result = new OrderRepresentation(
                    command.getOrderId(),date,command.getMemberId(),member.getMemberName(),member.getOldMemberType(),
                member.getNewMemberType(),member.getMemberPointsIncreased(),member.getMemberPoints(),items,totalPrice,
                discounts,totalDiscountPrice,receivables,payments,command.getDiscounts());

        return result;
    }

    private Date getDate(OrderCommand command) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATEFORMAT);
        Date date = new Date();
        try {
             date = sdf.parse(command.getCreateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private DiscountItemRepresentation calculateByPreferentialWay(List<String> discountsList,MetalProduct metalProduct,BigDecimal amount){
        DiscountItemRepresentation discountItemRepresentation = null;
        for(String discount : discountsList){
            BigDecimal totalPrice = metalProduct.getMetalProductPrice().multiply(amount);

            BigDecimal fullReductionPrice = caculateFullReduction(metalProduct,totalPrice,discount);

            BigDecimal offerePrice = caculateOffere(metalProduct,totalPrice,discount);

            discountItemRepresentation = new DiscountItemRepresentation(metalProduct.getMetalProductId(),metalProduct.getMetalProductName(),fullReductionPrice.max(offerePrice));
        }
        return discountItemRepresentation;
    }

    private BigDecimal caculateFullReduction(MetalProduct metalProduct,BigDecimal totalPrice,String discount){
        BigDecimal fullReductionPrice = BigDecimal.ZERO;
        if(StringUtils.isNotEmpty(metalProduct.getMetalProductFullReduction())){
            if(Constant.FULL_3000_SUB_350.equals(discount)){
                fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(Constant.THREE_THOUSAND).multiply(Constant.THREE_HUNDRED_AND_FIFTY));
            }else if(Constant.FULL_2000_SUB_30.equals(discount)){
                fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(Constant.TWO_THOUSAND).multiply(Constant.THIRTY));
            }else if(Constant.FULL_1000_SUB_10.equals(discount)){
                fullReductionPrice =  totalPrice.subtract(totalPrice.divideToIntegralValue(Constant.ONE_THOUSAND).multiply(BigDecimal.TEN));
            }else if(Constant.PART_THREE_HALF.equals(discount)){

            }else if(Constant.FULL_THREE_TO_ONE.equals(discount)){

            }
        }
        return  fullReductionPrice;
    }

    private BigDecimal caculateOffere(MetalProduct metalProduct,BigDecimal totalPrice,String discount){
        BigDecimal offerePrice = BigDecimal.ZERO;
        if(StringUtils.isNotEmpty(metalProduct.getMetalProductOffere())){
            if(Constant.COUPON_95.equals(discount)){
                offerePrice = totalPrice.multiply(Constant.FIVE_PERCENT);
            }else if(Constant.COUPON_90.equals(discount)){
                offerePrice = totalPrice.multiply(Constant.TEN_PERCENT);
            }
        }
        return offerePrice;
    }

    private Member getMemberInfo(OrderCommand command,BigDecimal receivables){
        Member member = new Member();
        try {
            member = Member.getMemberByNo(command.getMemberId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String oldMemberType = member.getOldMemberType();
        int memberPointsIncreased = receivables.intValue();
        if(Constant.GOLD_CARD.equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(Constant.ONE_POINT_FIVE).intValue();
        }else if(Constant.WHITE_GOLD_CARD.equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(Constant.ONE_POINT_EIGHT).intValue();
        }else if(Constant.DIAMOND_CARD.equals(oldMemberType)){
            memberPointsIncreased = receivables.multiply(Constant.TWO).intValue();
        }
        member.setMemberPointsIncreased(memberPointsIncreased);
        member.setMemberPoints(member.getMemberPoints() + memberPointsIncreased);
        member.setNewMemberType(Member.getMemberType(memberPointsIncreased));
        return member;
    }
}
