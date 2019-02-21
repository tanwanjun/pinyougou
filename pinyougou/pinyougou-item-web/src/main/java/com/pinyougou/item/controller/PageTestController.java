package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/test")
@RestController
public class PageTestController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;

    //注入配置文件中的配置项
    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;


    /**
     * 接收商品spu id数组及其根据每个spu id生成一个静态页面到指定了路径下
     * @param goodsIds 商品spu id数组
     * @return 操作结果
     */
    @GetMapping("/audit")
    public String auditGoods(Long[] goodsIds){
        if (goodsIds != null && goodsIds.length > 0) {
            for (Long goodsId : goodsIds) {
                genItmeHtml(goodsId);
            }
        }
        return "success";
    }

    /**
     * 生成spu id对应的商品详情页面
     * @param goodsId 商品spu id
     */
    private void genItmeHtml(Long goodsId) {
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();

            //模版
            Template template = configuration.getTemplate("item.ftl");

            //数据
            Map<String, Object> map = new HashMap<>();
            //查询数据
            Goods goods = goodsService.findGoodsByGoodsIdAndStauts(goodsId, "1");

            //goods 商品基本
            map.put("goods", goods.getGoods());
            //goodsDesc 商品描述
            map.put("goodsDesc", goods.getGoodsDesc());
            //itemList  商品sku列表（根据spu id查询获取到的sku列表）
            map.put("itemList", goods.getItemList());
            //itemCat1 一级商品分类中文名称
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());
            map.put("itemCat1", itemCat1.getName());
            //itemCat2 二级商品分类中文名称
            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
            map.put("itemCat2", itemCat2.getName());
            //itemCat3 三级商品分类中文名称
            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
            map.put("itemCat3", itemCat3.getName());

            //输出
            String filename = ITEM_HTML_PATH + goodsId + ".html";
            FileWriter fileWriter = new FileWriter(filename);
            template.process(map, fileWriter);
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收商品spu id数组；遍历每个spu id到指定路径下将对应的静态页面删除
     * @param goodsIds 商品spu id数组
     * @return 操作结果
     */
    @GetMapping("/delete")
    public String delete(Long[] goodsIds){
        if (goodsIds != null && goodsIds.length > 0) {
            for (Long goodsId : goodsIds) {
                String filename = ITEM_HTML_PATH + goodsId + ".html";
                File file = new File(filename);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return "success";
    }
}
