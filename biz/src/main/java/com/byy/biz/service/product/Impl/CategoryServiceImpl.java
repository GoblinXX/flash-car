package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.CategoryService;
import com.byy.dal.entity.beans.product.Category;
import com.byy.dal.mapper.product.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:58
 * @Description:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
