package test.template;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 06:14
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserPointRecordTest {
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
                            get("/point/record/back")
                                    .param("page", "1")
                                    .param("size", "8")
                                    .param("userId","1")
                                    .param("startTime","1560528000000")
                                    .param("endTime","1560614400000")
                                    .contentType("application/json;charset=utf-8"))
                    .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
}
