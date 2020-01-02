package com.byy.dal.entity.beans.storepic;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 门店详情图
 * @author: Goblin
 * @create: 2019-06-11 15:54
 **/
@Alias("StorePic")
@Setter
@Getter
@ToString
public class StorePic extends BaseEntityArchive {

  /**
   * 门店id
   */
  private Long storeId;
  /**
   * 图片地址
   */
  private String image;
}
