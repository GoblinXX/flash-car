package com.byy.api.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author: yyc
 * @date: 19-4-24 上午1:55
 */
@SpringBootConfiguration
public class WebMvcConfig {

  @Value("${spring.jackson.date-format}")
  private String pattern;

  @Bean
  @Primary
  public ObjectMapper serializingObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    addSerializer(javaTimeModule, LocalDateTime.class);
    addSerializer(javaTimeModule, Date.class);

    addDeserializer(javaTimeModule, LocalDateTime.class);
    addDeserializer(javaTimeModule, Date.class);

    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }

  /**
   * 日期序列化json
   *
   * @param javaTimeModule javaTimeModule
   * @param clazz Class
   * @param <T> T
   */
  private <T> void addSerializer(JavaTimeModule javaTimeModule, Class<T> clazz) {
    javaTimeModule.addSerializer(
        clazz,
        new JsonSerializer<T>() {
          @Override
          public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
              throws IOException {
            if (clazz == LocalDateTime.class) {
              gen.writeNumber(
                  ((LocalDateTime) value).toInstant(ZoneOffset.of("+8")).toEpochMilli());
            } else if (clazz == Date.class) {
              gen.writeNumber(((Date) value).getTime());
            } else {
              throw new RuntimeException("日期序列化失败");
            }
          }
        });
  }

  /**
   * json反序列化为日期
   *
   * @param javaTimeModule javaTimeModule
   * @param clazz Class
   * @param <T> T
   */
  @SuppressWarnings("unchecked")
  private <T> void addDeserializer(JavaTimeModule javaTimeModule, Class<T> clazz) {
    javaTimeModule.addDeserializer(
        clazz,
        new JsonDeserializer<T>() {
          @Override
          public T deserialize(JsonParser p, DeserializationContext dc) throws IOException {
            if (clazz == LocalDateTime.class) {
              long time = Long.parseLong(p.getValueAsString());
              return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("+8"));
            } else if (clazz == Date.class) {
              long time = Long.parseLong(p.getValueAsString());
              return (T) new Date(time);
            }
            throw new RuntimeException("日期反序列化失败");
          }
        });
  }
}
