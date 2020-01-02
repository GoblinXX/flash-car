package test.template;

import com.byy.api.Application;
import com.byy.dal.entity.beans.product.RentProductTime;
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

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 09:58
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RoadRescueTest {
  @Autowired
  private WebApplicationContext wac;
  private MockMvc mockMvc;

  @Test
  public void contextLoads() {}

  @Before // 在测试开始前初始化工作
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  /** 查询方法测试 */
  @Test
  public void testAppGet() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(get("/rescue/back").contentType("application/json;charset=utf-8"))
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
    ImmutableMap<String, String> map = ImmutableMap.of("id","1","name","道路救援","price","50","picture","skfghasgd");
    MvcResult result =
            mockMvc
                    .perform(
                            post("/rescue/back")
                                    .content(JSONObject.toJSONString(map))
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
