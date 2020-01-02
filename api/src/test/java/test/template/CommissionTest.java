package test.template;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.byy.api.Application;
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
 * @program: flash-car
 * @description: 分销测试类
 * @author: Goblin
 * @create: 2019-06-12 14:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CommissionTest {

  @Test
  public void contextLoads() {
  }

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;


  @Before // 在测试开始前初始化工作
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }


  /**
   * 编辑
   */
  @Test
  public void testGet() throws Exception {
    MvcResult result = mockMvc.perform(
        get("/commission/records/list").param("keyWord", "2").param("startTime", "1560268800000")
            .param("endTime", "1560441600000")
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 分页条件查询方法测试
   */
  @Test
  public void testList() throws Exception {
    MvcResult result = mockMvc.perform(
        get("/commission/records/app")
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
