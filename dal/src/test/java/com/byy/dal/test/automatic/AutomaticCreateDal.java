package com.byy.dal.test.automatic;

import com.byy.dal.DalConfig;
import com.byy.dal.entity.beans.cash.Cash;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.beans.forum.InvitationPic;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.beans.home.CarouselImg;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 自动创建dal
 *
 * @author: yyc
 * @date: 19-4-2 上午12:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DalConfig.class)
public class AutomaticCreateDal {

  private boolean overWriter = true; // 是否覆盖

  /**
   * todo 自动生成mapper.xml
   *
   * @throws Exception Exception
   */
  @Test
  public void automaticCreate() throws Exception {
    createMapperXml(InvitationDetail.class);
    createMapperXml(InvitationPic.class);
    createMapperXml(InvitationReply.class);
  }

  /**
   * 创建MapperXml
   *
   * @param clazz Class
   * @throws Exception Exception
   */
  private void createMapperXml(Class<?> clazz) throws Exception {
    String baseMapperXml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n"
            + "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
            + "<mapper namespace=\"com.byy.dal.mapper.%s.%sMapper\">\n"
            + "\n"
            + "    <!-- 通用查询映射结果 -->\n"
            + "    <resultMap id=\"BaseResultMap\" type=\"%s\">\n"
            + "%s\n"
            + "    </resultMap>\n"
            + "\n"
            + "    <!-- 通用查询结果列 -->\n"
            + "    <sql id=\"Base_Column_List\">\n"
            + "\t\t%s\n"
            + "    </sql>\n"
            + "\n"
            + "</mapper>\n";
    List<Field> fieldList = appendFieldList(clazz);
    StringBuilder builder = new StringBuilder();
    StringBuilder columns = new StringBuilder();
    String tableAlias = genUpperChar(clazz.getSimpleName()).toLowerCase();
    fieldList.forEach(
        field -> {
          boolean usable =
              (typeMap.containsKey(field.getType()) || field.getType().isEnum())
                  && !Modifier.isStatic(field.getModifiers());
          if (usable) {
            if (field.getName().equals("id")) {
              builder.append("\t\t<id column=\"id\" property=\"id\"/>\n");
            } else {
              builder.append(
                  String.format(
                      "\t\t<result column=\"%s\" property=\"%s\"/>\n",
                      toUnderLine(field.getName()), field.getName()));
            }
            columns.append(tableAlias).append(".").append(toUnderLine(field.getName())).append(",");
          }
        });
    String simpleName = clazz.getSimpleName();
    String className = clazz.getName();
    String s = className.substring(0, className.lastIndexOf("."));
    s = s.substring(s.lastIndexOf(".") + 1);
    String filePath =
        System.getProperty("user.dir").replaceAll("api", "dal")
            + "/src/main/resources/mapper/"
            + s
            + "/"
            + simpleName
            + "Mapper.xml";
    String content =
        String.format(
            baseMapperXml,
            s,
            simpleName,
            simpleName,
            builder.toString(),
            columns.substring(0, columns.length() - 1));
    writeClass(filePath, content);
  }

  private String toUnderLine(String str) {
    str = prefixToLower(str);
    return String.join("_", str.replaceAll("([A-Z])", ",$1").split(",")).toLowerCase();
  }

  private String prefixToLower(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }

  private List<Field> appendFieldList(Class<?> clazz) {
    List<Field> fieldList = Lists.newArrayList();
    while (clazz != Object.class) {
      Field[] fields = clazz.getDeclaredFields();
      List<Field> newArrayList = Lists.newArrayList();
      newArrayList.addAll(Arrays.asList(fields));
      newArrayList.addAll(fieldList);
      clazz = clazz.getSuperclass();
      fieldList = newArrayList;
    }
    return fieldList;
  }

  /**
   * 输出文件
   *
   * @param filePath filePath
   * @param content content
   * @throws Exception Exp
   */
  private void writeClass(String filePath, String content) throws Exception {
    File file = new File(filePath);
    if (!overWriter && file.exists()) {
      System.out.println(filePath.substring(filePath.lastIndexOf("/") + 1) + "已经存在，无法创建!");
      return;
    }
    String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
    File dir = new File(dirPath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    try (PrintWriter pw = new PrintWriter(filePath)) {
      pw.println(content);
      System.out.println("创建" + filePath.substring(filePath.lastIndexOf("/") + 1) + "成功!");
    }
  }

  /**
   * 获取所有大写字母
   *
   * @param str String
   * @return String
   */
  private String genUpperChar(String str) {
    StringBuilder builder = new StringBuilder();
    char[] chars = str.toCharArray();
    for (char c : chars) {
      if (c >= 'A' && c <= 'Z') {
        builder.append(c);
      }
    }
    return builder.toString();
  }

  private static ImmutableMap<Class<?>, String> typeMap;

  static {
    typeMap =
        ImmutableMap.<Class<?>, String>builder()
            .put(String.class, "varchar(45)")
            .put(LocalDateTime.class, "datetime")
            .put(Date.class, "datetime")
            .put(Enum.class, "varchar(45)")
            .put(boolean.class, "tinyint(1) default 0")
            .put(int.class, "int(11)")
            .put(double.class, "decimal(10,2)")
            .put(long.class, "bigint(20)")
            .put(Boolean.class, "tinyint(1) default 0")
            .put(Integer.class, "int(11)")
            .put(Double.class, "decimal(10,2)")
            .put(Long.class, "bigint(20)")
            .put(BigDecimal.class, "decimal(10,2)")
            .build();
  }
}
