/*
 * package com.saini.rclone.mapper;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import com.saini.rclone.model.Post; import com.saini.rclone.model.Subreddit;
 * 
 * import lombok.extern.slf4j.Slf4j; import ma.glasnost.orika.CustomMapper;
 * import ma.glasnost.orika.MapperFactory; import
 * ma.glasnost.orika.MappingContext; import
 * ma.glasnost.orika.impl.ConfigurableMapper;
 * 
 * @Component
 * 
 * @Slf4j public class PostMapperOrika extends ConfigurableMapper {
 * 
 * @Override public void configure(MapperFactory factory) {
 * factory.classMap(Post.class, Subreddit.class) .customize(new
 * CustomMapper<Post, Subreddit>() {
 * 
 * @Override public void mapAtoB(Post a, Subreddit b, MappingContext context) {
 * super.mapAtoB(a, b, context); }
 * 
 * @Override public void mapBtoA(Subreddit b, Post a, MappingContext context) {
 * super.mapBtoA(b, a, context); }
 * 
 * });
 * 
 * }
 * 
 * }
 */