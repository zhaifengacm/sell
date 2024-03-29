package com.imooc.sell.controller;

import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.form.CategoryForm;
import com.imooc.sell.service.CategoryService;
import com.imooc.sell.util.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 卖家商品类别
 *
 */
@Controller
@RequestMapping("/seller/category")
public class SellCategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 类目列表
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(Map<String,Object> map) {
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList",categoryList);
        return new ModelAndView("category/list",map);
    }

    /**
     * 修改类目
     * @param categoryId
     * @param map
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                             Map<String,Object> map) {
        ProductCategory productCategory = null;
        if (categoryId!=null) {
            productCategory = categoryService.findOne(categoryId);
            map.put("category",productCategory);
        }
        return new ModelAndView("/category/index",map);

    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String,Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("/common/error",map);
        }
        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId()!=null) {
                productCategory = categoryService.findOne(form.getCategoryId());

            }
            BeanUtils.copyProperties(form,productCategory);
            categoryService.save(productCategory);

        }catch (SellException e) {
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("/common/error",map);
        }
        map.put("msg","成功修改");
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("/common/success",map);
    }




}
