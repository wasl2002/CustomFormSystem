import InputField from './InputField.vue';
import TextareaField from './TextareaField.vue';
import SelectField from './SelectField.vue';
import RadioField from './RadioField.vue';
import CheckboxField from './CheckboxField.vue';
import DateField from './DateField.vue';
import AttachmentField from './AttachmentField.vue';
import RegionField from './RegionField.vue';
import RatingField from './RatingField.vue';
import FormulaField from './FormulaField.vue';

// 组件映射表
export const formFieldComponents = {
  'input-field': InputField,
  'textarea-field': TextareaField,
  'select-field': SelectField,
  'radio-field': RadioField,
  'checkbox-field': CheckboxField,
  'date-field': DateField,
  'attachment-field': AttachmentField,
  'region-field': RegionField,
  'rating-field': RatingField,
  'formula-field': FormulaField
};

// 组件名称映射表
export const formFieldComponentNames = {
  'input-field': 'InputField',
  'textarea-field': 'TextareaField',
  'select-field': 'SelectField',
  'radio-field': 'RadioField',
  'checkbox-field': 'CheckboxField',
  'date-field': 'DateField',
  'attachment-field': 'AttachmentField',
  'region-field': 'RegionField',
  'rating-field': 'RatingField',
  'formula-field': 'FormulaField'
};

export {
  InputField,
  TextareaField,
  SelectField,
  RadioField,
  CheckboxField,
  DateField,
  AttachmentField,
  RegionField,
  RatingField,
  FormulaField
};