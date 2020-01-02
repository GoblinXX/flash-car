package com.byy.api.service.controller.home;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.home.CarouselImgForm;
import com.byy.api.service.vo.home.CarouselImgVO;
import com.byy.api.service.vo.product.CategoryVO;
import com.byy.api.service.vo.product.ProductVO;
import com.byy.api.service.vo.product.RentProductVO;
import com.byy.biz.service.home.CarouselImgService;
import com.byy.biz.service.product.CategoryService;
import com.byy.biz.service.product.ProductService;
import com.byy.biz.service.product.RentProductService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.home.CarouselImg;
import com.byy.dal.entity.beans.product.Category;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.beans.product.RentProduct;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;
import static com.byy.dal.enums.JumpType.*;

/** @Author: xcf @Date: 19/06/19 下午 02:24 @Description:轮播图 */
@RestController
@AllArgsConstructor
@RequestMapping("/carousel")
public class CarouselImgController extends CommonController<CarouselImgService> {

  private CategoryService categoryService;

  private ProductService productService;

  private RentProductService rentProductService;

  /**
   * 后台根据状态查租赁商品或商品分类
   *
   * @param form
   * @return
   */
  @GetMapping("/back")
  public ResponseObject<ImmutableMap<String, Object>> getCategoryByStatusSys(CarouselImgForm form) {
    if (form.getProductOrRent()) {
      IPage<Category> page = categoryService.page(form.newPage());
      List<CategoryVO> categoryVOS = Transformer.fromList(page.getRecords(), CategoryVO.class);
      return success(ImmutableMap.of("list", categoryVOS, "total", page.getTotal()));
    } else {
      IPage<RentProduct> page = rentProductService.page(form.newPage());
      List<RentProductVO> rentProductVOS =
          Transformer.fromList(page.getRecords(), RentProductVO.class);
      return success(ImmutableMap.of("list", rentProductVOS, "total", page.getTotal()));
    }
  }

  /**
   * 后台根据分类id查常规商品
   *
   * @param form
   * @return
   */
  @GetMapping("/back/product")
  public ResponseObject<ImmutableMap<String, Object>> getProductByCategoryIdSys(
      CarouselImgForm form) {
    IPage<Product> page =
        productService.page(
            form.newPage(),
            WrapperProvider.queryWrapper(Product::getCategoryId, form.getCategoryId()));
    List<ProductVO> productVOS = Transformer.fromList(page.getRecords(), ProductVO.class);
    return success(ImmutableMap.of("list", productVOS, "total", page.getTotal()));
  }

  /**
   * 后台添加轮播图
   *
   * @param form
   * @return
   */
  @PostMapping("/back")
  public ResponseObject<CarouselImgVO> saveCarouselImgSys(@RequestBody CarouselImgForm form) {
    if (baseService.count() < 5) {
      CheckHelper.trueOrThrow(
          baseService.save(Transformer.fromBean(form, CarouselImg.class)),
          BizException::new,
          "添加轮播图失败,请检查相应字段!");
      return success(Transformer.fromBean(form, CarouselImgVO.class));
    } else {
      return error(-1, "轮播图不能超过五张!");
    }
  }

  /**
   * 后台查询所有轮播图,不分页
   *
   * @return
   */
  @GetMapping("/back/list")
  public ResponseObject<List<CarouselImgVO>> getAllCarouselImgSys() {
    List<CarouselImg> carouselImgs = baseService.list();
    return success(Transformer.fromList(carouselImgs, CarouselImgVO.class));
  }

  /**
   * 后台通过id查询轮播图
   *
   * @param carouselImgId
   * @return
   */
  @GetMapping("/back/single/{carouselImgId}")
  public ResponseObject<CarouselImgVO> getCarouselImgByIdSys(
      @PathVariable("carouselImgId") Long carouselImgId) {
    CarouselImgVO carouselImgVO =
        Transformer.fromBean(baseService.getById(carouselImgId), CarouselImgVO.class);
    if (carouselImgVO.getJumpType().equals(PRODUCT)) {
      if (carouselImgVO.getProductId() != null) {
        Product product = productService.getById(carouselImgVO.getProductId());
        if (product != null) {
          carouselImgVO.setProductName(product.getName());
          carouselImgVO.setCategoryId(product.getCategoryId());
          carouselImgVO.setCategoryName(categoryService.getById(product.getCategoryId()).getName());
        }
        return success(carouselImgVO);
      } else {
        RentProduct rentProduct = rentProductService.getById(carouselImgVO.getRentProductId());
        carouselImgVO.setRentProductName(rentProduct.getName());
        return success(carouselImgVO);
      }
    } else {
      return success(carouselImgVO);
    }
  }

  /**
   * 后台编辑轮播图
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  public ResponseObject<CarouselImgVO> modifyCarouselImgByIdSys(@RequestBody CarouselImgForm form) {
    CheckHelper.trueOrThrow(
        baseService.updateById(Transformer.fromBean(form, CarouselImg.class)),
        BizException::new,
        "编辑轮播图失败,请检查相应字段!");
    return success(Transformer.fromBean(form, CarouselImgVO.class));
  }

  /**
   * 后台删除轮播图
   *
   * @param carouselImgId
   * @return
   */
  @DeleteMapping("/back/{carouselImgId}")
  public ResponseObject<Object> removeCarouselImgByIdSys(
      @PathVariable("carouselImgId") Long carouselImgId) {
    boolean update =
        baseService.update(WrapperProvider.removeWrapper(CarouselImg::getId, carouselImgId));
    if (update) {
      return success(carouselImgId);
    } else {
      return error(-1, "删除轮播图失败!");
    }
  }

  /**
   * 小程序端查询轮播图
   *
   * @return
   */
  @GetMapping("/app")
  public ResponseObject<List<CarouselImgVO>> getAllCarouselImgApp() {
    return success(Transformer.fromList(baseService.list(), CarouselImgVO.class));
  }
}
