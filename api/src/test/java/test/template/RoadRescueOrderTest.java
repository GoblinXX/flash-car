package test.template;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.byy.api.Application;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

/**
 * @Author: Goblin
 * @Date: 13/06/19 下午 09:58
 * @Description: 道路救援订单测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RoadRescueOrderTest {

  @Autowired
  private WebApplicationContext wac;
  private MockMvc mockMvc;

  @Test
  public void contextLoads() {
  }

  @Before // 在测试开始前初始化工作
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }


  /**
   * 查看详情方法测试
   */
  @Test
  public void testGet() throws Exception {
    MvcResult result =
        mockMvc
            .perform(get("/road/order/1").contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 查询方法测试
   */
  @Test
  public void testAppGet() throws Exception {
    MvcResult result =
        mockMvc
            .perform(get("/road/order/app").param("page", "1")
                .param("size", "8").contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 条件分页查询方法测试
   */
  @Test
  public void testGetList() throws Exception {
    MvcResult result =
        mockMvc
            .perform(get("/road/order/back/list").param("page", "1")
                .param("size", "8").contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * * 新增方法测试
   */
  @Test
  public void testPost() throws Exception {
    HashMap<String, Object> map = Maps.newHashMap();
    map.put("buyerName", "张三");
    map.put("buyerPhone", "15623543981");
    map.put("address", "湖北省武汉市硚口区");
    map.put("detailedAddress", "丰盛路2211");
    map.put("storeId", 1L);
    map.put("goodsFee", 50);
    List<String> images = Lists.newArrayList();
    images.add("asdasdasd");
    images.add("dasd11dvsvc");
    map.put("images", images);
    MvcResult result =
        mockMvc
            .perform(
                post("/road/order")
                    .content(JSONObject.toJSONString(map))
                    .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 修改方法测试
   */
  @Test
  public void testPut() throws Exception {
    HashMap<String, Object> map = Maps.newHashMap();
    map.put("id", 1);
    map.put("totalFee", 50);
    MvcResult result =
        mockMvc
            .perform(put("/road/order").content(JSONObject.toJSONString(map))
                .contentType("application/json;charset=utf-8"))
            .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
