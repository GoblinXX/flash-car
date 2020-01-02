package test.template;

import com.byy.api.Application;
import com.byy.dal.entity.beans.point.PointConfig;
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

import static com.byy.dal.enums.SceneType.COMMENT;
import static com.byy.dal.enums.SceneType.NEW_USER_FIRST_SHOPPING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 06:17
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PointConfigTest {
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
                            get("/point/config/back")
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
  public void testPut() throws Exception {
    List<PointConfig> pointConfigList = new ArrayList<>();
    PointConfig pointConfig1 = new PointConfig();
    PointConfig pointConfig2 = new PointConfig();
    pointConfig1.setId(1L);
    pointConfig1.setScene(COMMENT);
    pointConfig1.setAmount(BigDecimal.valueOf(50));
    pointConfig2.setId(2L);
    pointConfig2.setScene(NEW_USER_FIRST_SHOPPING);
    pointConfig2.setAmount(BigDecimal.valueOf(100));
    pointConfigList.add(pointConfig1);
    pointConfigList.add(pointConfig2);
    ImmutableMap<String, Object> map = ImmutableMap.of("pointConfigList",pointConfigList);
    MvcResult result =
            mockMvc
                    .perform(
                            put("/point/config/back")
                                    .content(JSONObject.toJSONString(map))
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }


}
