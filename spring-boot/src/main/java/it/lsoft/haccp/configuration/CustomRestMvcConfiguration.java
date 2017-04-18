package it.lsoft.haccp.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class CustomRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {

		return new RepositoryRestConfigurerAdapter() {

			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		        Set<BeanDefinition> components = provider.findCandidateComponents("it.lsoft.haccp.data");
		        List<Class<?>> classes = new ArrayList<>();
		 
		        components.forEach(component -> {
		            try {
		                classes.add(Class.forName(component.getBeanClassName()));
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        });
		 
		        config.exposeIdsFor(classes.toArray(new Class[classes.size()]));
		       
			}
		};
	}
}