package test.template;

import com.byy.api.Application;
import com.byy.dal.entity.beans.product.Sku;
import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @description: 单元测试模板
 * @author: xcf
 * @create: 2019-06-12 20:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductTest {

  @Autowired private WebApplicationContext wac;
  private MockMvc mockMvc;

  @Test
  public void contextLoads() {}

  @Before // 在测试开始前初始化工作
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  /** 分页条件查询方法测试 */
  @Test
  public void testList() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                get("/product/back/page")
                    .param("page", "1")
                    .param("size", "8")
                    .param("categoryId", "1")
                    .param("onSale", "1")
                    .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * * 新增方法测试
   *
   * @throws Exception
   */
  @Test
  public void testPost() throws Exception {
    List<Sku> skus = new ArrayList<>();
    Sku sku1 = new Sku();
    sku1.setName("50000毫安");
    sku1.setSalePrice(BigDecimal.valueOf(68));
    sku1.setOriginalPrice(BigDecimal.valueOf(98));
    sku1.setCostPrice(BigDecimal.valueOf(38));
    sku1.setAmount(5000);
    skus.add(sku1);
    Sku sku2 = new Sku();
    sku2.setName("100000毫安");
    sku2.setSalePrice(BigDecimal.valueOf(998));
    sku2.setOriginalPrice(BigDecimal.valueOf(1998));
    sku2.setCostPrice(BigDecimal.valueOf(18));
    sku2.setAmount(50);
    skus.add(sku2);
    List<String> productPics = new ArrayList<>();
    productPics.add("zksdjfgjsd");
    productPics.add("sdfhsfdh");
    productPics.add("dghjdgh");
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("name", "超级电池");
    map.put("image", "sidfuagsd");
    map.put("categoryId", 1);
    map.put("installFee", 30);
    map.put("serviceFee", 50);
    map.put("skuList", skus);
    map.put("productPics", productPics);
    map.put("parentCommissionRatio", 12);
    map.put("grandpaCommissionRatio", 3);
    map.put("content", "牛批!");
    MvcResult result =
        mockMvc
            .perform(
                post("/product/back")
                    .content(JSONObject.toJSONString(map.build()))
                    .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /** 查询方法测试 */
  @Test
  public void testGet() throws Exception {
    MvcResult result =
        mockMvc
            .perform(get("/product/back/1").contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /** 修改方法测试 */
  @Test
  public void testPut() throws Exception {
    List<Sku> skuList = new ArrayList<>();
    Sku sku1 = new Sku();
    sku1.setName("600000毫安");
    sku1.setSalePrice(BigDecimal.valueOf(68));
    sku1.setOriginalPrice(BigDecimal.valueOf(98));
    sku1.setCostPrice(BigDecimal.valueOf(38));
    sku1.setAmount(5000);
    skuList.add(sku1);
    Sku sku2 = new Sku();
    sku2.setName("3000000毫安");
    sku2.setSalePrice(BigDecimal.valueOf(998));
    sku2.setOriginalPrice(BigDecimal.valueOf(1998));
    sku2.setCostPrice(BigDecimal.valueOf(18));
    sku2.setAmount(50);
    skuList.add(sku2);
    List<String> productPics = new ArrayList<>();
    productPics.add("zksdjfgjsd");
    productPics.add("sdfhsfdh");
    productPics.add("dghjdgh");
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("id", 10);
    map.put("name", "超级扩容电池");
    map.put("image", "sidfuagsd");
    map.put("categoryId", 1);
    map.put("installFee", 30);
    map.put("serviceFee", 50);
    map.put("skuList", skuList);
    map.put("productPics",productPics);
    map.put("parentCommissionRatio", 12);
    map.put("grandpaCommissionRatio", 3);
    map.put("content", "好用奥!");
    MvcResult result =
        mockMvc
            .perform(
                put("/product/back")
                    .content(JSONObject.toJSONString(map.build()))
                    .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * * 删除方法测试
   *
   * @throws Exception
   */
  @Test
  public void testDelete() throws Exception {
    MvcResult result =
        mockMvc
            .perform(delete("/product/back/6")
                    .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 更新申请状态方法测试
   */
  @Test
  public void testProductStatusSys() throws Exception {
    ImmutableMap<String, String> map = ImmutableMap.of("id", "1", "onSale", "true");
    MvcResult result = mockMvc.perform(put("/product/back/status").content(JSONObject.toJSONString(map))
            .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());

  }


  /** 分页条件查询方法测试 */
  @Test
  public void testAppList() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(
                            get("/product/app/page")
                                    .param("page", "1")
                                    .param("size", "8")
                                    .param("categoryId", "1")
/*                                    .param("keyword","车")*/
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }


  /** 查询方法测试 */
  @Test
  public void testAppGet() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(get("/product/app/1").contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }


}
