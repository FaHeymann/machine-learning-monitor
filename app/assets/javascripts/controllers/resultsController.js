export default class ResultsController {
  constructor($http, $window) {
    this.$http = $http;
    this.$window = $window;

    this.metricOptions = [
      {
        value: 'maxDeviation',
        name: 'Maximum Deviation',
      }, {
        value: 'maxPositiveDeviation',
        name: 'Maximum Positive Deviation',
      }, {
        value: 'maxNegativeDeviation',
        name: 'Maximum Negative Deviation',
      }, {
        value: '25Quantile',
        name: 'Q(0.25)',
      }, {
        value: '50Quantile',
        name: 'Q(0.50)',
      }, {
        value: '75Quantile',
        name: 'Q(0.75)',
      }, {
        value: '90Quantile',
        name: 'Q(0.90)',
      }, {
        value: '99Quantile',
        name: 'Q(0.99)',
      },
    ];

    this.metrics = [this.metricOptions[0]];
    this.chartData = [[]];
    this.chartLabels = [];
    this.chartSeries = [];
    this.chartOptions = {};

    this.featureSetId = this.featureSets.length ? '' + this.featureSets[0].id : '';
    this.algorithmId = this.featureSets.length ? '' + this.algorithms[0].id : '';

    this.fetch();
  }

  handleClick(points) {
    if (!points.length) {
      return;
    }
    // eslint-disable-next-line no-underscore-dangle
    const date = points[0]._xScale.ticks[points[0]._index][0];
    const dataEntry = this.$parent.$ctrl.data.find(entry => entry.createdAt === date);

    this.$parent.$ctrl.$window.location.href = '/results/' + dataEntry.id;
  }

  assignMetrics(metrics) {
    this.chartData = [];
    this.chartSeries = [];
    metrics.forEach((metric) => {
      this.chartData.push(this.data.map(entry => entry[metric.value]));
      this.chartSeries.push(metric.name);
    });
    if (!this.data.length) {
      this.chartData = [[0]];
    }
  }

  update() {
    this.assignMetrics(this.metrics);
  }

  fetch() {
    this.$http.get('/results/search/' + this.featureSetId + '/' + this.algorithmId)
      .then((response) => {
        this.data = response.data;
        this.chartLabels = this.data.map(entry =>
          [entry.createdAt].concat(
            entry.parameterTestValues.map(
              ptv => ptv.parameter.name + ': ' + ptv.valueAsString
            )
          ).concat(
            entry.ignoredLabelStrings.map(ils => 'ignored: ' + ils)
          )
        );
        this.update();
      });
  }
}
