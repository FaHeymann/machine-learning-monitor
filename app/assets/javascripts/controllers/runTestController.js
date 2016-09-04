export default class RunTestController {
  constructor($http, $window) {
    this.$http = $http;
    this.$window = $window;

    this.status = 'initial';
    this.error = '';
    this.lastResultSetId = 0;

    this.algorithmId = '' + this.algorithms[0].id;
    this.featureSetId = '' + this.featureSets[0].id;
    this.initParameters();
    this.initLabels();
  }

  initParameters() {
    this.parameters = [];
    this.getCurrentAlgorithm().parameters.forEach(
      p => this.parameters.push({
        id: p.id,
        value: p.type === 'ENUM' ? p.enumValues[0].value : '',
        type: p.type,
        name: p.name,
        enumValues: p.enumValues,
      })
    );
  }

  initLabels() {
    this.labels = [];
    this.getCurrentFeatureSet().labelStrings.forEach(
      l => this.labels.push({
        label: l,
        include: true,
      })
    );
  }

  getCurrentAlgorithm() {
    return this.algorithms.find(a => a.id === parseInt(this.algorithmId, 10));
  }

  getCurrentFeatureSet() {
    return this.featureSets.find(f => f.id === parseInt(this.featureSetId, 10));
  }

  submit() {
    this.status = 'submitting';
    this.error = '';

    const body = {
      algorithmId: this.algorithmId,
      featureSetId: this.featureSetId,
      parameters: this.parameters,
      excludeLabels: this.labels.filter(l => !l.include).map(l => l.label),
    };

    this.$http.post('/tests/run', body)
      .then((response) => {
        this.lastResultSetId = response.data.id;

        console.log(response);
      }, response => {
        try {
          this.error = response.data[''][0];
        } catch (e) {
          this.error = 'An error occured';
        }
        this.status = 'initial';
      });
  }
}
