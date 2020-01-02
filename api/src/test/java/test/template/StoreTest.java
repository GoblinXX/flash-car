package test.template;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.byy.api.Application;
import com.byy.api.service.form.auditstore.AuditStoreForm;
import com.google.common.collect.ImmutableMap;
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
 * @description: 门店测试类
 * @author: Goblin
 * @create: 2019-06-12 14:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StoreTest {

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


  /***
   * 新增方法测试
   * @throws Exception
   */
  @Test
  public void testPost() throws Exception {
    HashMap<String, Object> map = Maps.newHashMap();
    map.put("username", "admin");
    map.put("password", "123456");

    List<String> images = Lists.newArrayList();
    images.add("asdasdasdasdasdasd");
    images.add("dasdasdasdasdasdasd");
    images.add("asdas121212");
    map.put("images", images);

    AuditStoreForm auditStoreForm = new AuditStoreForm();
    auditStoreForm.setName("Goblin");
    auditStoreForm.setPhone("15623543981");
    auditStoreForm.setBusinessLicenseUp("dasdasdas");
    auditStoreForm.setBusinessLicenseDown("sadasdasd");
    auditStoreForm.setAddress("你猜");
    auditStoreForm.setAreaId(1L);
    auditStoreForm.setIdCardDown("121sdasd");
    auditStoreForm.setIdCardUp("adasd111");
    map.put("auditStoreForm", auditStoreForm);

    map.put("name", "asdasd");
    map.put("phone", "123456");
    map.put("image", "dasdasd");
    map.put("phone", "153215461231");
    map.put("content", "类似了");
    MvcResult result = mockMvc.perform(post("/store").
        content(JSONObject.toJSONString(map)).contentType("application/json;charset=utf-8")
    ).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 修改
   */
  @Test
  public void testPut() throws Exception {
    HashMap<String, Object> map = Maps.newHashMap();
    map.put("id", "15");
    map.put("name", "啦啦啦");
    MvcResult result = mockMvc.perform(
        put("/store").content(JSONObject.toJSONString(map))
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
  /**
   * 编辑
   */
  @Test
  public void testGet() throws Exception {
    MvcResult result = mockMvc.perform(
        get("/store/list").param("onSale", "1")
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * 编辑
   */
  @Test
  public void testList() throws Exception {
    MvcResult result = mockMvc.perform(
        get("/store/list").param("page", "1").param("size", "10")
            .param("name", "").param("onSale", "")
            .contentType("application/json;charset=utf-8")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
