export default class ResultsController {
  constructor($http, $window) {
    this.$http = $http;
    this.$window = $window;

    this.metric = 'maxDeviation';
    this.chartData = [[]];
    this.chartLabels = [];
    this.chartSeries = [];
    this.chartOptions = {};

    this.fetch();
  }

  handleClick(points) {
    if (!points.length) {
      return;
    }

    const date = points[0].label;

    const dataEntry = this.data.find(entry => {
      return entry.createdAt === date;
    });

    this.$window.location.href = '/results/' + dataEntry.id;
  }

  assignMetric(name) {
    this.chartData = [
      this.data.map(entry => {
        return entry[name];
      }
    )];
    if (!this.data.length) {
      this.chartData = [[0]];
    }
  }

  update() {
    this.assignMetric(this.metric);
  }

  fetch() {
    this.$http.get('/results/search/' + this.featureSetId + '/' + this.algorithmId)
      .then((response) => {
        this.data = response.data;
        this.chartLabels = this.data.map(entry => {
          return entry.createdAt;
        });
        this.update();
      });
  }
}
