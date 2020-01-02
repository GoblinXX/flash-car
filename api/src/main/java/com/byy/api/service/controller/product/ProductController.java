package com.byy.api.service.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.product.CategoryForm;
import com.byy.api.service.form.product.ProductForm;
import com.byy.api.service.utils.SkuUtils;
import com.byy.api.service.vo.comment.CommentVO;
import com.byy.api.service.vo.product.CategoryVO;
import com.byy.api.service.vo.product.ProductVO;
import com.byy.biz.service.comment.CommentPicService;
import com.byy.biz.service.comment.CommentService;
import com.byy.biz.service.product.CategoryService;
import com.byy.biz.service.product.ProductPicService;
import com.byy.biz.service.product.ProductService;
import com.byy.biz.service.product.SkuService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.comment.CommentPic;
import com.byy.dal.entity.beans.product.Category;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.beans.product.ProductPic;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.entity.dos.comment.CommentDO;
import com.byy.dal.entity.dos.product.ProductDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotNull;
import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;

/** @Author: xcf @Date: 12/06/19 上午 11:30 @Description:商品相关controller */
@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController extends CommonController<ProductService> {

  private SkuUtils skuUtils;

  private final CategoryService categoryService;

  private final ProductPicService productPicService;

  private final SkuService skuService;

  private final CommentService commentService;

  private final CommentPicService commentPicService;

  /**
   * 后台分页条件查询商品
   *
   * @param form
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllProductSys(@Valid ProductForm form) {
    Map<String, Object> params = mapParams(form);
    IPage<ProductDO> productPage = baseService.getAllProductSys(form.newPage(), params);
    List<ProductVO> productVOS = Transformer.fromList(productPage.getRecords(), ProductVO.class);
    productVOS =
        productVOS.stream()
            .peek(productVO -> skuUtils.setOne(productVO))
            .collect(Collectors.toList());
    ImmutableMap<String, Object> immutableMap =
        ImmutableMap.of("productList", productVOS, "productTotal", productPage.getTotal());
    return success(immutableMap);
  }

  /**
   * 后台添加商品
   *
   * @param form
   * @return
   */
  @PostMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<ProductVO> saveProductSys(@Valid @RequestBody ProductForm form) {
    Product product = Transformer.fromBean(form, Product.class);
    CheckHelper.trueOrThrow(baseService.save(product), BizException::new, "商品保存失败,请检查相应参数!");
    Long productId = product.getId();
    saveProductPic(form.getProductPics(), productId);
    saveSku(form.getSkuList(), productId);
    return success(Transformer.fromBean(form, ProductVO.class));
  }

  /**
   * 后台根据id查商品
   *
   * @param productId
   * @return
   */
  @GetMapping("/back/{productId}")
  public ResponseObject<ProductVO> getProductByPrimaryKeySys(
      @PathVariable("productId") Long productId) {
    ProductVO productVO = getProductById(productId);
    return success(productVO);
  }

  /**
   * 后台更新商品
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<ProductVO> modifyProductSys(@Valid @RequestBody ProductForm form) {
    Product product = Transformer.fromBean(form, Product.class);
    CheckHelper.trueOrThrow(baseService.updateById(product), BizException::new, "商品更新失败,请检查相应参数!");
    Long productId = product.getId();
    saveProductPic(form.getProductPics(), productId);
    saveSku(form.getSkuList(), productId);
    ProductVO productVO = Transformer.fromBean(form, ProductVO.class);
    return success(productVO);
  }

  /**
   * 后台根据id删除商品
   *
   * @param productId
   * @return
   */
  @DeleteMapping("/back/{productId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> removeProductByIdSys(@PathVariable("productId") Long productId) {
    if (baseService.removeById(productId)
        && productPicService.remove(
            WrapperProvider.queryWrapper(ProductPic::getProductId, productId))
        && skuService.remove(WrapperProvider.queryWrapper(Sku::getProductId, productId))) {
      return success(productId);
    } else {
      return error(null);
    }
  }

  /**
   * 后台设置商品上下架状态以及热门状态
   *
   * @param form
   * @return
   */
  @PutMapping("/back/status")
  public ResponseObject<ProductVO> setProductStatusSys(@Valid @RequestBody ProductForm form) {
    Product product = Transformer.fromBean(form, Product.class);
    CheckHelper.trueOrThrow(baseService.updateById(product), BizException::new, "状态设置失败,请检查相应参数!");
    return success(getProductById(product.getId()));
  }

  /**
   * 小程序端分页条件查询商品
   *
   * @param form
   * @return
   */
  @GetMapping("/app/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllProductApp(@Valid ProductForm form) {
    LambdaQueryWrapper<Product> wrapper;
    if (isNotEmpty(form.getOnHot())) {
      if (form.getOnHot()) {
        wrapper = convertParams(form, true);
      } else {
        wrapper = convertParams(form, false);
      }
    } else {
      wrapper = convertParams(form, false);
    }
    IPage<Product> page = baseService.page(form.newPage(), wrapper);
    List<ProductVO> productVOS = Transformer.fromList(page.getRecords(), ProductVO.class);
    productVOS =
        productVOS.stream()
            .peek(productVO -> skuUtils.setOne(productVO))
            .collect(Collectors.toList());
    return success(ImmutableMap.of("productList", productVOS, "productTotal", page.getTotal()));
  }

  /**
   * 小程序端通过id查询商品
   *
   * @param productId
   * @return
   */
  @GetMapping("/app/{productId}")
  public ResponseObject<ProductVO> getProductByPrimaryKeyApp(
      @PathVariable("productId") Long productId) {
    Product product = baseService.getById(productId);
    ProductVO productVO = Transformer.fromBean(product, ProductVO.class);
    List<Sku> skuList = skuService.list(WrapperProvider.queryWrapper(Sku::getProductId, productId));
    Sku sku = skuList.stream().min(Comparator.comparing(Sku::getSalePrice)).orElse(new Sku());
    productVO.setAmount(sku.getAmount());
    productVO.setSaleAmount(sku.getSaleAmount());
    productVO.setSalePrice(sku.getSalePrice());
    // 查询出商品评论
    CommentVO commentVO =
        Transformer.fromBean(commentService.getProductComment(productId), CommentVO.class);
    if (isNotEmpty(commentVO)) {
      commentVO.setCommentPics(
          commentPicService.list(
              WrapperProvider.queryWrapper(CommentPic::getCommentId, commentVO.getId())));
      productVO.setCommentVO(commentVO);
      productVO.setCommentAmount(commentService.getProductCommentAmount(productId));
    }
    productVO.setPicList(
        productPicService.list(
            WrapperProvider.queryWrapper(ProductPic::getProductId, productVO.getId())));
    productVO.setSkuList(
        skuService.list(WrapperProvider.queryWrapper(Sku::getProductId, productVO.getId())));
    return success(productVO);
  }

  /**
   * 保存该商品规格
   *
   * @param skuList 商品规格集合
   * @param productId 商品id
   */
  private void saveSku(List<Sku> skuList, Long productId) {
    if (isNotNull(skuList)) {
      // 需要保存的sku
      List<Sku> saveSkus =
          skuList.stream().filter(sku -> sku.getId() == null).collect(Collectors.toList());
      // 需要更新的sku
      List<Sku> updateSkus =
          skuList.stream().filter(sku -> sku.getId() != null).collect(Collectors.toList());
      if (isNotNull(updateSkus)) {
        CheckHelper.trueOrThrow(
            skuService.updateBatchById(updateSkus), BizException::new, "更新商品sku失败,请检查相应参数!");
      }
      // 需要删除的sku
      List<Sku> deleteSkus =
          skuService.list(WrapperProvider.queryWrapper(Sku::getProductId, productId)).stream()
              .filter(
                  sku ->
                      updateSkus.stream()
                          .noneMatch(updateSku -> updateSku.getId().equals(sku.getId())))
              .collect(Collectors.toList());
      if (isNotNull(deleteSkus)) {
        boolean b = deleteSkus.stream()
                .allMatch(
                        sku -> skuService.update(WrapperProvider.removeWrapper(Sku::getId, sku.getId())));
        CheckHelper.trueOrThrow(b,BizException::new,"删除商品规格是失败,请检查相应参数!");
      }
      saveSkus =
          saveSkus.stream().peek(sku -> sku.setProductId(productId)).collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          skuService.saveBatch(saveSkus), BizException::new, "添加商品sku失败,请检查相应参数!");
    }
  }

  /**
   * 保存该商品详情图片
   *
   * @param list 图片地址集合
   * @param productId 商品id
   */
  private void saveProductPic(List<String> list, Long productId) {
    CheckHelper.trueOrThrow(
            productPicService.remove(
                    WrapperProvider.queryWrapper(ProductPic::getProductId, productId)),
            BizException::new,
            "删除详情图片失败,请检查相应参数!");
    if (isNotNull(list)) {
      List<ProductPic> pics =
          list.stream()
              .map(
                  image -> {
                    ProductPic pic = new ProductPic();
                    pic.setPicture(image);
                    pic.setProductId(productId);
                    return pic;
                  })
              .collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          productPicService.saveBatch(pics), BizException::new, "商品详情图保存失败,请检查相应参数!");
    }
  }

  /**
   * 根据id查询商品 用于添加,编辑成功时返回 也可用作编辑时查询商品
   *
   * @param id
   * @return
   */
  private ProductVO getProductById(Long id) {
    Product product = baseService.getById(id);
    CheckHelper.trueOrThrow(product != null, BizException::new, "未查出商品,请检查商品id是否正确!");
    ProductVO productVO = Transformer.fromBean(product, ProductVO.class);
    productVO.setCategoryName(categoryService.getById(product.getCategoryId()).getName());
    productVO.setPicList(
        productPicService.list(WrapperProvider.queryWrapper(ProductPic::getProductId, id)));
    productVO.setSkuList(skuService.list(WrapperProvider.queryWrapper(Sku::getProductId, id)));
    return productVO;
  }

  /**
   * 小程序查询查询参数封装 将查询条件拼接
   *
   * @param form
   * @return
   */
  private LambdaQueryWrapper<Product> convertParams(ProductForm form, Boolean onHot) {
    LambdaQueryWrapper<Product> queryWrapper =
        Wrappers.<Product>lambdaQuery().eq(Product::getOnSale, 1);
    if (onHot) {
      queryWrapper.eq(Product::getOnHot, 1);
    }
    if (isNotEmpty(form.getKeyword())) {
      queryWrapper.like(Product::getName, form.getKeyword());
    }
    if (isNotEmpty(form.getCategoryId())) {
      queryWrapper.eq(Product::getCategoryId, form.getCategoryId());
    }
    return queryWrapper.orderByDesc(Product::getOnHot).orderByDesc(Product::getUpdatedAt);
  }

  /**
   * 后台查询参数封装 将查询条件存入map
   *
   * @param form
   * @return
   */
  private Map<String, Object> mapParams(ProductForm form) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotEmpty(form.getKeyword())) {
      params.put("keyword", form.getKeyword());
    }
    if (isNotEmpty(form.getCategoryId())) {
      params.put("categoryId", form.getCategoryId());
    }
    if (isNotEmpty(form.getOnSale())) {
      params.put("onSale", form.getOnSale());
    }
    return params;
  }
}
