package com.example.miaosha.dao;

import com.example.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsDao {

    /**
     * miaosha_goods 和 goods 联查
     * 小表 join 大表
     * @return
     */
    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from " +
            " miaosha_goods as mg left join goods as g" +
            " on mg.id = g.id")
    public List<GoodsVo> listGoodsVo();

    /**
     * selectkey 获取最后插入的ID值
     * @param id
     * @return
     */
    // 添加共享锁
    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from " +
            " miaosha_goods as mg left join goods as g" +
            " on mg.id = g.id where g.id = #{goodsId} for update")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false,
            statement = "select last_insert_id()")
    public GoodsVo getGoodsVoGoodsId(@Param("goodsId") long id);

    @Update("Update miaosha_goods set stock_count = stock_count - 1 where" +
            " goods_id = #{id}")
    public int reduceStock(GoodsVo g);
}
