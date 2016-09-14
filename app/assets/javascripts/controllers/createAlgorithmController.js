export default class CreateAlgorithmController {
  constructor($http, $window) {
    this.$http = $http;
    this.$window = $window;

    this.status = 'initial';
    this.error = '';

    this.parameters = [];
    this.name = '';
    this.description = '';
    this.endpoint = '';
  }

  addRow() {
    this.parameters.push({
      name: '',
      type: 'string',
      enumValues: [{ name: '' }],
    });
  }

  removeRow(index) {
    if (index > -1) {
      this.parameters.splice(index, 1);
    }
  }

  submit() {
    this.status = 'submitting';

    const body = {
      name: this.name,
      description: this.description,
      endpoint: this.endpoint,
      parameters: this.parameters.map((parameter) => {
        const normalized = Object.assign({}, parameter);
        normalized.enumValues = parameter.enumValues.map(enumValue => enumValue.name);
        return normalized;
      }),
    };

    this.$http.post('/algorithms/create', body)
      .then(() => {
        this.$window.location.href = '/algorithms';
      }, (response) => {
        try {
          this.error = response.data[''][0];
        } catch (e) {
          this.error = 'An error occured';
        }
        this.status = 'initial';
      });
  }
}
