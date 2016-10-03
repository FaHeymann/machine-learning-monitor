module.exports = {
  extends: 'airbnb-base',
  rules: {
    "comma-dangle": 'error',
    'prefer-template': 'off',
    'consistent-return': 'error',
    'no-case-declarations': 'off',
    'no-plusplus': ['error', { 'allowForLoopAfterthoughts': true }],
  },
};
