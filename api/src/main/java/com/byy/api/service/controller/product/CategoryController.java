package com.byy.api.service.controller.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.product.CategoryForm;
import com.byy.api.service.vo.product.CategoryVO;
import com.byy.biz.service.product.CategoryService;
import com.byy.biz.service.product.ProductService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.product.Category;
import com.byy.dal.entity.beans.product.Product;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;

/** @Author: xcf @Date: 13/06/19 下午 05:10 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController extends CommonController<CategoryService> {

  private ProductService productService;

  /**
   * 后台分页查询
   *
   * @param form
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllCategorySys(@Valid CategoryForm form) {
    ImmutableMap<String, Object> map = getAllCategory(form);
    return success(map);
  }

  /**
   * 后台添加商品分类
   *
   * @param form
   * @return
   */
  @PostMapping("/back")
  public ResponseObject<CategoryVO> saveCategorySys(@Valid @RequestBody CategoryForm form) {
    Category category = Transformer.fromBean(form, Category.class);
    CheckHelper.trueOrThrow(baseService.save(category), BizException::new, "添加商品分类失败,请检查相应参数!");
    return success(getCategoryById(category.getId()));
  }

  /**
   * 后台删除分类
   *
   * @param categoryId
   * @return
   */
  @DeleteMapping("/back/{categoryId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> removeCategorySys(@PathVariable("categoryId") Long categoryId) {
    if (productService.count(WrapperProvider.queryWrapper(Product::getCategoryId,categoryId)) != 0) {
      return error(-1,"该分类下存在未删除的商品,请先删除商品!");
    } else {
      CheckHelper.trueOrThrow(
          baseService.removeById(categoryId), BizException::new, "删除分类失败,请检查分类id是否正确!");
      return success(categoryId);
    }
  }

  /**
   * 后台通过id查询分类
   *
   * @param categoryId
   * @return
   */
  @GetMapping("/back/{categoryId}")
  public ResponseObject<CategoryVO> getCategoryByPrimaryKeySys(
      @PathVariable("categoryId") Long categoryId) {
    CategoryVO categoryVO = getCategoryById(categoryId);
    return success(categoryVO);
  }

  /**
   * 后台修改分类
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  public ResponseObject<CategoryVO> modifyCategorySys(@Valid @RequestBody CategoryForm form) {
    Category category = Transformer.fromBean(form, Category.class);
    CheckHelper.trueOrThrow(baseService.updateById(category), BizException::new, "更新分类失败,请检查相应参数!");
    return success(getCategoryById(category.getId()));
  }

  /**
   * 后台不分页查询所有分类
   *
   * @return
   */
  @GetMapping("/back/list")
  public ResponseObject<List<CategoryVO>> getAllCategory() {
    List<Category> list = baseService.list();
    List<CategoryVO> categoryVOS = Transformer.fromList(list, CategoryVO.class);
    return success(categoryVOS);
  }

  /**
   * 小程序端分页查询所有分类
   * @return
   */
  @GetMapping("/app/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllCategoryApp(@Valid CategoryForm form){
    ImmutableMap<String, Object> map = getAllCategory(form);
    return success(map);
  }



  /**
   * 分页查询所有分类
   * @param form
   * @return
   */
  private ImmutableMap<String,Object> getAllCategory(CategoryForm form){
    IPage<Category> page = baseService.page(form.newPage());
    ImmutableMap<String, Object> map =
            ImmutableMap.of(
                    "list",
                    Transformer.fromList(page.getRecords(), CategoryVO.class),
                    "total",
                    page.getTotal());
    return map;
  }

  /**
   * 根据id查询商品分类 用于添加,编辑成功时返回 也可用作编辑时查询商品分类
   *
   * @param categoryId
   * @return
   */
  private CategoryVO getCategoryById(Long categoryId) {
    Category category = baseService.getById(categoryId);
    return Transformer.fromBean(category, CategoryVO.class);
  }
}
