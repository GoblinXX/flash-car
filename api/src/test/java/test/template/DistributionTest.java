package test.template;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.byy.api.Application;
import com.byy.api.service.form.auditstore.AuditStoreForm;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
 * @program: flash-car
 * @description: 分销测试类
 * @author: Goblin
 * @create: 2019-06-12 14:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistributionTest {

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
        get("/distribution").param("id", "1")
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
