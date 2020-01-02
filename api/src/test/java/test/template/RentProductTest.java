package test.template;

import com.byy.api.Application;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.entity.beans.product.Sku;
import com.google.common.collect.ImmutableMap;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 09:16
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RentProductTest {

  @Autowired
  private WebApplicationContext wac;
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
                            get("/rent/back/page")
                                    .param("page", "1")
                                    .param("size", "8")
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
    List<String> rentProductPics = new ArrayList<>();
    rentProductPics.add("asdgfiuahdfgu");
    rentProductPics.add("icufhgxiudfh");
    rentProductPics.add("sdkjgjsdh");
    List<RentProductTime> rentProductTimeList = new ArrayList<>();
    RentProductTime rentProductTime1 = new RentProductTime();
    rentProductTime1.setTenancy(7);
    rentProductTime1.setPrice(BigDecimal.valueOf(15));
    RentProductTime rentProductTime2 = new RentProductTime();
    rentProductTime2.setTenancy(30);
    rentProductTime2.setPrice(BigDecimal.valueOf(14));
    rentProductTimeList.add(rentProductTime1);
    rentProductTimeList.add(rentProductTime2);
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("name", "牛牛电池");
    map.put("image", "sidfuagsd");
    map.put("amount",6000);
    map.put("costPrice",20);
    map.put("deposit",2000);
    map.put("rentProductTimeList", rentProductTimeList);
    map.put("rentProductPics", rentProductPics);
    map.put("parentCommissionRatio", 12);
    map.put("grandpaCommissionRatio", 3);
    map.put("content", "好用用用用!");
    MvcResult result =
            mockMvc
                    .perform(
                            post("/rent/back")
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
                    .perform(get("/rent/back/1").contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /** 修改方法测试 */
  @Test
  public void testPut() throws Exception {
/*    List<String> rentProductPics = new ArrayList<>();
    rentProductPics.add("asdgfiuahdfgu");
    rentProductPics.add("icufhgxiudfh");
    rentProductPics.add("sdkjgjsdh");*/
    List<Long> rentProductPicDeleteIds = new ArrayList<>();
    rentProductPicDeleteIds.add(6L);
    List<RentProductTime> rentProductTimeList = new ArrayList<>();
    RentProductTime rentProductTime1 = new RentProductTime();
    rentProductTime1.setId(15L);
    rentProductTime1.setTenancy(60);
    rentProductTime1.setPrice(BigDecimal.valueOf(13));
    RentProductTime rentProductTime2 = new RentProductTime();
    rentProductTime2.setTenancy(180);
    rentProductTime2.setPrice(BigDecimal.valueOf(12));
    rentProductTimeList.add(rentProductTime1);
    rentProductTimeList.add(rentProductTime2);
    List<Long> rentProductTimeDeleteIds = new ArrayList<>();
    rentProductTimeDeleteIds.add(14L);
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("id",6L);
    map.put("name", "大型号电池");
    map.put("image", "sidfuagsd");
    map.put("amount",6000);
    map.put("costPrice",20);
    map.put("deposit",2000);
    map.put("rentProductTimeList", rentProductTimeList);
    map.put("rentProductTimeDeleteIds",rentProductTimeDeleteIds);
    /*map.put("rentProductPics", rentProductPics);*/
    map.put("rentProductPicDeleteIds",rentProductPicDeleteIds);
    map.put("parentCommissionRatio", 12);
    map.put("grandpaCommissionRatio", 3);
    map.put("content", "好用奥!");
    MvcResult result =
            mockMvc
                    .perform(
                            put("/rent/back")
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
                    .perform(delete("/rent/back/6")
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
    MvcResult result = mockMvc.perform(put("/rent/back/sale").content(JSONObject.toJSONString(map))
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
                            get("/rent/app/page")
                                    .param("page", "1")
                                    .param("size", "8")
                                    .param("keyword","")
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }


  /** 查询方法测试 */
  @Test
  public void testAppGet() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(get("/rent/app/1").contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
}
