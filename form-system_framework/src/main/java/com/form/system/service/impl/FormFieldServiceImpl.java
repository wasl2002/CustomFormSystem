package com.form.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.form.system.entity.FormField;
import com.form.system.mapper.FormFieldMapper;
import com.form.system.service.FormFieldService;
import org.springframework.stereotype.Service;

@Service
public class FormFieldServiceImpl extends ServiceImpl<FormFieldMapper, FormField> implements FormFieldService {
}