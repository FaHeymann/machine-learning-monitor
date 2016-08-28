module.exports = {
  "extends": "airbnb",
  "plugins": [],
  "settings": {
    "arrow-body-style": ["error", "as-needed", { requireReturnForObjectLiteral: true }],
  },
  "parserOptions": {
    "ecmaFeatures": {
      "jsx": true
    }
  },
  "rules": {
    "prefer-template": 0,
    "consistent-return": 0,
    "no-case-declarations": 0,
  }
};
