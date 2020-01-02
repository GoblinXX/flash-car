package test.template;

import com.byy.api.Application;
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
 * @Date: 14/06/19 上午 09:43
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CategoryTest {
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
                            get("/category/back/page")
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
    ImmutableMap<String,String> map = ImmutableMap.of("name","电池");
    MvcResult result =
            mockMvc
                    .perform(
                            post("/category/back")
                                    .content(JSONObject.toJSONString(map))
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /** 查询方法测试 */
  @Test
  public void testGet() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(get("/category/back/1").contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /** 修改方法测试 */
  @Test
  public void testPut() throws Exception {
    ImmutableMap<String,String> map = ImmutableMap.of("id","3","name","引擎");
    MvcResult result =
            mockMvc
                    .perform(
                            put("/category/back")
                                    .content(JSONObject.toJSONString(map))
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
                    .perform(delete("/category/back/3")
                            .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }



  /** 不分页查询方法测试 */
  @Test
  public void testAppList() throws Exception {
    MvcResult result =
            mockMvc
                    .perform(
                            get("/category/back/list")
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }



}
