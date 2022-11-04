package com.kristianm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class DeviceContinuityInterceptorAppConfig extends WebMvcConfigurerAdapter {
   @Autowired
   DeviceContinuityInterceptor deviceContinuityInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(deviceContinuityInterceptor);
   }
}