export const required = fieldName => `${fieldName} é requerido`;

export const integerOnly = fieldName => 'Valor inválido!';

export const minLength = length => {
  return (fieldName) => `${fieldName} deve ter no mínimo ${length} characters`;
};