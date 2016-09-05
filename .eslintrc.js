module.exports = {
  extends: 'airbnb/base',
  rules: {
    'prefer-template': 'off',
    'consistent-return': 'error',
    'no-case-declarations': 'off',
    'arrow-parens': ['error', 'as-needed'],
    'arrow-body-style': ['error', 'as-needed', { requireReturnForObjectLiteral: true }],
  },
};
