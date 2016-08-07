angular.module('machine-learning-monitor', ['chart.js', 'ui.bootstrap'])

    .directive('results', function() {
        return {
            restrict: 'A',
            scope: true,
            controllerAs: 'ctrl',
            bindToController: {
            	featureSetId: '@',
            	algorithmId: '@'
            },
            controller: function($http) {
            	const ctrl = this;
            	ctrl.metric = "0";
				ctrl.chartData = [[]];
				ctrl.chartLabels = [];
				ctrl.chartSeries = [];
				ctrl.chartOptions = {};

				ctrl.assignMetric = (name) => {
					ctrl.chartData = [
						ctrl.data.map(entry => {
							return entry[name];
						}
					)];
				}

				ctrl.update = () => {
					switch (ctrl.metric) {
						case '0':
							ctrl.assignMetric('maxDeviation');
							break;
						case '1':
							ctrl.assignMetric('maxPositiveDeviation');
							break;
						case '2':
							ctrl.assignMetric('maxNegativeDeviation');
							break;
						case '3':
							ctrl.assignMetric('25Quantile');
							break;
						case '4':
							ctrl.assignMetric('50Quantile');
							break;
						case '5':
							ctrl.assignMetric('75Quantile');
							break;
						case '6':
							ctrl.assignMetric('90Quantile');
							break;
						case '7':
							ctrl.assignMetric('99Quantile');
							break;
					}
				}

				ctrl.fetch = () => {
					$http.get('/results/search/' + ctrl.featureSetId + '/' + ctrl.algorithmId)
						.then(function(response) {
							ctrl.data = response.data;
							ctrl.chartLabels = ctrl.data.map(entry => {
								return entry.createdAt;
							});
							ctrl.update();
						});
				}

				ctrl.fetch();
            }
		}
	})
