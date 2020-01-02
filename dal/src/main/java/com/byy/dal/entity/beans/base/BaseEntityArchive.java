package com.byy.dal.entity.beans.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-3-30 下午5:19
 */
@Setter
@Getter
@ToString
public abstract class BaseEntityArchive extends BaseEntity {

  @TableField(fill = FieldFill.INSERT)
  @TableLogic
  private Boolean archive;
}
