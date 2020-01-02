package com.byy.api.service.controller.location;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.BaseController;
import com.byy.api.service.vo.location.AddressChainVO;
import com.byy.biz.service.location.AddressChainService;
import com.byy.biz.service.location.AreaService;
import com.byy.biz.service.location.CityService;
import com.byy.biz.service.location.ProvinceService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.location.Area;
import com.byy.dal.entity.beans.location.City;
import com.byy.dal.entity.beans.location.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.byy.api.response.ResponseObject.success;

/**
 * 省市区固定地址
 *
 * @author: yyc
 * @date: 19-6-12 下午5:08
 */
@RestController
@RequestMapping("/address")
public class LocationController extends BaseController {

  private final ProvinceService provinceService;
  private final CityService cityService;
  private final AreaService areaService;
  private final AddressChainService addressChainService;

  @Autowired
  public LocationController(
      ProvinceService provinceService,
      CityService cityService,
      AreaService areaService,
      AddressChainService addressChainService) {
    this.provinceService = provinceService;
    this.cityService = cityService;
    this.areaService = areaService;
    this.addressChainService = addressChainService;
  }

  /**
   * 根据areaId查询省市区
   *
   * @param areaId Long
   * @return AddressChainVO
   */
  @GetMapping("/chain/{areaId}")
  public ResponseObject<AddressChainVO> getAddressChainByAreaId(@PathVariable Long areaId) {
    return success(
        Transformer.fromBean(addressChainService.loadAddressChain(areaId), AddressChainVO.class));
  }

  /**
   * 查询所有省
   *
   * @return ResponseObject
   */
  @GetMapping("/province")
  public ResponseObject<List<Province>> getProvinces() {
    List<Province> provinces = provinceService.list();
    return success(provinces);
  }

  /**
   * 查询省下面的市
   *
   * @param provinceId
   * @return ResponseObject
   */
  @GetMapping("/city/{provinceId}")
  public ResponseObject<List<City>> getCities(@PathVariable Long provinceId) {
    List<City> cities =
        cityService.list(WrapperProvider.queryWrapper(City::getProvinceId, provinceId));
    return success(cities);
  }

  /**
   * 查询市下面的区
   *
   * @param cityId
   * @return ResponseObject
   */
  @GetMapping("/area/{cityId}")
  public ResponseObject<List<Area>> getAreas(@PathVariable Long cityId) {
    List<Area> areas = areaService.list(WrapperProvider.queryWrapper(Area::getCityId, cityId));
    return success(areas);
  }
}
