package com.example.miaosha.dao;

import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {
    @Select("select * from miaosha_order where user_id = #{id} and goods_id = #{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoods(@Param("id") long id, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, " +
            "order_channel, status, create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}," +
            "#{orderChannel}, #{status}, #{createDate}) ")

    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Delete("delete from miaosha_order where order_id = #{orderId}")
    public boolean deleteMiaoshaOrder(OrderInfo orderInfo);

    @Select("select * from miaosha_order where order_id = #{id}")
    OrderInfo getOrderById(@Param("id") long orderId);
}
