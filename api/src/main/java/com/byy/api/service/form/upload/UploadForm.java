package com.byy.api.service.form.upload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class UploadForm {

  /** 文件 */
  @NotNull private MultipartFile file;
}
