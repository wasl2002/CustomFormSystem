package com.form.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.form.system.entity.FormConfig;
import com.form.system.mapper.FormConfigMapper;
import com.form.system.service.FormConfigService;
import org.springframework.stereotype.Service;

@Service
public class FormConfigServiceImpl extends ServiceImpl<FormConfigMapper, FormConfig> implements FormConfigService {
}