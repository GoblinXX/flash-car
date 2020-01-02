package com.byy.dal;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.byy.dal.config.BaseEntityObjectHandler;
import com.byy.dal.mapper.Scanned;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: yyc
 * @date: 19-3-30 下午4:58
 */
@SpringBootConfiguration
@EnableTransactionManagement
@EnableAutoConfiguration
@MapperScan("com.byy.dal.mapper")
@ComponentScan(basePackageClasses = Scanned.class)
public class DalConfig {

  /**
   * 自动填充
   *
   * @return MetaObjectHandler
   */
  @Bean
  public MetaObjectHandler metaObjectHandler() {
    return new BaseEntityObjectHandler();
  }

  /**
   * 逻辑删除注入器
   *
   * @return LogicSqlInjector
   */
  @Bean
  public LogicSqlInjector logicSqlInjector() {
    return new LogicSqlInjector();
  }

  /**
   * 乐观锁插件
   *
   * @return OptimisticLockerInterceptor
   */
  @Bean
  public OptimisticLockerInterceptor optimisticLockerInterceptor() {
    return new OptimisticLockerInterceptor();
  }

  /**
   * 分页插件
   *
   * @return PaginationInterceptor
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }
}
