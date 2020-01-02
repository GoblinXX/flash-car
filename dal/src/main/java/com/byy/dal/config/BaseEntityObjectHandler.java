package com.byy.dal.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author yyc
 * @date 19-3-29 上午12:57
 */
@Slf4j
public class BaseEntityObjectHandler implements MetaObjectHandler {

  private static final String CREATED_TIME_FIELD_NAME = "createdAt";
  private static final String UPDATED_TIME_FIELD_NAME = "updatedAt";
  private static final String ARCHIVE_FIELD_NAME = "archive";

  @Override
  public void insertFill(MetaObject metaObject) {
    Object createdAtField = getFieldValByName(CREATED_TIME_FIELD_NAME, metaObject);
    if (createdAtField == null) {
      log.debug("----- auto fill " + CREATED_TIME_FIELD_NAME + " -----");
      setFieldValByName(CREATED_TIME_FIELD_NAME, LocalDateTime.now(), metaObject);
    }

    Object updatedAtField = getFieldValByName(UPDATED_TIME_FIELD_NAME, metaObject);
    if (updatedAtField == null) {
      log.debug("----- auto fill " + UPDATED_TIME_FIELD_NAME + " -----");
      setFieldValByName(UPDATED_TIME_FIELD_NAME, LocalDateTime.now(), metaObject);
    }

    Object archiveField = getFieldValByName(ARCHIVE_FIELD_NAME, metaObject);
    if (archiveField == null) {
      log.debug("----- auto fill " + ARCHIVE_FIELD_NAME + " -----");
      setFieldValByName(ARCHIVE_FIELD_NAME, false, metaObject);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    log.debug("----- update auto fill " + UPDATED_TIME_FIELD_NAME + " -----");
    setFieldValByName(UPDATED_TIME_FIELD_NAME, LocalDateTime.now(), metaObject);
  }
}
