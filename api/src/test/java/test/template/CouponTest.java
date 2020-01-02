package test.template;

import com.byy.api.Application;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 04:49
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CouponTest {
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
                            get("/coupon/back/page")
                                    .param("page", "1")
                                    .param("size", "8")
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
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("name","元旦");
    map.put("faceValue","500");
    map.put("amount","10000");
    map.put("validFrom","1576339200000");
    map.put("validTo","1579017600000");
    map.put("conditionUse","500");
    MvcResult result =
            mockMvc
                    .perform(
                            post("/coupon/back")
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
                    .perform(delete("/coupon/back/5")
                            .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
