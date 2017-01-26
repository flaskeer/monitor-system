package com.asterisk.opensource.monitor.dao;

import com.asterisk.opensource.monitor.domain.Goods;

import java.util.List;
import java.util.Map;


public interface IGoodsDao {

    /**
     * 添加商品
     * @param goods
     */
    void addGoods(Goods goods);

    /**
     * 查询所有商品
     * @return
     */
    List<Goods> getAllGoods();

    /**
     * 查询商品
     * @param goodsId 商品ID
     * @return
     */
    Goods getGoodsById(Long goodsId);

    /**
     * 删除商品
     * @param goodsId 商品ID
     * @return
     */
    int delGoodsById(Long goodsId);

    /**
     * 更新商品
     * @param goods
     * @return
     */
    int updateGoodsById(Goods goods);

    /**
     * 扣除库存
     * @param map
     * @return
     */
    int subGoodsStock(Map<String, Object> map);
}
